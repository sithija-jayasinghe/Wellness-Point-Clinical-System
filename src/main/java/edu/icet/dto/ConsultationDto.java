package edu.icet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationDto {
    private Long consultationId;
    private Long appointmentId;
    private String diagnosis;
    private String notes;
}
