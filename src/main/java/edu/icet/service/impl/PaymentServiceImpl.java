package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.AuditLogDto;
import edu.icet.dto.PaymentDto;
import edu.icet.entity.Appointment;
import edu.icet.entity.Payment;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.PaymentRepository;
import edu.icet.service.AuditLogService;
import edu.icet.service.PaymentService;
import edu.icet.util.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final AppointmentRepository appointmentRepo;
    private final AuditLogService auditLogService;
    private final ObjectMapper mapper;

    @Override
    public void addPayment(PaymentDto paymentDto) {
        Payment payment = mapper.convertValue(paymentDto, Payment.class);

        // Validate payment amount against doctor's consultation fee
        if (payment.getAppointmentId() != null) {
            Appointment appointment = appointmentRepo.findById(payment.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));

            if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
                throw new RuntimeException("Cannot make payment for a CANCELLED appointment.");
            }

            if (appointment.getDoctor() != null) {
                if (payment.getAmount() != appointment.getDoctor().getConsultationFee()) {
                    throw new RuntimeException("Payment amount must match the doctor's consultation fee: " + appointment.getDoctor().getConsultationFee());
                }
            }
        }

        Payment savedPayment = paymentRepo.save(payment);

        // Audit Log
        AuditLogDto auditLog = new AuditLogDto();
        auditLog.setUserId(null); // System action or unknown user context
        auditLog.setAction("PAYMENT_MADE");
        auditLog.setEntity("Payment");
        auditLog.setEntityId(savedPayment.getPaymentId());
        auditLog.setTimestamp(LocalDateTime.now());

        auditLogService.createLog(auditLog);
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
