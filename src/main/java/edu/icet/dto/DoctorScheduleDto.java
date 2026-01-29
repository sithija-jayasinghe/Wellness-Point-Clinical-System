package edu.icet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleDto {
    private Long id;
    private Long doctorId;
    private Long clinicId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Integer maxPatients;
}

