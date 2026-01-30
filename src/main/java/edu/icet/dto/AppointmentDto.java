package edu.icet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentDto {
    private Long id;
    private Long scheduleId;
    private Long patientId;
    private DoctorDto doctor;
    private LocalDateTime appointmentTime;
    private Integer appointmentNo; // This will be set by the backend
    private String status;
}
