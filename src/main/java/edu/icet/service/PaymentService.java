package edu.icet.service;

import edu.icet.dto.PaymentDto;
import java.util.List;

public interface PaymentService {
    void addPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllPayments();
    PaymentDto getPaymentById(Long id);
    void updatePayment(Long id, PaymentDto paymentDto);
    void deletePayment(Long id);
}
