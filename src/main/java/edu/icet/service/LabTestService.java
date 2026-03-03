package edu.icet.service;

import edu.icet.dto.LabTestDto;
import edu.icet.util.LabTestStatus;

import java.util.List;

public interface LabTestService {
    LabTestDto createLabTest(LabTestDto labTestDto);
    LabTestDto getLabTestById(Long id);
    List<LabTestDto> getAllLabTests();
    List<LabTestDto> getLabTestsByPatientId(Long patientId);
    List<LabTestDto> getLabTestsByLabOperatorId(Long labOperatorId);
    List<LabTestDto> getLabTestsByStatus(LabTestStatus status);
    List<LabTestDto> getLabTestsByDoctorId(Long doctorId);
    LabTestDto updateLabTestResult(Long id, String result, String notes);
    LabTestDto updateLabTestStatus(Long id, LabTestStatus status);
    void deleteLabTest(Long id);
}
