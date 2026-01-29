package edu.icet.dto;

import lombok.Data;

@Data
public class PatientDto {
    private Long patientId;
    private String name;
    private String nic;
    private String phone;
    private String gender;
}
