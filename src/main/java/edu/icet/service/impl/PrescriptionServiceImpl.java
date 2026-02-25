package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.PrescriptionDto;
import edu.icet.entity.Consultation;
import edu.icet.entity.Patient;
import edu.icet.entity.Prescription;
import edu.icet.entity.PrescriptionItem;
import edu.icet.exception.InvalidOperationException;
import edu.icet.exception.ResourceAlreadyExistsException;
import edu.icet.exception.ResourceNotFoundException;
import edu.icet.repository.ConsultationRepository;
import edu.icet.repository.PatientRepository;
import edu.icet.repository.PrescriptionRepository;
import edu.icet.service.EmailService;
import edu.icet.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepo;
    private final ConsultationRepository consultationRepo;
    private final PatientRepository patientRepo;
    private final EmailService emailService;
    private final ObjectMapper mapper;

    @Override
    public PrescriptionDto savePrescription(PrescriptionDto prescriptionDto) {
        // Validate that prescription items are not empty
        if (prescriptionDto.getPrescriptionItems() == null || prescriptionDto.getPrescriptionItems().isEmpty()) {
            throw new InvalidOperationException("Prescription must contain at least one item.");
        }

        Prescription prescription = mapper.convertValue(prescriptionDto, Prescription.class);

        if (prescriptionDto.getConsultationId() != null) {
            // Check if a prescription already exists for this consultation
            if (prescriptionRepo.findByConsultationConsultationId(prescriptionDto.getConsultationId()).isPresent()) {
                throw new ResourceAlreadyExistsException("A prescription already exists for this consultation.");
            }

            Consultation consultation = consultationRepo.findById(prescriptionDto.getConsultationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with ID: " + prescriptionDto.getConsultationId()));
            prescription.setConsultation(consultation);
        }

        if (prescription.getPrescriptionItems() != null) {
            for (PrescriptionItem item : prescription.getPrescriptionItems()) {
                item.setPrescription(prescription);
            }
        }

        Prescription saved = prescriptionRepo.save(prescription);

        // Send prescription email to patient
        // Chain: Prescription -> Consultation -> Appointment -> patientId -> Patient
        try {
            if (saved.getConsultation() != null
                    && saved.getConsultation().getAppointment() != null
                    && saved.getConsultation().getAppointment().getPatientId() != null) {
                Long patientId = saved.getConsultation().getAppointment().getPatientId();
                Optional<Patient> patientOpt = patientRepo.findById(patientId);
                patientOpt.ifPresent(patient -> {
                    if (patient.getEmail() != null && !patient.getEmail().isBlank()) {
                        emailService.sendPrescriptionIssuedEmail(saved, patient.getEmail(), patient.getName());
                    }
                });
            }
        } catch (Exception e) {
            // Do not fail the save if email fails
        }

        return mapper.convertValue(saved, PrescriptionDto.class);
    }

    @Override
    public List<PrescriptionDto> getAllPrescriptions() {
        List<Prescription> list = prescriptionRepo.findByDeletedFalse();
        List<PrescriptionDto> dtoList = new ArrayList<>();
        list.forEach(entity -> {
            PrescriptionDto dto = mapper.convertValue(entity, PrescriptionDto.class);
            if (entity.getConsultation() != null) {
                dto.setConsultationId(entity.getConsultation().getConsultationId());
            }
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Override
    public PrescriptionDto getPrescriptionById(Long id) {
        Optional<Prescription> byId = prescriptionRepo.findById(id);
        return byId.filter(p -> !p.isDeleted())
                .map(entity -> {
            PrescriptionDto dto = mapper.convertValue(entity, PrescriptionDto.class);
            if (entity.getConsultation() != null) {
                dto.setConsultationId(entity.getConsultation().getConsultationId());
            }
            return dto;
        }).orElse(null);
    }

    @Override
    public void updatePrescription(Long id, PrescriptionDto prescriptionDto) {
        Prescription prescription = prescriptionRepo.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

        Prescription updated = mapper.convertValue(prescriptionDto, Prescription.class);
        updated.setPrescriptionId(id);
        updated.setDeleted(prescription.isDeleted()); // preserve deleted status

        if (prescriptionDto.getConsultationId() != null) {
                Consultation consultation = consultationRepo.findById(prescriptionDto.getConsultationId())
                        .orElse(null);
                updated.setConsultation(consultation);
        }

        if (updated.getPrescriptionItems() != null) {
             for (PrescriptionItem item : updated.getPrescriptionItems()) {
                 item.setPrescription(updated);
             }
        }

        prescriptionRepo.save(updated);
    }

    @Override
    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));
        prescription.setDeleted(true);
        prescriptionRepo.save(prescription);
    }
}
