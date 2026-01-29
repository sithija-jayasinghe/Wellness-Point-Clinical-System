package edu.icet.service.impl;

import edu.icet.dto.UserRegistrationDto;
import edu.icet.entity.*;
import edu.icet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}