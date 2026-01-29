package edu.icet.service.impl;

import edu.icet.entity.Patient;
import edu.icet.repository.PatientRepository;
import edu.icet.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepo;

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepo.save(patient);
    }
    @Override
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }
}
