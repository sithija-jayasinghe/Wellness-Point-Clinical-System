package edu.icet.dto;

import edu.icet.entity.Payment;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RefundDto {
    private Long refundId;
    private Double amount;
    private LocalDate refundDate;
    private String reason;
    private Payment payment;
}
