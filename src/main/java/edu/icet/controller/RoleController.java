package edu.icet.controller;

import edu.icet.entity.Role;
import edu.icet.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin
public class RoleController {

    @Autowired
    private final RoleRepository roleRepository;

    @PostMapping("/add")
    public void addRole(@RequestBody Role role) {
        roleRepository.save(role);
    }

    @GetMapping("/get-all")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}