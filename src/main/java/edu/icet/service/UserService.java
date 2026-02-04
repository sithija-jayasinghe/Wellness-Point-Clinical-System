package edu.icet.service;

import edu.icet.dto.LoginResponseDto;
import edu.icet.dto.UserRegistrationDto;
import edu.icet.dto.UserResponseDto;
import java.util.List;

public interface UserService {
    void registerUser(UserRegistrationDto dto);
    LoginResponseDto login(String username, String password);
    List<UserResponseDto> getAllUsers();
    void updateUser(Long id, UserRegistrationDto dto);
    void deleteUser(Long id);
}