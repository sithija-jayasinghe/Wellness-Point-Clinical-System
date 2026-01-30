package edu.icet.dto;

import edu.icet.entity.User;
import lombok.Data;

@Data
public class DoctorDto {
    private Long doctorId;
    private User userId;
    private String name;
    private String specialization;
    private double consultationFee;
    private String status;
}
