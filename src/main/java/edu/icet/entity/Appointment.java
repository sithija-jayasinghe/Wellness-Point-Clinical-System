package edu.icet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RELATIONSHIP CONNECTED HERE
    @ManyToOne
    @JoinColumn(name = "schedule_id") // This creates the Foreign Key column in the DB
    private DoctorSchedule doctorSchedule;

    private Long patientId; // You can do the same for Patient if you have a Patient entity

    private LocalDateTime appointmentTime;
    private Integer appointmentNo;
}
