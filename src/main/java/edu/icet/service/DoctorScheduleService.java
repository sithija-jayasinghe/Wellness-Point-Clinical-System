package edu.icet.service;

import edu.icet.dto.DoctorScheduleDto;
import java.util.List;

public interface DoctorScheduleService {
    void addSchedule(DoctorScheduleDto doctorScheduleDto);
    List<DoctorScheduleDto> getAllSchedules();
    DoctorScheduleDto getScheduleById(Long id);
    void deleteSchedule(Long id);
    void updateSchedule(Long id, DoctorScheduleDto doctorScheduleDto);
    boolean isSlotAvailable(Long scheduleId, Long patientId);
}
