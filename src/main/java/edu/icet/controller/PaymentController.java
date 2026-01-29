package edu.icet.controller;

import edu.icet.dto.PaymentDto;
import edu.icet.entity.Payment;
import edu.icet.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final ModelMapper modelMapper;

    public PaymentController(PaymentService paymentService, ModelMapper modelMapper) {
        this.paymentService = paymentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public PaymentDto addPayment(@RequestBody PaymentDto paymentDto) {
        Payment payment = modelMapper.map(paymentDto, Payment.class);
        Payment savedPayment = paymentService.savePayment(payment);
        return modelMapper.map(savedPayment, PaymentDto.class);
    }

    @GetMapping("/all")
    public List<PaymentDto> getAllPayments() {
        return paymentService.getAllPayments().stream()
                .map(payment -> modelMapper.map(payment, PaymentDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PaymentDto getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        if (payment == null) return null;
        return modelMapper.map(payment, PaymentDto.class);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}
