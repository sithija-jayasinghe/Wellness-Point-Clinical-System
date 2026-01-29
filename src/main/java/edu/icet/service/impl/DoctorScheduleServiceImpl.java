package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.DoctorScheduleDto;
import edu.icet.dto.NotificationDto;
import edu.icet.entity.DoctorSchedule;
import edu.icet.entity.Staff;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepo;
    private final AppointmentRepository appointmentRepo;
    private final StaffRepository staffRepo; // Inject StaffRepo
    private final NotificationService notificationService; // Inject NotificationService
    private final ObjectMapper mapper;

    @Override
    public void addSchedule(DoctorScheduleDto doctorScheduleDto) {
        // Validate logic
        if (!doctorScheduleDto.getStartDateTime().isBefore(doctorScheduleDto.getEndDateTime())) {
            throw new RuntimeException("Start time must be before end time");
        }
        if (doctorScheduleDto.getMaxPatients() <= 0) {
            throw new RuntimeException("Max patients must be greater than 0");
        }

        DoctorSchedule schedule = mapper.convertValue(doctorScheduleDto, DoctorSchedule.class);
        scheduleRepo.save(schedule);

        // Notify Staff about new schedule
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
        Optional<DoctorSchedule> byId = scheduleRepo.findById(id);
        return byId.map(doctorSchedule -> mapper.convertValue(doctorSchedule, DoctorScheduleDto.class)).orElse(null);
    }

    @Override
    public void deleteSchedule(Long id) {
        if (scheduleRepo.existsById(id)) {
            scheduleRepo.deleteById(id);
        }
    }

    @Override
    public void updateSchedule(Long id, DoctorScheduleDto doctorScheduleDto) {
        if (scheduleRepo.findById(id).isPresent()) {
            DoctorSchedule entity = mapper.convertValue(doctorScheduleDto, DoctorSchedule.class);
            entity.setId(id);
            scheduleRepo.save(entity);

            // Notify Staff about schedule update
            notifyAllStaff("Schedule Updated for Doctor ID: " + entity.getDoctorId());
        }
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
        Optional<DoctorSchedule> scheduleOpt = scheduleRepo.findById(scheduleId);

        if (scheduleOpt.isEmpty()) {
            throw new RuntimeException("Schedule not found");
        }

        DoctorSchedule schedule = scheduleOpt.get();

        // 1. Check strict time availability
        LocalDateTime now = LocalDateTime.now();
        // Since we added endDateTime to the Entity, this will now resolve
        if (schedule.getEndDateTime() != null && schedule.getEndDateTime().isBefore(now)) {
            throw new RuntimeException("This schedule has expired.");
        }

        // 2. Check max patients limit
        long currentBookings = appointmentRepo.countByDoctorScheduleId(scheduleId);
        if (currentBookings >= schedule.getMaxPatients()) {
            throw new RuntimeException("Doctor is fully booked for this slot.");
        }

        // 3. Prevent double booking for same patient
        boolean alreadyBooked = appointmentRepo.existsByDoctorScheduleIdAndPatientId(scheduleId, patientId);
        if (alreadyBooked) {
            throw new RuntimeException("Patient already has an appointment for this schedule.");
        }

        return true;
    }
}
