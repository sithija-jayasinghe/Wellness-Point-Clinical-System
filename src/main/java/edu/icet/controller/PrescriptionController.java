package edu.icet.controller;

import edu.icet.dto.PrescriptionDto;
import edu.icet.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@CrossOrigin
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<PrescriptionDto> createPrescription(@RequestBody PrescriptionDto prescriptionDto) {
        return ResponseEntity.ok(prescriptionService.savePrescription(prescriptionDto));
    }

    @GetMapping
    public List<PrescriptionDto> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDto> getPrescriptionById(@PathVariable Long id) {
        PrescriptionDto prescription = prescriptionService.getPrescriptionById(id);
        return (prescription != null) ? ResponseEntity.ok(prescription) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public void updatePrescription(@PathVariable Long id, @RequestBody PrescriptionDto prescriptionDto) {
        prescriptionService.updatePrescription(id, prescriptionDto);
    }

    @DeleteMapping("/{id}")
    public void deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
    }
}
