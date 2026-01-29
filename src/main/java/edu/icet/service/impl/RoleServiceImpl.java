package edu.icet.service.impl;

import edu.icet.entity.Permission;
import edu.icet.entity.Role;
import edu.icet.repository.PermissionRepository;
import edu.icet.repository.RoleRepository;
import edu.icet.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void createRole(Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new RuntimeException("Role already exists!");
        }
        roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void addPermissionToRole(String roleName, String permissionCode) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Permission permission = permissionRepository.findByCode(permissionCode)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        role.getPermissions().add(permission);

        roleRepository.save(role);
    }
}