package edu.icet.service;

import edu.icet.entity.User;
import edu.icet.entity.UserRole;
import edu.icet.repository.UserRepository;
import edu.icet.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        UserRole userRole = userRoleRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Role not found for user: " + username));

        String roleName = userRole.getRole().getName();

        // Ensure role format matches what SecurityConfig expects (defaults usually need ROLE_ prefix unless configured otherwise)
        // User request: set authorities as "ROLE_" + roleName
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
