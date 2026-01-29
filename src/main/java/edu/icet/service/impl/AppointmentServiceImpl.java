package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.AppointmentDto;
import edu.icet.entity.Appointment;
import edu.icet.entity.DoctorSchedule;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.DoctorScheduleRepository;
import edu.icet.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final DoctorScheduleRepository scheduleRepo;
    private final ObjectMapper mapper;

    @Override
    public AppointmentDto bookAppointment(AppointmentDto dto) {
        // 1. Retrieve the Schedule
        DoctorSchedule schedule = scheduleRepo.findById(dto.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // Validation logic
        if (dto.getAppointmentTime().isBefore(schedule.getStartTime()) ||
                dto.getAppointmentTime().isAfter(schedule.getEndTime())) {
            throw new RuntimeException("Appointment time falls outside the doctor's schedule.");
        }

        // Count logic (Note: countByScheduleId needs to update to countByDoctorScheduleId if method name is sensitive to field name)
        Integer currentCount = appointmentRepo.countByDoctorScheduleId(dto.getScheduleId());
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

        Appointment savedAppointment = appointmentRepo.save(appointment);

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
