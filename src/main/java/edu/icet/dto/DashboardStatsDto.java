package edu.icet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDto {
    private Long totalPatients;
    private Long todayAppointments;
    private Double todayIncome;
    private Double totalIncome;
}
