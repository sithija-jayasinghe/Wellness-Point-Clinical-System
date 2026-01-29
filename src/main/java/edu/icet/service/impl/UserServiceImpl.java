package edu.icet.service.impl;

import edu.icet.dto.UserRegistrationDto;
import edu.icet.entity.*;
import edu.icet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClinicRepository clinicRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserClinicRepository userClinicRepository;

    public void registerUser(UserRegistrationDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setStatus("ACTIVE");
        User savedUser = userRepository.save(user);

        Role role = roleRepository.findByName(dto.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

        Clinic clinic = clinicRepository.findById(dto.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        UserClinic userClinic = new UserClinic();
        userClinic.setUser(savedUser);
        userClinic.setClinic(clinic);
        userClinicRepository.save(userClinic);
    }

    public Boolean validateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, UserRegistrationDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}