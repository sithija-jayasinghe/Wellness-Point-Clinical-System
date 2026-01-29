package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.DoctorDto;
import edu.icet.entity.Doctor;
import edu.icet.repository.DoctorRepository;
import edu.icet.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepo;
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
        list.forEach(entity -> dtoList.add(mapper.convertValue(entity, DoctorDto.class)));
        return dtoList;
    }

    @Override
    public DoctorDto getDoctorById(Long id) {
        Optional<Doctor> byId = doctorRepo.findById(id);
        return byId.map(entity -> mapper.convertValue(entity, DoctorDto.class)).orElse(null);
    }

    @Override
    public void updateDoctor(Long id, DoctorDto doctorDto) {
        if (doctorRepo.existsById(id)) {
            Doctor doctor = mapper.convertValue(doctorDto, Doctor.class);
            doctor.setId(id);
            doctorRepo.save(doctor);
        }
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepo.deleteById(id);
    }
}
