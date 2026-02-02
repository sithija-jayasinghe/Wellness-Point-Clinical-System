package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.AppointmentDto;
import edu.icet.dto.AuditLogDto;
import edu.icet.dto.NotificationDto;
import edu.icet.entity.Appointment;
import edu.icet.entity.DoctorSchedule;
import edu.icet.entity.Patient;
import edu.icet.exception.BookingFullException;
import edu.icet.exception.InvalidOperationException;
import edu.icet.exception.ResourceNotFoundException;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.DoctorScheduleRepository;
import edu.icet.repository.PatientRepository;
import edu.icet.service.AppointmentService;
import edu.icet.service.AuditLogService;
import edu.icet.service.NotificationService;
import edu.icet.util.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private static final String APPOINTMENT_NOT_FOUND = "Appointment not found";
    private static final String SCHEDULE_NOT_FOUND = "Schedule not found";

    private final AppointmentRepository appointmentRepo;
    private final DoctorScheduleRepository scheduleRepo;
    private final PatientRepository patientRepo;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public AppointmentDto bookAppointment(AppointmentDto dto) {

        if (dto.getScheduleId() == null) {
            throw new IllegalArgumentException("Schedule ID is required");
        }
        if (dto.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (dto.getAppointmentTime() == null) {
            throw new IllegalArgumentException("Appointment time is required");
        }

        // 1) Retrieve schedule
        DoctorSchedule schedule = scheduleRepo.findById(dto.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException(SCHEDULE_NOT_FOUND));

        // 2) Validate time within schedule
        if (dto.getAppointmentTime().isBefore(schedule.getStartDateTime()) ||
                dto.getAppointmentTime().isAfter(schedule.getEndDateTime())) {
            throw new InvalidOperationException("Appointment time falls outside the doctor's schedule");
        }

        // NEW: Check for Patient Double Booking (Cannot have another active appointment at the exact same time)
        if (appointmentRepo.existsByPatientIdAndAppointmentTimeAndStatusNotAndDeletedFalse(
                dto.getPatientId(), dto.getAppointmentTime(), AppointmentStatus.CANCELLED)) {
            throw new InvalidOperationException("Patient already has an active appointment at " + dto.getAppointmentTime());
        }

        // NEW: Check for Doctor/Schedule Time Slot Availability (Doctor cannot see two patients at exact same time)
        if (appointmentRepo.existsByDoctorScheduleDoctorIdAndAppointmentTimeAndStatusNotAndDeletedFalse(
                schedule.getDoctorId(), dto.getAppointmentTime(), AppointmentStatus.CANCELLED)) {
            throw new BookingFullException("Doctor is already booked at " + dto.getAppointmentTime());
        }

        // 3) Capacity check
        long currentCount = appointmentRepo.countByDoctorScheduleIdAndDeletedFalse(dto.getScheduleId());
        if (currentCount >= schedule.getMaxPatients()) {
            throw new BookingFullException("Booking full for this schedule");
        }

        // 4) Queue number (appointmentNo)
        Integer maxAppointmentNo = appointmentRepo.findMaxAppointmentNo(dto.getScheduleId());
        int newAppointmentNo = (maxAppointmentNo == null) ? 1 : maxAppointmentNo + 1;

        // 5) Map DTO -> Entity
        Appointment appointment = mapper.convertValue(dto, Appointment.class);

        // 6) Link schedule and set system fields
        appointment.setDoctorSchedule(schedule);
        appointment.setAppointmentNo(newAppointmentNo);
        appointment.setStatus(AppointmentStatus.BOOKED);

        Appointment savedAppointment = appointmentRepo.save(appointment);

        // 7) Notify patient (safe optional usage)
        patientRepo.findById(dto.getPatientId())
                .filter(p -> p.getUserId() != null)
                .ifPresent(p -> {
                    NotificationDto notif = new NotificationDto();
                    notif.setUserId(p.getUserId());
                    notif.setMessage("Your appointment is booked successfully. No: " + newAppointmentNo);
                    notificationService.sendNotification(notif);
                });

        return mapToDto(savedAppointment);
    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> all = appointmentRepo.findByDeletedFalse();
        List<AppointmentDto> dtos = new ArrayList<>();
        all.forEach(a -> dtos.add(mapToDto(a)));
        return dtos;
    }

    @Override
    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepo.findById(id)
                .filter(a -> !a.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException(APPOINTMENT_NOT_FOUND));
        return mapToDto(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(APPOINTMENT_NOT_FOUND));
        appointment.setDeleted(true);
        appointmentRepo.save(appointment);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepo.findById(id)
                .filter(a -> !a.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException(APPOINTMENT_NOT_FOUND));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
             throw new InvalidOperationException("Appointment is already cancelled");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new InvalidOperationException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepo.save(appointment);

        // Notify patient
        if (appointment.getPatientId() != null) {
            patientRepo.findById(appointment.getPatientId())
                    .filter(p -> p.getUserId() != null)
                    .ifPresent(p -> {
                        NotificationDto notif = new NotificationDto();
                        notif.setUserId(p.getUserId());
                        notif.setMessage("Your appointment #" + appointment.getAppointmentNo() + " has been cancelled.");
                        notificationService.sendNotification(notif);
                    });
        }
    }

    @Override
    @Transactional
    public void completeAppointment(Long id) {
        Appointment appointment = appointmentRepo.findById(id)
                .filter(a -> !a.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException(APPOINTMENT_NOT_FOUND));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidOperationException("Cannot complete a cancelled appointment");
        }

        // Ensure strictly from BOOKED (or IN_PROGRESS if we had it)
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
             throw new InvalidOperationException("Appointment is already completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepo.save(appointment);

        // Retrieve userId from patient
        Long userId = null;
        if (appointment.getPatientId() != null) {
            Patient patient = patientRepo.findById(appointment.getPatientId()).orElse(null);
            if (patient != null) userId = patient.getUserId();
        }

        // Audit log
        AuditLogDto auditLog = new AuditLogDto();
        auditLog.setUserId(userId);
        auditLog.setAction("CONSULTATION_COMPLETED");
        auditLog.setEntity("Appointment");
        auditLog.setEntityId(id);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogService.createLog(auditLog);
    }

    @Override
    @Transactional
    public AppointmentDto updateAppointment(Long id, AppointmentDto dto) {
        Appointment existing = appointmentRepo.findById(id)
                .filter(a -> !a.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException(APPOINTMENT_NOT_FOUND));

        // If schedule changed, fetch and attach
        if (dto.getScheduleId() != null) {
            DoctorSchedule newSchedule = scheduleRepo.findById(dto.getScheduleId())
                    .orElseThrow(() -> new ResourceNotFoundException(APPOINTMENT_NOT_FOUND));
            existing.setDoctorSchedule(newSchedule);
        }

        if (dto.getAppointmentTime() != null) {
            existing.setAppointmentTime(dto.getAppointmentTime());
        }

        Appointment saved = appointmentRepo.save(existing);
        return mapToDto(saved);
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        AppointmentDto dto = mapper.convertValue(appointment, AppointmentDto.class);
        if (appointment.getDoctorSchedule() != null) {
            dto.setScheduleId(appointment.getDoctorSchedule().getId());
        }
        return dto;
    }
}
