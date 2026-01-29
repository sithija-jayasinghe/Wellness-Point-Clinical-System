package edu.icet.controller;

import edu.icet.entity.Clinic;
import edu.icet.service.ClinicService;
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
    private final ClinicService clinicService;

    @PostMapping("/add")
    public void addClinic(@RequestBody Clinic clinic) {
        clinicService.addClinic(clinic);
    }

    @GetMapping("/get-all")
    public List<Clinic> getAllClinics() {
        return clinicService.getAllClinics();
    }

    @PutMapping("/update/{id}")
    public void updateClinic(@PathVariable Long id, @RequestBody Clinic clinic) {
        clinicService.updateClinic(id, clinic);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClinic(@PathVariable Long id) {
        clinicService.deleteClinic(id);
    }
}