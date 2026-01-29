package edu.icet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class DoctorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use LocalDateTime or LocalTime depending on your requirement
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxPatients;

    // You might also want to link this to a doctor later
    // private Long doctorId;
}
