package edu.icet.controller;

import edu.icet.dto.PermissionDto;
import edu.icet.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService service;

    @PostMapping("/add")
    public PermissionDto addPermission(@RequestBody PermissionDto dto) {
        return service.createPermission(dto);
    }

    @GetMapping("/get-all")
    public List<PermissionDto> getAllPermissions() {
        return service.getAllPermissions();
    }

    @GetMapping("/{id}")
    public PermissionDto getPermissionById(@PathVariable Long id) {
        return service.getPermissionById(id);
    }

    @PutMapping("/update/{id}")
    public PermissionDto updatePermission(@PathVariable Long id, @RequestBody PermissionDto dto) {
        return service.updatePermission(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePermission(@PathVariable Long id) {
        service.deletePermission(id);
    }
}
