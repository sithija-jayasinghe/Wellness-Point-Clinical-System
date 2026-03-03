package edu.icet.controller;

import edu.icet.dto.LabTestDto;
import edu.icet.service.LabTestService;
import edu.icet.util.LabTestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-tests")
@RequiredArgsConstructor
@CrossOrigin
public class LabTestController {

    private final LabTestService labTestService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_OPERATOR', 'DOCTOR')")
    public ResponseEntity<LabTestDto> createLabTest(@RequestBody LabTestDto labTestDto) {
        LabTestDto created = labTestService.createLabTest(labTestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_OPERATOR', 'DOCTOR')")
    public ResponseEntity<LabTestDto> getLabTestById(@PathVariable Long id) {
        return ResponseEntity.ok(labTestService.getLabTestById(id));
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_OPERATOR')")
    public ResponseEntity<List<LabTestDto>> getAllLabTests() {
        return ResponseEntity.ok(labTestService.getAllLabTests());
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_OPERATOR', 'DOCTOR')")
    public ResponseEntity<List<LabTestDto>> getLabTestsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(labTestService.getLabTestsByPatientId(patientId));
    }

    @GetMapping("/operator/{labOperatorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_OPERATOR')")
    public ResponseEntity<List<LabTestDto>> getLabTestsByOperator(@PathVariable Long labOperatorId) {
        return ResponseEntity.ok(labTestService.getLabTestsByLabOperatorId(labOperatorId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_OPERATOR')")
    public ResponseEntity<List<LabTestDto>> getLabTestsByStatus(@PathVariable LabTestStatus status) {
        return ResponseEntity.ok(labTestService.getLabTestsByStatus(status));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_OPERATOR', 'DOCTOR')")
    public ResponseEntity<List<LabTestDto>> getLabTestsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(labTestService.getLabTestsByDoctorId(doctorId));
    }

    @PutMapping("/update-result/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_OPERATOR')")
    public ResponseEntity<LabTestDto> updateLabTestResult(
            @PathVariable Long id,
            @RequestParam String result,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(labTestService.updateLabTestResult(id, result, notes));
    }

    @PutMapping("/update-status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_OPERATOR')")
    public ResponseEntity<LabTestDto> updateLabTestStatus(
            @PathVariable Long id,
            @RequestParam LabTestStatus status) {
        return ResponseEntity.ok(labTestService.updateLabTestStatus(id, status));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLabTest(@PathVariable Long id) {
        labTestService.deleteLabTest(id);
        return ResponseEntity.noContent().build();
    }
}
