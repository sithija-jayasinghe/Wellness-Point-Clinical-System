package edu.icet.controller;

import edu.icet.dto.ConsultationDto;
import edu.icet.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@CrossOrigin
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping
    public ResponseEntity<ConsultationDto> createConsultation(@RequestBody ConsultationDto consultationDto) {
        return ResponseEntity.ok(consultationService.saveConsultation(consultationDto));
    }

    @GetMapping
    public List<ConsultationDto> getAllConsultations() {
        return consultationService.getAllConsultations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDto> getConsultationById(@PathVariable Long id) {
        ConsultationDto consultation = consultationService.getConsultationById(id);
        return (consultation != null) ? ResponseEntity.ok(consultation) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public void updateConsultation(@PathVariable Long id, @RequestBody ConsultationDto consultationDto) {
        consultationService.updateConsultation(id, consultationDto);
    }

    @DeleteMapping("/{id}")
    public void deleteConsultation(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
    }
}
