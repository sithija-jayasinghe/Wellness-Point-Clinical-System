package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.UserRegistrationDto;
import edu.icet.entity.*;
import edu.icet.repository.*;
import edu.icet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public void registerUser(UserRegistrationDto dto) {
        User user = mapper.convertValue(dto, User.class);
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

    @Override
    public Boolean validateUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(Long id, UserRegistrationDto dto) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).get();

            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());

            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                user.setPassword(dto.getPassword());
            }

            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}