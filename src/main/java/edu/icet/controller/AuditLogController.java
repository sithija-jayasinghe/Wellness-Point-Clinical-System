package edu.icet.controller;

import edu.icet.dto.AuditLogDto;
import edu.icet.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit-log")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService service;

    @PostMapping("/add")
    public AuditLogDto createLog(@RequestBody AuditLogDto dto) {
        return service.createLog(dto);
    }

    @GetMapping("/get-all")
    public List<AuditLogDto> getAllLogs() {
        return service.getAllLogs();
    }

    @GetMapping("/user/{userId}")
    public List<AuditLogDto> getLogsByUserId(@PathVariable Long userId) {
        return service.getLogsByUserId(userId);
    }

    @GetMapping("/{id}")
    public AuditLogDto getLogById(@PathVariable Long id) {
        return service.getLogById(id);
    }
}
