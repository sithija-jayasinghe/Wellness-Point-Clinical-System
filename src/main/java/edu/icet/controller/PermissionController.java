package edu.icet.controller;

import edu.icet.entity.Permission;
import edu.icet.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@CrossOrigin
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/add")
    public void createPermission(@RequestBody Permission permission) {
        permissionService.createPermission(permission);
    }

    @GetMapping("/get-all")
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }
}