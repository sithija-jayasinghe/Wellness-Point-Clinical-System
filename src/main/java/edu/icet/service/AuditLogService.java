package edu.icet.service;

import edu.icet.dto.AuditLogDto;
import java.util.List;

public interface AuditLogService {
    AuditLogDto createLog(AuditLogDto dto);
    List<AuditLogDto> getAllLogs();
    List<AuditLogDto> getLogsByUserId(Long userId);
    AuditLogDto getLogById(Long id);
}
