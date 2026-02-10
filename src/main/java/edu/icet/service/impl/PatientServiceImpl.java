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
    private final AppointmentRepository appointmentRepo;
    private final ConsultationRepository consultationRepo;
    private final PrescriptionRepository prescriptionRepo;
    
    // Injected for User Creation
    private final edu.icet.repository.UserRepository userRepo;
    private final edu.icet.repository.RoleRepository roleRepo;
    private final edu.icet.repository.UserRoleRepository userRoleRepo;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    
    private final ObjectMapper mapper;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void addPatient(PatientDto patientDto) {
        if (patientRepo.findByNic(patientDto.getNic()).isPresent()) {
            throw new ResourceAlreadyExistsException("Patient with NIC " + patientDto.getNic() + " already exists.");
        }

        // 1. Create User Entity
        edu.icet.entity.User user = new edu.icet.entity.User();
        
        // Handle potential username duplicates (simplistic approach: append phone if name exists)
        String username = patientDto.getName();
        if (userRepo.existsByUsername(username)) {
            // If the name exists, try appending last 4 digits of phone
            String phone = patientDto.getPhone();
            if (phone.length() >= 4) {
                 username = username + phone.substring(phone.length() - 4);
            } else {
                 username = username + "_" + System.currentTimeMillis();
            }
        }
        
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(patientDto.getPhone())); // Password is Phone
        user.setEmail(patientDto.getPhone() + "@wellness.com"); // Dummy Email
        user.setStatus("ACTIVE");
        
        edu.icet.entity.User savedUser = userRepo.save(user);

        // 2. Assign Role (PATIENT)
        edu.icet.entity.Role role = roleRepo.findByName("PATIENT")
                .orElseThrow(() -> new RuntimeException("Role 'PATIENT' not found"));

        edu.icet.entity.UserRole userRole = new edu.icet.entity.UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(role);
        userRoleRepo.save(userRole);

        // 3. Create Patient and Link User
        Patient patient = mapper.convertValue(patientDto, Patient.class);
        patient.setUserId(savedUser.getUserId());
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
        return byId.filter(p -> !p.isDeleted())
                .map(entity -> mapper.convertValue(entity, PatientDto.class))
                .orElse(null);
    }

    @Override
    public void updatePatient(Long id, PatientDto patientDto) {
        Patient existing = patientRepo.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Patient updatedInfo = mapper.convertValue(patientDto, Patient.class);

        // Preserve userId if not provided in DTO
        if (updatedInfo.getUserId() == null) {
             updatedInfo.setUserId(existing.getUserId());
        }

        // Preserve deleted status
        updatedInfo.setDeleted(existing.isDeleted());

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
