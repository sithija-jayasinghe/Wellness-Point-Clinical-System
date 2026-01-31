package edu.icet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Schedule ID is required")
    private Long scheduleId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private DoctorDto doctor;

    @NotNull(message = "Appointment time is required")
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    private Integer appointmentNo; // This will be set by the backend
    private String status;
}
