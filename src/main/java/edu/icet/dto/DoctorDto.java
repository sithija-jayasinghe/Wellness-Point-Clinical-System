package edu.icet.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.icet.entity.User;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorDto {
    @JsonAlias("id")
    private Long doctorId;
    @JsonAlias("user")
    private User userId;
    private String name;
    private String specialization;
    private double consultationFee;
    private String status;
}
