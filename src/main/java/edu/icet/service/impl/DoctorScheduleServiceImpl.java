package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.DoctorScheduleDto;
import edu.icet.entity.DoctorSchedule;
import edu.icet.repository.DoctorScheduleRepository;
import edu.icet.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepo;
    private final ObjectMapper mapper;

    @Override
    public void addSchedule(DoctorScheduleDto doctorScheduleDto) {
        // (Keep your existing validation logic here)
        DoctorSchedule schedule = mapper.convertValue(doctorScheduleDto, DoctorSchedule.class);
        scheduleRepo.save(schedule);
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
        if (byId.isPresent()) {
            return mapper.convertValue(byId.get(), DoctorScheduleDto.class);
        }
        return null;
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
            entity.setId(id); // Ensure the ID remains the same for update
            scheduleRepo.save(entity);
        }
    }
}
