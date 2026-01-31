package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.MedicalHistoryDto;
import edu.icet.dto.PatientDto;
import edu.icet.dto.PrescriptionItemDto;
import edu.icet.entity.Appointment;
import edu.icet.entity.Patient;
import edu.icet.exception.ResourceAlreadyExistsException;
import edu.icet.exception.ResourceNotFoundException;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.ConsultationRepository;
import edu.icet.repository.PatientRepository;
import edu.icet.repository.PrescriptionRepository;
import edu.icet.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;     // Injected
    private final ConsultationRepository consultationRepo; // Injected
    private final PrescriptionRepository prescriptionRepo; // Injected
    private final ObjectMapper mapper;

    @Override
    public void addPatient(PatientDto patientDto) {
        if (patientRepo.findByNic(patientDto.getNic()).isPresent()) {
            throw new ResourceAlreadyExistsException("Patient with NIC " + patientDto.getNic() + " already exists.");
        }

        Patient patient = mapper.convertValue(patientDto, Patient.class);
        patientRepo.save(patient);
    }

    @Override
    public List<PatientDto> getAllPatients() {
        List<Patient> list = patientRepo.findByDeletedFalse();
        List<PatientDto> dtoList = new ArrayList<>();
        list.forEach(entity -> dtoList.add(mapper.convertValue(entity, PatientDto.class)));
        return dtoList;
    }

    @Override
    public PatientDto getPatientById(Long id) {
        Optional<Patient> byId = patientRepo.findById(id);
        return byId.filter(p -> !Boolean.TRUE.equals(p.getDeleted()))
                .map(entity -> mapper.convertValue(entity, PatientDto.class))
                .orElse(null);
    }

    @Override
    public void updatePatient(Long id, PatientDto patientDto) {
        Patient existing = patientRepo.findById(id)
                .filter(p -> !Boolean.TRUE.equals(p.getDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Patient updatedInfo = mapper.convertValue(patientDto, Patient.class);

        // Preserve userId if not provided in DTO
        if (updatedInfo.getUserId() == null) {
             updatedInfo.setUserId(existing.getUserId());
        }

        // Preserve deleted status
        updatedInfo.setDeleted(existing.getDeleted());

        updatedInfo.setId(id);
        patientRepo.save(updatedInfo);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        patient.setDeleted(true);
        patientRepo.save(patient);
    }

    @Override
    public List<MedicalHistoryDto> getMedicalHistory(Long patientId) {
        if (!patientRepo.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found");
        }

        List<Appointment> appointments = appointmentRepo.findByPatientIdAndDeletedFalseOrderByAppointmentTimeDesc(patientId);
        List<MedicalHistoryDto> history = new ArrayList<>();

        for (Appointment app : appointments) {
            MedicalHistoryDto dto = new MedicalHistoryDto();
            dto.setAppointmentId(app.getId());
            dto.setAppointmentDate(app.getAppointmentTime());
            dto.setStatus(app.getStatus().name());

            if (app.getDoctor() != null) {
                dto.setDoctorName(app.getDoctor().getName());
                dto.setSpecialization(app.getDoctor().getSpecialization());
            }

            // Fetch Consultation
            consultationRepo.findByAppointmentId(app.getId()).ifPresent(consultation -> {
                dto.setConsultationId(consultation.getConsultationId());
                dto.setDiagnosis(consultation.getDiagnosis());
                dto.setNotes(consultation.getNotes());

                // Fetch Prescription
                prescriptionRepo.findByConsultationConsultationId(consultation.getConsultationId()).ifPresent(prescription -> {
                    dto.setPrescriptionId(prescription.getPrescriptionId());
                    if (prescription.getPrescriptionItems() != null) {
                        List<PrescriptionItemDto> items = new ArrayList<>();
                        prescription.getPrescriptionItems().forEach(item ->
                                items.add(mapper.convertValue(item, PrescriptionItemDto.class))
                        );
                        dto.setMedications(items);
                    }
                });
            });

            history.add(dto);
        }
        return history;
    }
}
