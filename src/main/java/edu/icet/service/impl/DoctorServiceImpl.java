package edu.icet.service.impl;

import edu.icet.entity.Doctor;
import edu.icet.repository.DoctorRepository;
import edu.icet.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepo;

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepo.save(doctor);
    }
}

