package edu.icet.controller;

import edu.icet.entity.Clinic;
import edu.icet.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
@CrossOrigin
public class ClinicController {

    @Autowired
    private final ClinicRepository clinicRepository;

    @PostMapping("/add")
    public void addClinic(@RequestBody Clinic clinic) {
        clinicRepository.save(clinic);
    }

    @GetMapping("/get-all")
    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public void updateClinic(@PathVariable Long id, @RequestBody Clinic clinicDetails) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        clinic.setName(clinicDetails.getName());
        clinic.setAddress(clinicDetails.getAddress());
        clinic.setPhone(clinicDetails.getPhone());
        clinic.setStatus(clinicDetails.getStatus());

        clinicRepository.save(clinic);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClinic(@PathVariable Long id) {
        clinicRepository.deleteById(id);
    }
}