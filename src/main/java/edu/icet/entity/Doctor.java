package edu.icet.entity;

import edu.icet.util.DoctorStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;
    private String name;
    private String specialization;
    private double consultationFee;

    @Enumerated(EnumType.STRING)
    private DoctorStatus status;
}
