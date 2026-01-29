package edu.icet.controller;

import edu.icet.dto.AppointmentDto;
import edu.icet.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping("/book")
    public AppointmentDto bookAppointment(@RequestBody AppointmentDto appointmentDto) {
        return service.bookAppointment(appointmentDto);
    }

    @GetMapping("/get-all")
    public List<AppointmentDto> getAllAppointments() {
        return service.getAllAppointments();
    }

    @GetMapping("/{id}")
    public AppointmentDto getAppointmentById(@PathVariable Long id) {
        return service.getAppointmentById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        service.deleteAppointment(id);
    }

    @PutMapping("/update/{id}")
    public AppointmentDto updateAppointment(@PathVariable Long id, @RequestBody AppointmentDto appointmentDto) {
        return service.updateAppointment(id, appointmentDto);
    }
}
