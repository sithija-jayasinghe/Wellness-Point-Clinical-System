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

    @PutMapping("/{id}")
    public Clinic updateClinic(@PathVariable Long id, @RequestBody Clinic clinicDetails) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        clinic.setName(clinicDetails.getName());
        clinic.setAddress(clinicDetails.getAddress());
        clinic.setPhone(clinicDetails.getPhone());
        clinic.setStatus(clinicDetails.getStatus());

        return clinicRepository.save(clinic);
    }

    @DeleteMapping("/{id}")
    public String deleteClinic(@PathVariable Long id) {
        clinicRepository.deleteById(id);
        return "Clinic Deleted Successfully!";
    }
}