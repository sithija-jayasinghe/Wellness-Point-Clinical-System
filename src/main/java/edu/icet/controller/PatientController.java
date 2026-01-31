package edu.icet.controller;

import edu.icet.dto.PatientDto;
import edu.icet.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService patientService;

    @PostMapping("/register")
    public void addPatient(@Valid @RequestBody PatientDto patientDto) {
        patientService.addPatient(patientDto);
    }

    @GetMapping("/get-all")
    public List<PatientDto> getAllPatient() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public PatientDto getPatient(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/update/{id}")
    public void updatePatient(@PathVariable Long id,
                             @Valid @RequestBody PatientDto patientDto) {
        patientService.updatePatient(id, patientDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}
