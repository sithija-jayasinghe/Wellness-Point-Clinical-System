package edu.icet.service;

import edu.icet.dto.AppointmentDto;
import java.util.List;

public interface AppointmentService {
    AppointmentDto bookAppointment(AppointmentDto dto);

    // New methods
    List<AppointmentDto> getAllAppointments();
    AppointmentDto getAppointmentById(Long id);
    void deleteAppointment(Long id);
    AppointmentDto updateAppointment(Long id, AppointmentDto appointmentDto);

    void cancelAppointment(Long id);
    void completeAppointment(Long id);
}
