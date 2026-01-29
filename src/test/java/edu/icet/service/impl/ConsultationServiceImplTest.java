package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.ConsultationDto;
import edu.icet.entity.Appointment;
import edu.icet.entity.Consultation;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.ConsultationRepository;
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
class ConsultationServiceImplTest {

    @Mock
    private ConsultationRepository consultationRepo;

    @Mock
    private AppointmentRepository appointmentRepo;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private ConsultationServiceImpl consultationService;

    private ConsultationDto consultationDto;
    private Consultation consultation;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        consultationDto = new ConsultationDto();
        consultationDto.setAppointmentId(1L);
        consultationDto.setDiagnosis("Flu");
        consultationDto.setNotes("Rest");

        consultation = new Consultation();
        consultation.setDiagnosis("Flu");
        consultation.setNotes("Rest");

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus("BOOKED");
    }

    @Test
    void saveConsultation_Success() {
        when(mapper.convertValue(consultationDto, Consultation.class)).thenReturn(consultation);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));
        when(consultationRepo.save(any(Consultation.class))).thenReturn(consultation);
        when(mapper.convertValue(consultation, ConsultationDto.class)).thenReturn(consultationDto);

        ConsultationDto result = consultationService.saveConsultation(consultationDto);
        assertEquals(consultationDto, result);

        // Verify consultation is saved
        verify(consultationRepo, times(1)).save(consultation);

        // Verify appointment status is updated to COMPLETED
        assertEquals("COMPLETED", appointment.getStatus());
        verify(appointmentRepo, times(1)).save(appointment);
    }

    @Test
    void saveConsultation_CancelledAppointment_ThrowsException() {
        appointment.setStatus("CANCELLED");

        when(mapper.convertValue(consultationDto, Consultation.class)).thenReturn(consultation);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));

        assertThrows(RuntimeException.class, () -> consultationService.saveConsultation(consultationDto));

        verify(consultationRepo, never()).save(any(Consultation.class));
        verify(appointmentRepo, never()).save(appointment);
    }

    @Test
    void saveConsultation_CompletedAppointment_ThrowsException() {
        appointment.setStatus("COMPLETED");

        when(mapper.convertValue(consultationDto, Consultation.class)).thenReturn(consultation);
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));

        assertThrows(RuntimeException.class, () -> consultationService.saveConsultation(consultationDto));

        verify(consultationRepo, never()).save(any(Consultation.class));
        verify(appointmentRepo, never()).save(appointment);
    }
}
