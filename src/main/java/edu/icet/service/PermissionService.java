package edu.icet.service;

import edu.icet.dto.PermissionDto;
import java.util.List;

public interface PermissionService {
    PermissionDto createPermission(PermissionDto dto);
    List<PermissionDto> getAllPermissions();
    PermissionDto getPermissionById(Long id);
    void deletePermission(Long id);
    PermissionDto updatePermission(Long id, PermissionDto dto);
}
