package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.AppointmentDto;
import edu.icet.dto.NotificationDto;
import edu.icet.entity.Appointment;
import edu.icet.entity.DoctorSchedule;
import edu.icet.entity.Patient;
import edu.icet.exception.BookingFullException;
import edu.icet.exception.InvalidOperationException;
import edu.icet.exception.ResourceNotFoundException;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.DoctorScheduleRepository;
import edu.icet.repository.PatientRepository;
import edu.icet.service.AuditLogService;
import edu.icet.service.NotificationService;
import edu.icet.util.AppointmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepo;
    @Mock
    private DoctorScheduleRepository scheduleRepo;
    @Mock
    private PatientRepository patientRepo;
    @Mock
    private NotificationService notificationService;
    @Mock
    private AuditLogService auditLogService;
    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private DoctorSchedule schedule;
    private AppointmentDto appointmentDto;
    private Appointment appointmentEntity;
    private Patient patient;

    @BeforeEach
    void setUp() {
        schedule = new DoctorSchedule();
        schedule.setId(1L);
        schedule.setStartDateTime(LocalDateTime.now().minusHours(1));
        schedule.setEndDateTime(LocalDateTime.now().plusHours(5));
        schedule.setMaxPatients(10);

        appointmentDto = new AppointmentDto();
        appointmentDto.setScheduleId(1L);
        appointmentDto.setPatientId(100L);
        appointmentDto.setAppointmentTime(LocalDateTime.now().plusHours(1));

        appointmentEntity = new Appointment();
        appointmentEntity.setId(1L);
        appointmentEntity.setAppointmentNo(1);
        appointmentEntity.setStatus(AppointmentStatus.BOOKED);

        patient = new Patient();
        patient.setId(100L);
        patient.setUserId(50L);
    }

    @Test
    void testBookAppointment_Success() {
        when(scheduleRepo.findById(1L)).thenReturn(Optional.of(schedule));
        when(appointmentRepo.existsByPatientIdAndAppointmentTimeAndStatusNotAndDeletedFalse(any(), any(), any())).thenReturn(false);
        when(appointmentRepo.existsByDoctorScheduleDoctorIdAndAppointmentTimeAndStatusNotAndDeletedFalse(any(), any(), any())).thenReturn(false);
        when(appointmentRepo.countByDoctorScheduleIdAndDeletedFalse(1L)).thenReturn(5L);
        when(appointmentRepo.findMaxAppointmentNo(1L)).thenReturn(0);
        when(mapper.convertValue(appointmentDto, Appointment.class)).thenReturn(appointmentEntity);
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(appointmentEntity);
        when(mapper.convertValue(appointmentEntity, AppointmentDto.class)).thenReturn(appointmentDto);
        when(patientRepo.findById(100L)).thenReturn(Optional.of(patient));

        AppointmentDto result = appointmentService.bookAppointment(appointmentDto);

        assertNotNull(result);
        verify(appointmentRepo, times(1)).save(any(Appointment.class));
        verify(notificationService, times(1)).sendNotification(any(NotificationDto.class));
    }

    @Test
    void testBookAppointment_ScheduleNotFound() {
        when(scheduleRepo.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.bookAppointment(appointmentDto);
        });

        assertEquals("Schedule not found", exception.getMessage());
    }

    @Test
    void testBookAppointment_BookingFull() {
        when(scheduleRepo.findById(1L)).thenReturn(Optional.of(schedule));
        when(appointmentRepo.existsByPatientIdAndAppointmentTimeAndStatusNotAndDeletedFalse(any(), any(), any())).thenReturn(false);
        when(appointmentRepo.existsByDoctorScheduleDoctorIdAndAppointmentTimeAndStatusNotAndDeletedFalse(any(), any(), any())).thenReturn(false);
        when(appointmentRepo.countByDoctorScheduleIdAndDeletedFalse(1L)).thenReturn(10L); // Max patients reached

        Exception exception = assertThrows(BookingFullException.class, () -> {
            appointmentService.bookAppointment(appointmentDto);
        });

        assertEquals("Booking full for this schedule", exception.getMessage());
    }

    @Test
    void testBookAppointment_TimeOutsideSchedule() {
        when(scheduleRepo.findById(1L)).thenReturn(Optional.of(schedule));
        appointmentDto.setAppointmentTime(LocalDateTime.now().plusHours(10)); // Outside schedule

        Exception exception = assertThrows(InvalidOperationException.class, () -> {
            appointmentService.bookAppointment(appointmentDto);
        });

        assertEquals("Appointment time falls outside the doctor's schedule", exception.getMessage());
    }

    @Test
    void testCancelAppointment_Success() {
        appointmentEntity.setStatus(AppointmentStatus.BOOKED);
        appointmentEntity.setPatientId(100L);
        appointmentEntity.setAppointmentNo(1);

        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointmentEntity));
        when(patientRepo.findById(100L)).thenReturn(Optional.of(patient));

        appointmentService.cancelAppointment(1L);

        assertEquals(AppointmentStatus.CANCELLED, appointmentEntity.getStatus());
        verify(appointmentRepo, times(1)).save(appointmentEntity);
        verify(notificationService, times(1)).sendNotification(any(NotificationDto.class));
    }

    @Test
    void testCompleteAppointment_Success() {
        appointmentEntity.setStatus(AppointmentStatus.BOOKED);

        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointmentEntity));

        appointmentService.completeAppointment(1L);

        assertEquals(AppointmentStatus.COMPLETED, appointmentEntity.getStatus());
        verify(appointmentRepo, times(1)).save(appointmentEntity);
        verify(auditLogService, times(1)).createLog(any());
    }
}
