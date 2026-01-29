package edu.icet.repository;

import edu.icet.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Count existing appointments for a specific schedule
    long countByDoctorScheduleId(Long scheduleId);

    // Check if patient is already booked for this schedule
    boolean existsByDoctorScheduleIdAndPatientId(Long scheduleId, Long patientId);

    // Find the maximum appointment number for a specific schedule
    @Query("SELECT MAX(a.appointmentNo) FROM Appointment a WHERE a.doctorSchedule.id = :scheduleId")
    Integer findMaxAppointmentNo(Long scheduleId);
}
