package edu.icet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationDto {
    private String username;
    private String email;
    private String password;
    private String role;
    private String status;
    private Long clinicId;
}