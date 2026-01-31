package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.PatientDto;
import edu.icet.entity.Patient;
import edu.icet.repository.PatientRepository;
import edu.icet.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepo;
    private final ObjectMapper mapper;

    @Override
    public void addPatient(PatientDto patientDto) {
        Patient patient = mapper.convertValue(patientDto, Patient.class);
        patientRepo.save(patient);
    }

    @Override
    public List<PatientDto> getAllPatients() {
        List<Patient> list = patientRepo.findAll();
        List<PatientDto> dtoList = new ArrayList<>();
        list.forEach(entity -> dtoList.add(mapper.convertValue(entity, PatientDto.class)));
        return dtoList;
    }

    @Override
    public PatientDto getPatientById(Long id) {
        Optional<Patient> byId = patientRepo.findById(id);
        return byId.map(entity -> mapper.convertValue(entity, PatientDto.class)).orElse(null);
    }

    @Override
    public void updatePatient(Long id, PatientDto patientDto) {
        Optional<Patient> existingOpt = patientRepo.findById(id);
        if (existingOpt.isPresent()) {
            Patient existing = existingOpt.get();
            Patient updatedInfo = mapper.convertValue(patientDto, Patient.class);

            // Preserve userId if not provided in DTO
            if (updatedInfo.getUserId() == null) {
                updatedInfo.setUserId(existing.getUserId());
            }

            updatedInfo.setId(id);
            patientRepo.save(updatedInfo);
        }
    }

    @Override
    public void deletePatient(Long id) {
        patientRepo.deleteById(id);
    }
}
