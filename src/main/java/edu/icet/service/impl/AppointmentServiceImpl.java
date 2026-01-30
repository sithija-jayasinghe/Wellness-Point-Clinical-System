package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.AppointmentDto;
import edu.icet.entity.Appointment;
import edu.icet.entity.DoctorSchedule;
import edu.icet.entity.Patient;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.DoctorScheduleRepository;
import edu.icet.repository.PatientRepository;
import edu.icet.service.AppointmentService;
import edu.icet.service.AuditLogService;
import edu.icet.service.NotificationService;
import edu.icet.dto.AuditLogDto;
import edu.icet.dto.NotificationDto;
import edu.icet.util.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final DoctorScheduleRepository scheduleRepo;
    private final PatientRepository patientRepo; // Inject PatientRepo
    private final NotificationService notificationService; // Inject NotificationService
    private final AuditLogService auditLogService;
    private final ObjectMapper mapper;

    @Override
    public AppointmentDto bookAppointment(AppointmentDto dto) {
        // 1. Retrieve the Schedule
        DoctorSchedule schedule = scheduleRepo.findById(dto.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // Validation logic
        if (dto.getAppointmentTime().isBefore(schedule.getStartDateTime()) ||
                dto.getAppointmentTime().isAfter(schedule.getEndDateTime())) {
            throw new RuntimeException("Appointment time falls outside the doctor's schedule.");
        }

        // Count logic
        long currentCount = appointmentRepo.countByDoctorScheduleId(dto.getScheduleId());
        if (currentCount >= schedule.getMaxPatients()) {
            throw new RuntimeException("Booking Full");
        }

        // Logic for Queue Number
        Integer maxAppointmentNo = appointmentRepo.findMaxAppointmentNo(dto.getScheduleId());
        int newAppointmentNo = (maxAppointmentNo == null) ? 1 : maxAppointmentNo + 1;

        // 2. Map DTO to Entity
        Appointment appointment = mapper.convertValue(dto, Appointment.class);

        // 3. MANUALLY CONNECT THE RELATIONSHIP
        appointment.setDoctorSchedule(schedule);
        appointment.setAppointmentNo(newAppointmentNo);
        appointment.setStatus(AppointmentStatus.BOOKED);

        Appointment savedAppointment = appointmentRepo.save(appointment);

        // Notify Patient
        Optional<Patient> patientOpt = patientRepo.findById(dto.getPatientId());
        if (patientOpt.isPresent() && patientOpt.get().getUserId() != null) {
            NotificationDto notif = new NotificationDto();
            notif.setUserId(patientOpt.get().getUserId());
            notif.setMessage("Your appointment is booked successfully. No: " + newAppointmentNo);
            notificationService.sendNotification(notif);
        }

        return mapToDto(savedAppointment);
    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> all = appointmentRepo.findAll();
        List<AppointmentDto> dtos = new ArrayList<>();
        all.forEach(a -> dtos.add(mapToDto(a)));
        return dtos;
    }

    @Override
    public AppointmentDto getAppointmentById(Long id) {
        return appointmentRepo.findById(id)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Override
    public void deleteAppointment(Long id) {
        if (appointmentRepo.existsById(id)) {
            appointmentRepo.deleteById(id);
        }
    }

    @Override
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel a completed appointment.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepo.save(appointment);

        // Notify Patient
        Optional<Patient> patientOpt = patientRepo.findById(appointment.getPatientId());
        if (patientOpt.isPresent() && patientOpt.get().getUserId() != null) {
            NotificationDto notif = new NotificationDto();
            notif.setUserId(patientOpt.get().getUserId());
            notif.setMessage("Your appointment #" + appointment.getAppointmentNo() + " has been cancelled.");
            notificationService.sendNotification(notif);
        }
    }

    @Override
    public void completeAppointment(Long id) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Cannot complete a cancelled appointment.");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepo.save(appointment);

        // Audit Log
        AuditLogDto auditLog = new AuditLogDto();
        // Assuming the patient is the user related to the appointment, or potentially the doctor.
        // Since we don't have authentication context, we'll log the patientId as usage reference if applicable,
        // or just leave userId null if strictly mapping to User table id which might not work for Patient.
        // But for "User" field in log, I will try to supply the patientId if possible contextually,
        // however AuditLog.userId is numeric. If Patient is not User, this is problematic.
        // I will default to null to be safe and avoid FK constraint violations.
        auditLog.setUserId(null);
        auditLog.setAction("CONSULTATION_COMPLETED");
        auditLog.setEntity("Appointment");
        auditLog.setEntityId(id);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogService.createLog(auditLog);
    }

    @Override
    public AppointmentDto updateAppointment(Long id, AppointmentDto dto) {
        Optional<Appointment> existingOpt = appointmentRepo.findById(id);
        if (existingOpt.isPresent()) {
            Appointment existing = existingOpt.get();

            // If the schedule ID changed, we need to fetch the new schedule
            if (dto.getScheduleId() != null) {
                DoctorSchedule newSchedule = scheduleRepo.findById(dto.getScheduleId())
                        .orElseThrow(() -> new RuntimeException("Schedule not found"));
                existing.setDoctorSchedule(newSchedule);
            }

            existing.setAppointmentTime(dto.getAppointmentTime());
            // Update other fields as needed...

            Appointment saved = appointmentRepo.save(existing);
            return mapToDto(saved);
        }
        return null;
    }

    // Helper method to Map Entity -> DTO and extract the ID
    private AppointmentDto mapToDto(Appointment appointment) {
        AppointmentDto dto = mapper.convertValue(appointment, AppointmentDto.class);
        if (appointment.getDoctorSchedule() != null) {
            dto.setScheduleId(appointment.getDoctorSchedule().getId());
        }
        return dto;
    }
}
