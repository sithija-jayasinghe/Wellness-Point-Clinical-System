package edu.icet.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleDto {
    private Long id;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private Long clinicId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startDateTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endDateTime;

    @Min(value = 1, message = "Max patients must be at least 1")
    private Integer maxPatients;
}
