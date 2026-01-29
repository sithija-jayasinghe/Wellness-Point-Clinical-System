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
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Maps to audit_id

    private Long userId; // Maps to user_id (Actor)

    private String action; // Action performed (e.g., "CREATED", "DELETED")

    private String entity; // Affected entity (e.g., "Appointment", "Patient")

    private Long entityId; // Entity identifier

    private LocalDateTime timestamp;
}
