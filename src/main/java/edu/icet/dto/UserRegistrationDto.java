package edu.icet.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String username;
    private String email;
    private String password;
    private String role;
    private Long clinicId;
}