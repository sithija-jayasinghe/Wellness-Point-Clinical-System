package edu.icet.repository;

import edu.icet.entity.Appointment;
import edu.icet.util.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Count existing appointments (excluding soft deleted)
    long countByDoctorScheduleIdAndDeletedFalse(Long scheduleId);

    // Check if patient is already booked for this schedule (excluding soft deleted)
    boolean existsByDoctorScheduleIdAndPatientIdAndDeletedFalse(Long scheduleId, Long patientId);

    // Find the maximum appointment number
    @Query("SELECT MAX(a.appointmentNo) FROM Appointment a WHERE a.doctorSchedule.id = :scheduleId AND a.deleted = false")
    Integer findMaxAppointmentNo(Long scheduleId);

    boolean existsByPatientIdAndAppointmentTimeAndStatusNotAndDeletedFalse(Long patientId, LocalDateTime appointmentTime, AppointmentStatus status);

    boolean existsByDoctorScheduleDoctorIdAndAppointmentTimeAndStatusNotAndDeletedFalse(Long doctorId, LocalDateTime appointmentTime, AppointmentStatus status);

    List<Appointment> findByDeletedFalse();

    List<Appointment> findByPatientIdAndDeletedFalseOrderByAppointmentTimeDesc(Long patientId);

    long countByAppointmentTimeBetweenAndDeletedFalse(LocalDateTime start, LocalDateTime end);
}
