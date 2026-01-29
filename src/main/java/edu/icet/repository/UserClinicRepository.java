package edu.icet.repository;

import edu.icet.entity.UserClinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserClinicRepository extends JpaRepository<UserClinic, Long> {
}