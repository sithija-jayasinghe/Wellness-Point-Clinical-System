package edu.icet.repository;

import edu.icet.entity.LabTest;
import edu.icet.util.LabTestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    List<LabTest> findByPatient_Id(Long patientId);
    List<LabTest> findByLabOperator_UserId(Long labOperatorId);
    List<LabTest> findByStatus(LabTestStatus status);
    List<LabTest> findByDoctor_Id(Long doctorId);
}
