package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.PaymentDto;
import edu.icet.entity.Appointment;
import edu.icet.entity.Doctor;
import edu.icet.entity.Payment;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.PaymentRepository;
import edu.icet.service.AuditLogService;
import edu.icet.util.AppointmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepo;
    @Mock
    private AppointmentRepository appointmentRepo;
    @Mock
    private AuditLogService auditLogService;
    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentDto paymentDto;
    private Payment payment;
    private Appointment appointment;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        paymentDto = new PaymentDto();
        paymentDto.setAppointmentId(1L);
        paymentDto.setAmount(500.0);

        payment = new Payment();
        payment.setAppointmentId(1L);
        payment.setAmount(500.0);

        doctor = new Doctor();
        doctor.setConsultationFee(500.0);

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDoctor(doctor);
        appointment.setStatus(AppointmentStatus.BOOKED);
    }

    @Test
    void addPayment_Success() {
        when(mapper.convertValue(paymentDto, Payment.class)).thenReturn(payment);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));
        when(paymentRepo.save(payment)).thenReturn(payment);

        paymentService.addPayment(paymentDto);

        verify(paymentRepo, times(1)).save(payment);
        verify(auditLogService, times(1)).createLog(any());
    }

    @Test
    void addPayment_AmountMismatch_ThrowsException() {
        payment.setAmount(300.0); // Mismatch: 300 vs 500

        when(mapper.convertValue(paymentDto, Payment.class)).thenReturn(payment);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));

        assertThrows(RuntimeException.class, () -> paymentService.addPayment(paymentDto));

        verify(paymentRepo, never()).save(any(Payment.class));
    }

    @Test
    void addPayment_CancelledAppointment_ThrowsException() {
        appointment.setStatus(AppointmentStatus.CANCELLED);

        when(mapper.convertValue(paymentDto, Payment.class)).thenReturn(payment);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentService.addPayment(paymentDto);
        });

        assertEquals("Cannot make payment for a CANCELLED appointment.", exception.getMessage());
        verify(paymentRepo, never()).save(any(Payment.class));
    }
}
