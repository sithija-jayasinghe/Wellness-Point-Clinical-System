package edu.icet.entity;

import edu.icet.util.LabTestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "lab_test")
@AllArgsConstructor
@NoArgsConstructor
public class LabTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "lab_operator_id")
    private User labOperator;

    @Column(nullable = false)
    private String testName;

    private String testCode;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String result;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabTestStatus status;

    private LocalDateTime requestedDate;

    private LocalDateTime completedDate;

    private String notes;
}
