package edu.icet.entity;

import edu.icet.util.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private Long patientId;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private DoctorSchedule doctorSchedule;

    private LocalDateTime appointmentTime;
    private Integer appointmentNo;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted = false;
}
