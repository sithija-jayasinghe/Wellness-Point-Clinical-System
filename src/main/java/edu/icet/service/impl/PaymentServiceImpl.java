package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.PaymentDto;
import edu.icet.entity.Payment;
import edu.icet.repository.PaymentRepository;
import edu.icet.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final ObjectMapper mapper;

    @Override
    public void addPayment(PaymentDto paymentDto) {
        Payment payment = mapper.convertValue(paymentDto, Payment.class);
        paymentRepo.save(payment);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        List<Payment> list = paymentRepo.findAll();
        List<PaymentDto> dtoList = new ArrayList<>();
        list.forEach(entity -> dtoList.add(mapper.convertValue(entity, PaymentDto.class)));
        return dtoList;
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Optional<Payment> byId = paymentRepo.findById(id);
        return byId.map(entity -> mapper.convertValue(entity, PaymentDto.class)).orElse(null);
    }

    @Override
    public void updatePayment(Long id, PaymentDto paymentDto) {
        if (paymentRepo.existsById(id)) {
            Payment payment = mapper.convertValue(paymentDto, Payment.class);
            payment.setPaymentId(id);
            paymentRepo.save(payment);
        }
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepo.deleteById(id);
    }
}
