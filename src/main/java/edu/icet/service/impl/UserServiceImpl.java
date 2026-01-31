package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.UserRegistrationDto;
import edu.icet.entity.*;
import edu.icet.exception.ResourceAlreadyExistsException;
import edu.icet.repository.*;
import edu.icet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClinicRepository clinicRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserClinicRepository userClinicRepository;
    private final ObjectMapper mapper;
    private final PasswordEncoder passwordEncoder; // Security එක Inject

    @Override
    @Transactional
    public void registerUser(UserRegistrationDto dto) {

        // --- Validation Logic ---
        if (Boolean.TRUE.equals(userRepository.existsByEmail(dto.getEmail()))) {
            throw new ResourceAlreadyExistsException("Email already exists!");
        }
        if (Boolean.TRUE.equals(userRepository.existsByUsername(dto.getUsername()))) {
            throw new ResourceAlreadyExistsException("Username already exists!");
        }

        // DTO -> Entity Convert
        User user = mapper.convertValue(dto, User.class);

        // --- Security Logic: Password Encrypt  ---
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setStatus("ACTIVE");
        User savedUser = userRepository.save(user);

        // Role Assign
        Role role = roleRepository.findByName(dto.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

        // Clinic Assign
        Clinic clinic = clinicRepository.findById(dto.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        UserClinic userClinic = new UserClinic();
        userClinic.setUser(savedUser);
        userClinic.setClinic(clinic);
        userClinicRepository.save(userClinic);
    }

    @Override
    public Boolean validateUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(Long id, UserRegistrationDto dto) {
        userRepository.findById(id).ifPresent(user -> {
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());

            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            userRepository.save(user);
        });
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}