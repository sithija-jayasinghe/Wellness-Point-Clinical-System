package edu.icet.service;

import edu.icet.dto.PrescriptionDto;
import java.util.List;

public interface PrescriptionService {
    PrescriptionDto savePrescription(PrescriptionDto prescriptionDto);
    List<PrescriptionDto> getAllPrescriptions();
    PrescriptionDto getPrescriptionById(Long id);
    void updatePrescription(Long id, PrescriptionDto prescriptionDto);
    void deletePrescription(Long id);
}
