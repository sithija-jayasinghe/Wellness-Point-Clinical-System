package edu.icet.service;

import edu.icet.entity.Clinic;
import java.util.List;

public interface ClinicService {
    void addClinic(Clinic clinic);
    List<Clinic> getAllClinics();
    void updateClinic(Long id, Clinic clinic);
    void deleteClinic(Long id);
}