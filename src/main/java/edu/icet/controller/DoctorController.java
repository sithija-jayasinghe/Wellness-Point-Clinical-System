package edu.icet.controller;

import edu.icet.dto.DoctorDto;
import edu.icet.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping("/register")
    public void addDoctor(@Valid @RequestBody DoctorDto doctorDto) {
        doctorService.addDoctor(doctorDto);
    }

    @GetMapping("/get-all")
    public List<DoctorDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public DoctorDto getDoctor(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @PutMapping("/update/{id}")
    public void updateDoctor(@PathVariable Long id,
                             @Valid @RequestBody DoctorDto doctorDto) {
        doctorService.updateDoctor(id, doctorDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}
