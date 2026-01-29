package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.entity.Clinic;
import edu.icet.repository.ClinicRepository;
import edu.icet.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;
    private final ObjectMapper mapper;

    @Override
    public void addClinic(Clinic clinic) {
        clinicRepository.save(clinic);
    }

    @Override
    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }

    @Override
    public void updateClinic(Long id, Clinic clinicDetails) {
        if (clinicRepository.existsById(id)) {
            Clinic clinic = mapper.convertValue(clinicDetails, Clinic.class);
            clinic.setId(id);
            clinicRepository.save(clinic);
        }
    }

    @Override
    public void deleteClinic(Long id) {
        clinicRepository.deleteById(id);
    }
}