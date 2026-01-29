package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.PrescriptionDto;
import edu.icet.entity.Consultation;
import edu.icet.entity.Prescription;
import edu.icet.entity.PrescriptionItem;
import edu.icet.repository.ConsultationRepository;
import edu.icet.repository.PrescriptionRepository;
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
    private final ObjectMapper mapper;

    @Override
    public PrescriptionDto savePrescription(PrescriptionDto prescriptionDto) {
        // Validate that prescription items are not empty
        if (prescriptionDto.getPrescriptionItems() == null || prescriptionDto.getPrescriptionItems().isEmpty()) {
            throw new RuntimeException("Prescription must contain at least one item.");
        }

        Prescription prescription = mapper.convertValue(prescriptionDto, Prescription.class);

        if (prescriptionDto.getConsultationId() != null) {
            Consultation consultation = consultationRepo.findById(prescriptionDto.getConsultationId())
                    .orElseThrow(() -> new RuntimeException("Consultation not found"));
            prescription.setConsultation(consultation);
        }

        if (prescription.getPrescriptionItems() != null) {
            for (PrescriptionItem item : prescription.getPrescriptionItems()) {
                item.setPrescription(prescription);
            }
        }

        Prescription saved = prescriptionRepo.save(prescription);
        return mapper.convertValue(saved, PrescriptionDto.class);
    }

    @Override
    public List<PrescriptionDto> getAllPrescriptions() {
        List<Prescription> list = prescriptionRepo.findAll();
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
        return byId.map(entity -> {
            PrescriptionDto dto = mapper.convertValue(entity, PrescriptionDto.class);
            if (entity.getConsultation() != null) {
                dto.setConsultationId(entity.getConsultation().getConsultationId());
            }
            return dto;
        }).orElse(null);
    }

    @Override
    public void updatePrescription(Long id, PrescriptionDto prescriptionDto) {
        if (prescriptionRepo.existsById(id)) {
            Prescription prescription = mapper.convertValue(prescriptionDto, Prescription.class);
            prescription.setPrescriptionId(id);

            if (prescriptionDto.getConsultationId() != null) {
                Consultation consultation = consultationRepo.findById(prescriptionDto.getConsultationId())
                        .orElse(null);
                prescription.setConsultation(consultation);
            }

            if (prescription.getPrescriptionItems() != null) {
                for (PrescriptionItem item : prescription.getPrescriptionItems()) {
                    item.setPrescription(prescription);
                }
            }

            prescriptionRepo.save(prescription);
        }
    }

    @Override
    public void deletePrescription(Long id) {
        prescriptionRepo.deleteById(id);
    }
}
