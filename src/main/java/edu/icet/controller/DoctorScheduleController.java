package edu.icet.controller;

import edu.icet.dto.DoctorScheduleDto;
import edu.icet.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService service;

    @PostMapping("/add")
    public void addSchedule(@RequestBody DoctorScheduleDto scheduleDto) {
        service.addSchedule(scheduleDto);
    }

    @GetMapping("/get-all")
    public List<DoctorScheduleDto> getAllSchedules() {
        return service.getAllSchedules();
    }

    @GetMapping("/{id}")
    public DoctorScheduleDto getScheduleById(@PathVariable Long id) {
        return service.getScheduleById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        service.deleteSchedule(id);
    }

    @PutMapping("/update/{id}")
    public void updateSchedule(@PathVariable Long id, @RequestBody DoctorScheduleDto scheduleDto) {
        service.updateSchedule(id, scheduleDto);
    }
}
