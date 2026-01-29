package edu.icet.service;

import edu.icet.entity.Role;
import java.util.List;

public interface RoleService {
    void createRole(Role role);
    List<Role> getAllRoles();

    void addPermissionToRole(String roleName, String permissionCode);
}