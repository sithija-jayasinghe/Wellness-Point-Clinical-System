package edu.icet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDto {
    private Long prescriptionId;
    private Long consultationId;
    private LocalDate issuedDate;
    private List<PrescriptionItemDto> prescriptionItems;
}
