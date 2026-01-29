package edu.icet.dto;

import lombok.Data;

@Data
public class DoctorDto {
    private Long doctorId;
    private Long userId;
    private String doctorName;
    private String specialization;
    private double consultationFee;
}
