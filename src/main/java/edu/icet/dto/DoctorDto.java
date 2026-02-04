package edu.icet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.icet.entity.Clinic;
import edu.icet.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorDto {
    // 1. Renamed 'doctorId' to 'id' to match Doctor Entity
    private Long id;

    // 2. Renamed 'userId' to 'user' to match Doctor Entity
    private User user;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @Min(value = 0, message = "Consultation fee cannot be negative")
    private double consultationFee;

    private String status;

    private java.util.List<Clinic> clinics;
}