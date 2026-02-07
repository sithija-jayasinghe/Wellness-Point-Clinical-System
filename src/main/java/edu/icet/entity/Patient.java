package edu.icet.entity;

import edu.icet.util.Gender;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nic;
    private String phone;
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "patient_clinic",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_id")
    )
    private List<Clinic> clinics;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;
}