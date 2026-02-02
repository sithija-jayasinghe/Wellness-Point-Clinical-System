package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.LoginResponseDto;
import edu.icet.dto.UserRegistrationDto;
import edu.icet.entity.*;
import edu.icet.exception.ResourceAlreadyExistsException;
import edu.icet.repository.*;
import edu.icet.service.JwtService;
import edu.icet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
    public LoginResponseDto login(String username, String password) {
        // Authenticate using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Fetch User and Role
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserRole userRole = userRoleRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Role not found for user"));

        String roleName = userRole.getRole().getName();

        // Generate Token
        // The authentication.getPrincipal() is the UserDetails object returned by CustomUserDetailsService
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails, roleName);

        // Build Response
        return LoginResponseDto.builder()
                .token(token)
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(roleName)
                .build();
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