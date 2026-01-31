package edu.icet.repository;

import edu.icet.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    @Query("SELECT COUNT(s) > 0 FROM DoctorSchedule s WHERE s.doctorId = :doctorId AND ((s.startDateTime < :endDateTime AND s.endDateTime > :startDateTime))")
    boolean existsOverlappingSchedule(@Param("doctorId") Long doctorId, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);
}
