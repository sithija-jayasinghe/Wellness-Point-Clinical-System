package edu.icet.controller;

import edu.icet.dto.RefundDto;
import edu.icet.entity.Payment;
import edu.icet.entity.Refund;
import edu.icet.service.RefundService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;
    private final ModelMapper modelMapper;

    @Autowired
    public RefundController(RefundService refundService, ModelMapper modelMapper) {
        this.refundService = refundService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public RefundDto addRefund(@RequestBody RefundDto refundDto) {
        Refund refund = modelMapper.map(refundDto, Refund.class);

        if (refundDto.getPaymentId() != null) {
            Payment payment = new Payment();
            payment.setPaymentId(refundDto.getPaymentId());
            refund.setPayment(payment);
        }

        Refund savedRefund = refundService.saveRefund(refund);
        RefundDto savedDto = modelMapper.map(savedRefund, RefundDto.class);

        if (savedRefund.getPayment() != null) {
            savedDto.setPaymentId(savedRefund.getPayment().getPaymentId());
        }

        return savedDto;
    }

    @GetMapping("/all")
    public List<RefundDto> getAllRefunds() {
        return refundService.getAllRefunds().stream()
                .map(refund -> {
                    RefundDto dto = modelMapper.map(refund, RefundDto.class);
                    if (refund.getPayment() != null) {
                        dto.setPaymentId(refund.getPayment().getPaymentId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RefundDto getRefundById(@PathVariable Long id) {
        Refund refund = refundService.getRefundById(id);
        if (refund == null) return null;

        RefundDto dto = modelMapper.map(refund, RefundDto.class);
        if (refund.getPayment() != null) {
            dto.setPaymentId(refund.getPayment().getPaymentId());
        }
        return dto;
    }

    @DeleteMapping("/{id}")
    public void deleteRefund(@PathVariable Long id) {
        refundService.deleteRefund(id);
    }
}
