package edu.icet.service.impl;

import edu.icet.dto.AuditLogDto;
import edu.icet.dto.LabTestDto;
import edu.icet.entity.Doctor;
import edu.icet.entity.LabTest;
import edu.icet.entity.Patient;
import edu.icet.entity.User;
import edu.icet.repository.DoctorRepository;
import edu.icet.repository.LabTestRepository;
import edu.icet.repository.PatientRepository;
import edu.icet.repository.UserRepository;
import edu.icet.service.AuditLogService;
import edu.icet.service.LabTestService;
import edu.icet.util.LabTestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository labTestRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    @Override
    public LabTestDto createLabTest(LabTestDto dto) {
        LabTest labTest = mapToEntity(dto);
        labTest.setStatus(LabTestStatus.REQUESTED);
        labTest.setRequestedDate(LocalDateTime.now());
        LabTest saved = labTestRepository.save(labTest);

        AuditLogDto auditLog = new AuditLogDto();
        auditLog.setUserId(dto.getLabOperatorId());
        auditLog.setAction("LAB_TEST_CREATED");
        auditLog.setEntity("LabTest");
        auditLog.setEntityId(saved.getId());
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogService.createLog(auditLog);

        return mapToDto(saved);
    }

    @Override
    public LabTestDto getLabTestById(Long id) {
        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found"));
        return mapToDto(labTest);
    }

    @Override
    public List<LabTestDto> getAllLabTests() {
        return labTestRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LabTestDto> getLabTestsByPatientId(Long patientId) {
        return labTestRepository.findByPatient_Id(patientId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LabTestDto> getLabTestsByLabOperatorId(Long labOperatorId) {
        return labTestRepository.findByLabOperator_UserId(labOperatorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LabTestDto> getLabTestsByStatus(LabTestStatus status) {
        return labTestRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LabTestDto> getLabTestsByDoctorId(Long doctorId) {
        return labTestRepository.findByDoctor_Id(doctorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabTestDto updateLabTestResult(Long id, String result, String notes) {
        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found"));

        labTest.setResult(result);
        labTest.setNotes(notes);
        labTest.setStatus(LabTestStatus.COMPLETED);
        labTest.setCompletedDate(LocalDateTime.now());

        LabTest saved = labTestRepository.save(labTest);

        AuditLogDto auditLog = new AuditLogDto();
        auditLog.setUserId(labTest.getLabOperator() != null ? labTest.getLabOperator().getUserId() : null);
        auditLog.setAction("LAB_TEST_RESULT_UPDATED");
        auditLog.setEntity("LabTest");
        auditLog.setEntityId(saved.getId());
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogService.createLog(auditLog);

        return mapToDto(saved);
    }

    @Override
    public LabTestDto updateLabTestStatus(Long id, LabTestStatus status) {
        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found"));

        labTest.setStatus(status);
        if (status == LabTestStatus.COMPLETED) {
            labTest.setCompletedDate(LocalDateTime.now());
        }

        LabTest saved = labTestRepository.save(labTest);

        AuditLogDto auditLog = new AuditLogDto();
        auditLog.setUserId(labTest.getLabOperator() != null ? labTest.getLabOperator().getUserId() : null);
        auditLog.setAction("LAB_TEST_STATUS_UPDATED");
        auditLog.setEntity("LabTest");
        auditLog.setEntityId(saved.getId());
        auditLog.setTimestamp(LocalDateTime.now());
        auditLogService.createLog(auditLog);

        return mapToDto(saved);
    }

    @Override
    public void deleteLabTest(Long id) {
        if (!labTestRepository.existsById(id)) {
            throw new RuntimeException("Lab test not found");
        }
        labTestRepository.deleteById(id);
    }

    private LabTestDto mapToDto(LabTest labTest) {
        LabTestDto dto = new LabTestDto();
        dto.setId(labTest.getId());
        dto.setPatientId(labTest.getPatient() != null ? labTest.getPatient().getId() : null);
        dto.setDoctorId(labTest.getDoctor() != null ? labTest.getDoctor().getId() : null);
        dto.setLabOperatorId(labTest.getLabOperator() != null ? labTest.getLabOperator().getUserId() : null);
        dto.setTestName(labTest.getTestName());
        dto.setTestCode(labTest.getTestCode());
        dto.setDescription(labTest.getDescription());
        dto.setResult(labTest.getResult());
        dto.setStatus(labTest.getStatus());
        dto.setRequestedDate(labTest.getRequestedDate());
        dto.setCompletedDate(labTest.getCompletedDate());
        dto.setNotes(labTest.getNotes());
        return dto;
    }

    private LabTest mapToEntity(LabTestDto dto) {
        LabTest labTest = new LabTest();

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        labTest.setPatient(patient);

        if (dto.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            labTest.setDoctor(doctor);
        }

        if (dto.getLabOperatorId() != null) {
            User labOperator = userRepository.findById(dto.getLabOperatorId())
                    .orElseThrow(() -> new RuntimeException("Lab operator not found"));
            labTest.setLabOperator(labOperator);
        }

        labTest.setTestName(dto.getTestName());
        labTest.setTestCode(dto.getTestCode());
        labTest.setDescription(dto.getDescription());
        labTest.setResult(dto.getResult());
        labTest.setNotes(dto.getNotes());
        return labTest;
    }
}
