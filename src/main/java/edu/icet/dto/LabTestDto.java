package edu.icet.dto;

import edu.icet.util.LabTestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabTestDto {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long labOperatorId;
    private String testName;
    private String testCode;
    private String description;
    private String result;
    private LabTestStatus status;
    private LocalDateTime requestedDate;
    private LocalDateTime completedDate;
    private String notes;
}
