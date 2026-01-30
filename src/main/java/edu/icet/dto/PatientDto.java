package edu.icet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDto {
    private Long id;
    private Long userId;
    private String name;
    private String nic;
    private String phone;
    private LocalDate dob;
    private String gender;
}
