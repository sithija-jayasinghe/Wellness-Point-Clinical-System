package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.DoctorDto;
import edu.icet.entity.Clinic;
import edu.icet.entity.Doctor;
import edu.icet.entity.UserClinic;
import edu.icet.repository.DoctorRepository;
import edu.icet.repository.UserClinicRepository;
import edu.icet.service.DoctorService;
import edu.icet.util.DoctorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepo;
    private final UserClinicRepository userClinicRepo;
    private final edu.icet.repository.ClinicRepository clinicRepo;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public void addDoctor(DoctorDto doctorDto) {
        // 1. Save the Doctor entity
        Doctor doctor = mapper.convertValue(doctorDto, Doctor.class);
        doctorRepo.save(doctor);

        // 2. Handle Clinic Mappings (UserClinic)
        // Check if we have a User link and a list of clinics from the frontend
        if (doctor.getUser() != null && doctorDto.getClinics() != null) {
            updateUserClinics(doctor, doctorDto.getClinics());
        }
    }

    @Override
    public List<DoctorDto> getAllDoctors() {
        List<Doctor> list = doctorRepo.findAll();
        List<DoctorDto> dtoList = new ArrayList<>();

        list.forEach(entity -> {
            DoctorDto dto = mapper.convertValue(entity, DoctorDto.class);
            dto.setClinics(getClinicsByUser(entity));
            dtoList.add(dto);
        });

        return dtoList;
    }

    @Override
    public DoctorDto getDoctorById(Long id) {
        Optional<Doctor> byId = doctorRepo.findById(id);

        return byId.map(entity -> {
            DoctorDto dto = mapper.convertValue(entity, DoctorDto.class);
            dto.setClinics(getClinicsByUser(entity));
            return dto;
        }).orElse(null);
    }

    @Override
    @Transactional
    public void updateDoctor(Long id, DoctorDto doctorDto) {
        Optional<Doctor> byId = doctorRepo.findById(id);

        if (byId.isPresent()) {
            Doctor doctor = byId.get();
            doctor.setName(doctorDto.getName());
            doctor.setSpecialization(doctorDto.getSpecialization());
            doctor.setConsultationFee(doctorDto.getConsultationFee());

            if (doctorDto.getStatus() != null) {
                doctor.setStatus(DoctorStatus.valueOf(doctorDto.getStatus()));
            }

            if (doctor.getUser() != null && doctorDto.getClinics() != null) {
                updateUserClinics(doctor, doctorDto.getClinics());
            }

            doctorRepo.save(doctor);
        }
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepo.deleteById(id);
    }

    private List<Clinic> getClinicsByUser(Doctor doctor) {
        if (doctor.getUser() == null) {
            return List.of();
        }
        return userClinicRepo.findByUser(doctor.getUser())
                .stream()
                .map(UserClinic::getClinic)
                .toList(); // replaces collect(Collectors.toList())
    }

    private void updateUserClinics(Doctor doctor, List<Clinic> clinics) {
        List<UserClinic> existingMappings = userClinicRepo.findByUser(doctor.getUser());
        userClinicRepo.deleteAll(existingMappings);

        // Add all clinics from the list
        for (Clinic clinicDto : clinics) {
            if (clinicDto != null && clinicDto.getId() != null) {
                clinicRepo.findById(clinicDto.getId()).ifPresent(clinic -> {
                    UserClinic userClinic = new UserClinic();
                    userClinic.setUser(doctor.getUser());
                    userClinic.setClinic(clinic);
                    userClinicRepo.save(userClinic);
                });
            }
        }
    }
}
