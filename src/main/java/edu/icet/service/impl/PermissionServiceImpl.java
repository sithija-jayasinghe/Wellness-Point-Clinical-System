package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.PermissionDto;
import edu.icet.entity.Permission;
import edu.icet.repository.PermissionRepository;
import edu.icet.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final ObjectMapper mapper;

    @Override
    public PermissionDto createPermission(PermissionDto dto) {
        Permission permission = mapper.convertValue(dto, Permission.class);
        Permission saved = permissionRepository.save(permission);
        return mapper.convertValue(saved, PermissionDto.class);
    }

    @Override
    public List<PermissionDto> getAllPermissions() {
        List<Permission> all = permissionRepository.findAll();
        List<PermissionDto> dtos = new ArrayList<>();
        all.forEach(p -> dtos.add(mapper.convertValue(p, PermissionDto.class)));
        return dtos;
    }

    @Override
    public PermissionDto getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .map(p -> mapper.convertValue(p, PermissionDto.class))
                .orElse(null);
    }

    @Override
    public void deletePermission(Long id) {
        if (permissionRepository.existsById(id)) {
            permissionRepository.deleteById(id);
        }
    }

    @Override
    public PermissionDto updatePermission(Long id, PermissionDto dto) {
        Optional<Permission> existingOpt = permissionRepository.findById(id);
        if (existingOpt.isPresent()) {
            Permission existing = existingOpt.get();
            existing.setCode(dto.getCode());
            existing.setDescription(dto.getDescription());

            Permission saved = permissionRepository.save(existing);
            return mapper.convertValue(saved, PermissionDto.class);
        }
        return null;
    }
}
