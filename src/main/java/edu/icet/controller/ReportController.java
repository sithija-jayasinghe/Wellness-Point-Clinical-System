package edu.icet.controller;

import edu.icet.dto.DashboardStatsDto;
import edu.icet.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard-stats")
    public DashboardStatsDto getDashboardStats() {
        return reportService.getDashboardStats();
    }
}
