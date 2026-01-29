package edu.icet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionItemDto {
    private Long itemId;
    private Long prescriptionId;
    private String medicineName;
    private String dosage;
    private String duration;
}
