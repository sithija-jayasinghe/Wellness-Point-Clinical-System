package edu.icet.service;

import edu.icet.dto.ConsultationDto;
import java.util.List;

public interface ConsultationService {
    ConsultationDto saveConsultation(ConsultationDto consultationDto);
    List<ConsultationDto> getAllConsultations();
    ConsultationDto getConsultationById(Long id);
    void updateConsultation(Long id, ConsultationDto consultationDto);
    void deleteConsultation(Long id);
}
