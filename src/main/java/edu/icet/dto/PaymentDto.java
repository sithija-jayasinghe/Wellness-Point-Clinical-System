package edu.icet.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaymentDto {
    private Long paymentId;
    private Double amount;
    private LocalDate paymentDate;
    private Long appointmentId;
    private String paymentMethod;
    private String status;
}
