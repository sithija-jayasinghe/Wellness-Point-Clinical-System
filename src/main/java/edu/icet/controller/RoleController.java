package edu.icet.controller;

import edu.icet.entity.Role;
import edu.icet.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/add")
    public void createRole(@RequestBody Role role) {
        roleService.createRole(role);
    }

    @GetMapping("/get-all")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/add-permission")
    public void addPermissionToRole(@RequestParam String roleName, @RequestParam String permissionCode) {
        roleService.addPermissionToRole(roleName, permissionCode);
    }
}