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
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxPatients;
    // Add doctorId if you plan to link it later
}

