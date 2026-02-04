package edu.icet.repository;

import edu.icet.entity.User;
import edu.icet.entity.UserClinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserClinicRepository extends JpaRepository<UserClinic, Long> {
    List<UserClinic> findByUser(User user);
    void deleteByUser(User user);
}