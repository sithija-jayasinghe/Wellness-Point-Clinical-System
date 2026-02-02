package edu.icet.service.impl;

import edu.icet.dto.DashboardStatsDto;
import edu.icet.repository.AppointmentRepository;
import edu.icet.repository.PatientRepository;
import edu.icet.repository.PaymentRepository;
import edu.icet.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;
    private final PaymentRepository paymentRepo;

    @Override
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();

        // 1. Total Patients
        stats.setTotalPatients(patientRepo.countByDeletedFalse());

        // 2. Today's Appointments
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        stats.setTodayAppointments(appointmentRepo.countByAppointmentTimeBetweenAndDeletedFalse(startOfDay, endOfDay));

        // 3. Today's Income
        Double todayIncome = paymentRepo.sumAmountByPaymentDate(LocalDate.now());
        stats.setTodayIncome(todayIncome != null ? todayIncome : 0.0);

        // 4. Total Income
        Double totalIncome = paymentRepo.sumTotalAmount();
        stats.setTotalIncome(totalIncome != null ? totalIncome : 0.0);

        return stats;
    }
}
