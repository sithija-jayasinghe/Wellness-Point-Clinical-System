package edu.icet.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDto {
    private Long id;
    private String name;
    private String nic;
    private String phone;
    private LocalDate dob;
    private String gender;
}
