package edu.icet.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MedicalHistoryDto {
    private Long appointmentId;
    private LocalDateTime appointmentDate;
    private String status;

    private String doctorName;
    private String specialization;

    // Consultation details
    private Long consultationId;
    private String diagnosis;
    private String notes;

    // Prescription details
    private Long prescriptionId;
    private List<PrescriptionItemDto> medications;
}
