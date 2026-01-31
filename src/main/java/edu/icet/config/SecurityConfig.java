package edu.icet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {
        private static final String ROLE_ADMIN = "ADMIN";
        private static final String ROLE_STAFF = "STAFF";
        private static final String ROLE_DOCTOR = "DOCTOR";
        private static final String ROLE_PATIENT = "PATIENT";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/users/login", "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/clinics/**", "/api/doctors/**").permitAll()

                        // Secured endpoints
                        .requestMatchers("/api/users/**").hasRole(ROLE_ADMIN)
                        .requestMatchers("/api/doctors/register").hasAnyRole(ROLE_ADMIN, ROLE_STAFF)
                        .requestMatchers("/schedule/add", "/schedule/update/**", "/schedule/delete/**").hasAnyRole(ROLE_ADMIN, ROLE_DOCTOR)
                        .requestMatchers("/appointment/**").hasAnyRole(ROLE_ADMIN, ROLE_STAFF, ROLE_DOCTOR, ROLE_PATIENT)

                        .anyRequest().authenticated()
                );
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}