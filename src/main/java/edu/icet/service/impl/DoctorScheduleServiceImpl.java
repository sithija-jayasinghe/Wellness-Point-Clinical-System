package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.DoctorScheduleDto;
import edu.icet.dto.NotificationDto;
import edu.icet.entity.DoctorSchedule;
import edu.icet.entity.Staff;
import edu.icet.exception.BookingFullException;
import edu.icet.exception.InvalidOperationException;
import edu.icet.exception.ResourceNotFoundException;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.DoctorScheduleRepository;
import edu.icet.repository.StaffRepository;
import edu.icet.service.DoctorScheduleService;
import edu.icet.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private static final String SCHEDULE_NOT_FOUND = "Schedule not found";

    private final DoctorScheduleRepository scheduleRepo;
    private final AppointmentRepository appointmentRepo;
    private final StaffRepository staffRepo;
    private final NotificationService notificationService;
    private final ObjectMapper mapper;

    @Override
    public void addSchedule(DoctorScheduleDto doctorScheduleDto) {

        if (doctorScheduleDto.getStartDateTime() == null || doctorScheduleDto.getEndDateTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required");
        }

        // Validate logic
        if (!doctorScheduleDto.getStartDateTime().isBefore(doctorScheduleDto.getEndDateTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (doctorScheduleDto.getMaxPatients() == null || doctorScheduleDto.getMaxPatients() <= 0) {
            throw new IllegalArgumentException("Max patients must be greater than 0");
        }

        if (doctorScheduleDto.getDoctorId() == null) {
            throw new IllegalArgumentException("Doctor ID is required");
        }

        // Check for overlapping schedules
        boolean overlap = scheduleRepo.existsOverlappingSchedule(
                doctorScheduleDto.getDoctorId(),
                doctorScheduleDto.getStartDateTime(),
                doctorScheduleDto.getEndDateTime()
        );

        if (overlap) {
            throw new InvalidOperationException("Doctor already has a schedule that overlaps with this time range");
        }

        DoctorSchedule schedule = mapper.convertValue(doctorScheduleDto, DoctorSchedule.class);
        scheduleRepo.save(schedule);

        // Notify Staff about the new schedule
        notifyAllStaff("New schedule created for Doctor ID: " + schedule.getDoctorId());
    }

    @Override
    public List<DoctorScheduleDto> getAllSchedules() {
        List<DoctorSchedule> allList = scheduleRepo.findAll();
        List<DoctorScheduleDto> dtoList = new ArrayList<>();
        allList.forEach(entity -> dtoList.add(mapper.convertValue(entity, DoctorScheduleDto.class)));
        return dtoList;
    }

    @Override
    public DoctorScheduleDto getScheduleById(Long id) {
        DoctorSchedule schedule = scheduleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SCHEDULE_NOT_FOUND));

        return mapper.convertValue(schedule, DoctorScheduleDto.class);
    }

    @Override
    public void deleteSchedule(Long id) {
        if (!scheduleRepo.existsById(id)) {
            throw new ResourceNotFoundException(SCHEDULE_NOT_FOUND);
        }
        scheduleRepo.deleteById(id);
    }

    @Override
    public void updateSchedule(Long id, DoctorScheduleDto doctorScheduleDto) {
        // Ensure schedule exists
        DoctorSchedule existing = scheduleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SCHEDULE_NOT_FOUND));

        // Validate input (optional but recommended)
        if (doctorScheduleDto.getStartDateTime() != null
                && doctorScheduleDto.getEndDateTime() != null
                && !doctorScheduleDto.getStartDateTime().isBefore(doctorScheduleDto.getEndDateTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        DoctorSchedule entity = mapper.convertValue(doctorScheduleDto, DoctorSchedule.class);
        entity.setId(existing.getId()); // keep the same id
        scheduleRepo.save(entity);

        // Notify Staff about schedule update
        notifyAllStaff("Schedule Updated for Doctor ID: " + entity.getDoctorId());
    }

    private void notifyAllStaff(String message) {
        List<Staff> activeStaff = staffRepo.findByStatus("ACTIVE");
        for (Staff staff : activeStaff) {
            if (staff.getUserId() != null) {
                NotificationDto notif = new NotificationDto();
                notif.setUserId(staff.getUserId());
                notif.setMessage(message);
                notificationService.sendNotification(notif);
            }
        }
    }

    @Override
    public boolean isSlotAvailable(Long scheduleId, Long patientId) {

        if (scheduleId == null) {
            throw new IllegalArgumentException("Schedule ID is required");
        }
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }

        DoctorSchedule schedule = scheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(SCHEDULE_NOT_FOUND));

        // 1) Check schedule not expired
        LocalDateTime now = LocalDateTime.now();
        if (schedule.getEndDateTime() != null && schedule.getEndDateTime().isBefore(now)) {
            throw new InvalidOperationException("This schedule has expired");
        }

        // 2. Check max patients limit
        long currentBookings = appointmentRepo.countByDoctorScheduleIdAndDeletedFalse(scheduleId);
        if (currentBookings >= schedule.getMaxPatients()) {
            throw new BookingFullException("Doctor is fully booked for this slot.");
        }

        // 3. Prevent double booking for the same patient
        boolean alreadyBooked = appointmentRepo.existsByDoctorScheduleIdAndPatientIdAndDeletedFalse(scheduleId, patientId);
        if (alreadyBooked) {
            throw new InvalidOperationException("Patient already has an appointment for this schedule.");
        }

        return true;
    }
}
