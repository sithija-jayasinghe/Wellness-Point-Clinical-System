package edu.icet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Maps to staff_id

    // Foreign Key linking to a User account
    private Long userId;

    // Role (e.g., Nurse, Receptionist)
    private String designation;

    // ACTIVE / INACTIVE
    private String status;
}
