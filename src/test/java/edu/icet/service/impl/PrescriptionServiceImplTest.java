package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.PrescriptionDto;
import edu.icet.dto.PrescriptionItemDto;
import edu.icet.entity.Consultation;
import edu.icet.entity.Prescription;
import edu.icet.repository.ConsultationRepository;
import edu.icet.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceImplTest {

    @Mock
    private PrescriptionRepository prescriptionRepo;

    @Mock
    private ConsultationRepository consultationRepo;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    private PrescriptionDto prescriptionDto;
    private Prescription prescription;
    private Consultation consultation;

    @BeforeEach
    void setUp() {
        prescriptionDto = new PrescriptionDto();
        prescriptionDto.setConsultationId(1L);

        consultation = new Consultation();
        consultation.setConsultationId(1L);

        prescription = new Prescription();
        prescription.setConsultation(consultation);
    }

    @Test
    void savePrescription_NoItems_ThrowsException() {
        prescriptionDto.setPrescriptionItems(null);

        assertThrows(RuntimeException.class, () -> prescriptionService.savePrescription(prescriptionDto));
        verify(prescriptionRepo, never()).save(any(Prescription.class));
    }

    @Test
    void savePrescription_EmptyItems_ThrowsException() {
        prescriptionDto.setPrescriptionItems(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> prescriptionService.savePrescription(prescriptionDto));
        verify(prescriptionRepo, never()).save(any(Prescription.class));
    }

    @Test
    void savePrescription_WithItems_Success() {
        PrescriptionItemDto itemDto = new PrescriptionItemDto();
        itemDto.setMedicineName("Paracetamol");
        prescriptionDto.setPrescriptionItems(List.of(itemDto));

        when(mapper.convertValue(prescriptionDto, Prescription.class)).thenReturn(prescription);
        when(consultationRepo.findById(1L)).thenReturn(Optional.of(consultation));
        when(prescriptionRepo.save(any(Prescription.class))).thenReturn(prescription);
        when(mapper.convertValue(prescription, PrescriptionDto.class)).thenReturn(prescriptionDto);

        prescriptionService.savePrescription(prescriptionDto);

        verify(prescriptionRepo, times(1)).save(prescription);
    }
}
