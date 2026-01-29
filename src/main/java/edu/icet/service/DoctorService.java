package edu.icet.service;

import edu.icet.dto.DoctorDto;
import java.util.List;

public interface DoctorService {
    void addDoctor(DoctorDto doctorDto);
    List<DoctorDto> getAllDoctors();
    DoctorDto getDoctorById(Long id);
    void updateDoctor(Long id, DoctorDto doctorDto);
    void deleteDoctor(Long id);
}
