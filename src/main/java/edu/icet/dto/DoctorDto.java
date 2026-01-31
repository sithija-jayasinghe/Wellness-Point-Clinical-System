package edu.icet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.icet.entity.User;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorDto {
    // 1. Renamed 'doctorId' to 'id' to match Doctor Entity
    private Long id;

    // 2. Renamed 'userId' to 'user' to match Doctor Entity
    private User user;

    private String name;
    private String specialization;
    private double consultationFee;
    private String status;
}