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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepo;
    private final UserClinicRepository userClinicRepo;
    private final edu.icet.repository.ClinicRepository clinicRepo;
    private final ObjectMapper mapper;

    @Override
    public void addDoctor(DoctorDto doctorDto) {
        Doctor doctor = mapper.convertValue(doctorDto, Doctor.class);
        doctorRepo.save(doctor);
    }

    @Override
    public List<DoctorDto> getAllDoctors() {
        List<Doctor> list = doctorRepo.findAll();
        List<DoctorDto> dtoList = new ArrayList<>();
        list.forEach(entity -> {
            DoctorDto dto = mapper.convertValue(entity, DoctorDto.class);
            if (entity.getUser() != null) {
                List<UserClinic> userClinics = userClinicRepo.findByUser(entity.getUser());
                List<Clinic> clinics = userClinics.stream()
                        .map(UserClinic::getClinic)
                        .collect(Collectors.toList());
                dto.setClinics(clinics);
            }
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Override
    public DoctorDto getDoctorById(Long id) {
        Optional<Doctor> byId = doctorRepo.findById(id);
        return byId.map(entity -> {
            DoctorDto dto = mapper.convertValue(entity, DoctorDto.class);
            if (entity.getUser() != null) {
                List<UserClinic> userClinics = userClinicRepo.findByUser(entity.getUser());
                List<Clinic> clinics = userClinics.stream()
                        .map(UserClinic::getClinic)
                        .collect(Collectors.toList());
                dto.setClinics(clinics);
            }
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
                // Fetch and delete existing mappings explicitly
                List<UserClinic> existingMappings = userClinicRepo.findByUser(doctor.getUser());
                userClinicRepo.deleteAll(existingMappings);

                // Add new mappings
                for (Clinic clinicDto : doctorDto.getClinics()) {
                    if (clinicDto != null && clinicDto.getId() != null) {
                        Optional<Clinic> clinicOpt = clinicRepo.findById(clinicDto.getId());
                        if (clinicOpt.isPresent()) {
                            UserClinic userClinic = new UserClinic();
                            userClinic.setUser(doctor.getUser());
                            userClinic.setClinic(clinicOpt.get());
                            userClinicRepo.save(userClinic);
                        }
                    }
                }
            }
            doctorRepo.save(doctor);
        }
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepo.deleteById(id);
    }
}
