package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.AuditLogDto;
import edu.icet.entity.AuditLog;
import edu.icet.repository.AuditLogRepository;
import edu.icet.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper mapper;

    @Override
    public AuditLogDto createLog(AuditLogDto dto) {
        AuditLog log = mapper.convertValue(dto, AuditLog.class);

        // Auto-set timestamp if missing
        if (log.getTimestamp() == null) {
            log.setTimestamp(LocalDateTime.now());
        }

        AuditLog saved = auditLogRepository.save(log);
        return mapper.convertValue(saved, AuditLogDto.class);
    }

    @Override
    public List<AuditLogDto> getAllLogs() {
        List<AuditLog> all = auditLogRepository.findAll();
        List<AuditLogDto> dtos = new ArrayList<>();
        all.forEach(log -> dtos.add(mapper.convertValue(log, AuditLogDto.class)));
        return dtos;
    }

    @Override
    public List<AuditLogDto> getLogsByUserId(Long userId) {
        List<AuditLog> userLogs = auditLogRepository.findByUserId(userId);
        List<AuditLogDto> dtos = new ArrayList<>();
        userLogs.forEach(log -> dtos.add(mapper.convertValue(log, AuditLogDto.class)));
        return dtos;
    }

    @Override
    public AuditLogDto getLogById(Long id) {
        return auditLogRepository.findById(id)
                .map(log -> mapper.convertValue(log, AuditLogDto.class))
                .orElse(null);
    }
}
