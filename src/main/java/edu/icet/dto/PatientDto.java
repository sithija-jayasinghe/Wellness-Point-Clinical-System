package edu.icet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.icet.entity.Clinic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "NIC is required")
    private String nic;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Past(message = "Date of Birth must be in the past")
    private LocalDate dob;

    private String gender;
    private Long userId;

    private List<Clinic> clinics;

    private List<Long> clinicIds;
}