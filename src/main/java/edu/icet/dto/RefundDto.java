package edu.icet.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RefundDto {
    private Long refundId;
    private Long paymentId;
    private Double amount;
    private String reason;
    private LocalDate refundDate;
}
