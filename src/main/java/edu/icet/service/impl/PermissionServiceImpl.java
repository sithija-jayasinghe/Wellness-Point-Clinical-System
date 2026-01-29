package edu.icet.service.impl;

import edu.icet.entity.Permission;
import edu.icet.repository.PermissionRepository;
import edu.icet.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public void createPermission(Permission permission) {
        if (permissionRepository.existsByCode(permission.getCode())) {
            throw new RuntimeException("Permission code already exists!");
        }
        permissionRepository.save(permission);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}