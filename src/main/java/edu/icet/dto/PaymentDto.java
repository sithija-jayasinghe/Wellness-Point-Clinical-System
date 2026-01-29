package edu.icet.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class PaymentDto {
    private Long paymentId;
    private Long appointmentId;
    private Double amount;
    private String paymentMethod;
    private Date paymentDate;
    private String status;
    private List<RefundDto> refunds;
}
