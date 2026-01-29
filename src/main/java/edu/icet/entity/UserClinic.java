package edu.icet.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_clinic")
public class UserClinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userClinicId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
}