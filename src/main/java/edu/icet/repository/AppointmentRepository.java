package edu.icet.repository;

import edu.icet.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Integer countByDoctorScheduleId(Long scheduleId);

    @Query("SELECT MAX(a.appointmentNo) FROM Appointment a WHERE a.doctorSchedule.id = :scheduleId")
    Integer findMaxAppointmentNo(Long scheduleId);
}
