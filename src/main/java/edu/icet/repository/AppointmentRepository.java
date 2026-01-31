package edu.icet.repository;

import edu.icet.entity.Appointment;
import edu.icet.util.AppointmentStatus;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Count existing appointments
    long countByDoctorScheduleId(Long scheduleId);

    // Check if patient is already booked for this schedule
    boolean existsByDoctorScheduleIdAndPatientId(Long scheduleId, Long patientId);

    // Find the maximum appointment number
    @Query("SELECT MAX(a.appointmentNo) FROM Appointment a WHERE a.doctorSchedule.id = :scheduleId")
    Integer findMaxAppointmentNo(Long scheduleId);

    boolean existsByPatientIdAndAppointmentTimeAndStatusNot(Long patientId, LocalDateTime appointmentTime, AppointmentStatus status);

    boolean existsByDoctorScheduleDoctorIdAndAppointmentTimeAndStatusNot(Long doctorId, LocalDateTime appointmentTime, AppointmentStatus status);
}
