package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.ConsultationDto;
import edu.icet.entity.Appointment;
import edu.icet.entity.Consultation;
import edu.icet.exception.InvalidOperationException;
import edu.icet.exception.ResourceNotFoundException;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.ConsultationRepository;
import edu.icet.service.ConsultationService;
import edu.icet.util.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepo;
    private final AppointmentRepository appointmentRepo;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public ConsultationDto saveConsultation(ConsultationDto consultationDto) {

        if (consultationDto.getAppointmentId() == null) {
            throw new IllegalArgumentException("Appointment ID is required for consultation");
        }

        Appointment appointment = appointmentRepo.findById(consultationDto.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Rule: Consultation allowed only for BOOKED appointments
        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new InvalidOperationException("Consultation allowed only for BOOKED appointments");
        }

        Consultation consultation = mapper.convertValue(consultationDto, Consultation.class);
        consultation.setAppointment(appointment);

        Consultation saved = consultationRepo.save(consultation);

        // Auto-Complete: After consultation is saved -> update appointment status to COMPLETED
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepo.save(appointment);

        return mapper.convertValue(saved, ConsultationDto.class);
    }


    @Override
    public List<ConsultationDto> getAllConsultations() {
        List<Consultation> list = consultationRepo.findAll();
        List<ConsultationDto> dtoList = new ArrayList<>();
        list.forEach(entity -> {
            ConsultationDto dto = mapper.convertValue(entity, ConsultationDto.class);
            if (entity.getAppointment() != null) {
                dto.setAppointmentId(entity.getAppointment().getId());
            }
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Override
    public ConsultationDto getConsultationById(Long id) {
        Optional<Consultation> byId = consultationRepo.findById(id);
        return byId.map(entity -> {
            ConsultationDto dto = mapper.convertValue(entity, ConsultationDto.class);
            if (entity.getAppointment() != null) {
                dto.setAppointmentId(entity.getAppointment().getId());
            }
            return dto;
        }).orElse(null);
    }

    @Override
    public void updateConsultation(Long id, ConsultationDto consultationDto) {
        if (consultationRepo.existsById(id)) {
            Consultation consultation = mapper.convertValue(consultationDto, Consultation.class);
            consultation.setConsultationId(id);
             if (consultationDto.getAppointmentId() != null) {
                Appointment appointment = appointmentRepo.findById(consultationDto.getAppointmentId())
                        .orElse(null);
                consultation.setAppointment(appointment);
            }
            consultationRepo.save(consultation);
        }
    }

    @Override
    public void deleteConsultation(Long id) {
        consultationRepo.deleteById(id);
    }
}
