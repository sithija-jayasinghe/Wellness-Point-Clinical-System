package edu.icet.controller;

import edu.icet.dto.PaymentDto;
import edu.icet.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/add")
    public void addPayment(@RequestBody PaymentDto paymentDto) {
        paymentService.addPayment(paymentDto);
    }

    @GetMapping("/get-all")
    public List<PaymentDto> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public PaymentDto getPayment(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PutMapping("/update/{id}")
    public void updatePayment(@PathVariable Long id, @RequestBody PaymentDto paymentDto) {
        paymentService.updatePayment(id, paymentDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}
