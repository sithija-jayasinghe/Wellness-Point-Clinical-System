package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.RefundDto;
import edu.icet.entity.Appointment;
import edu.icet.entity.Payment;
import edu.icet.entity.Refund;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.PaymentRepository;
import edu.icet.repository.RefundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefundServiceImplTest {

    @Mock
    private RefundRepository refundRepo;

    @Mock
    private PaymentRepository paymentRepo;

    @Mock
    private AppointmentRepository appointmentRepo;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private RefundServiceImpl refundService;

    private RefundDto refundDto;
    private Refund refund;
    private Payment payment;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setPaymentId(1L);
        payment.setAppointmentId(1L);

        refundDto = new RefundDto();
        refundDto.setPayment(payment);

        refund = new Refund();
        refund.setPayment(payment);

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus("CANCELLED");
    }

    @Test
    void addRefund_Success() {
        when(mapper.convertValue(refundDto, Refund.class)).thenReturn(refund);
        when(paymentRepo.findById(1L)).thenReturn(Optional.of(payment));
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));

        refundService.addRefund(refundDto);

        verify(refundRepo, times(1)).save(refund);
    }

    @Test
    void addRefund_NotCancelled_ThrowsException() {
        appointment.setStatus("COMPLETED");

        when(mapper.convertValue(refundDto, Refund.class)).thenReturn(refund);
        when(paymentRepo.findById(1L)).thenReturn(Optional.of(payment));
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));

        assertThrows(RuntimeException.class, () -> refundService.addRefund(refundDto));

        verify(refundRepo, never()).save(any(Refund.class));
    }
}
