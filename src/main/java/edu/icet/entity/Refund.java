package edu.icet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refund")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private Long refundId;

    private Double amount;

    @Column(name = "refund_date")
    private LocalDate refundDate;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
