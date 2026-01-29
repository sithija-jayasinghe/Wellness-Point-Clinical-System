package edu.icet.service;

import edu.icet.entity.Permission;
import java.util.List;

public interface PermissionService {
    void createPermission(Permission permission);
    List<Permission> getAllPermissions();
}