package edu.icet.service;

import edu.icet.dto.PatientDto;
import java.util.List;

public interface PatientService {
    void addPatient(PatientDto patientDto);
    List<PatientDto> getAllPatients();
    PatientDto getPatientById(Long id);
    void updatePatient(Long id, PatientDto patientDto);
    void deletePatient(Long id);
}
