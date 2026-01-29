package edu.icet.controller;

import edu.icet.entity.Clinic;
import edu.icet.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
@CrossOrigin
public class ClinicController {

    private final ClinicRepository clinicRepository;

    @PostMapping
    public Clinic createClinic(@RequestBody Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    @GetMapping
    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }
}