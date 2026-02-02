package edu.icet.repository;

import edu.icet.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByNic(String nic);
    List<Patient> findByDeletedFalse();
    long countByDeletedFalse();
}
