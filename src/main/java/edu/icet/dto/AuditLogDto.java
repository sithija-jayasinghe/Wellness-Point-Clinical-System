package edu.icet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDto {
    private Long id;
    private Long userId;
    private String action;
    private String entity;
    private Long entityId;
    private LocalDateTime timestamp;
}
