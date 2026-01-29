package edu.icet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private Long id;
    private Long scheduleId;
    private Long patientId;
    private LocalDateTime appointmentTime;
    private Integer appointmentNo; // This will be set by the backend
}
