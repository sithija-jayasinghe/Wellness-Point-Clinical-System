package edu.icet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
    private String description;
    private Set<PermissionDto> permissions;
}
