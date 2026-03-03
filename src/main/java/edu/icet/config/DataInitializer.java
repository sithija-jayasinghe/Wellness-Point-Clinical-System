package edu.icet.config;

import edu.icet.entity.Permission;
import edu.icet.entity.Role;
import edu.icet.repository.PermissionRepository;
import edu.icet.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        initLabOperatorPermissions();
        initLabOperatorRole();
    }

    private void initLabOperatorPermissions() {
        List<String[]> labPermissions = List.of(
                new String[]{"LAB_TEST_CREATE", "Create lab test requests"},
                new String[]{"LAB_TEST_VIEW", "View lab test details and results"},
                new String[]{"LAB_TEST_UPDATE", "Update lab test results and status"},
                new String[]{"LAB_TEST_DELETE", "Delete lab test records"},
                new String[]{"PATIENT_VIEW", "View patient information"},
                new String[]{"PRESCRIPTION_VIEW", "View prescription details"}
        );

        for (String[] perm : labPermissions) {
            if (!permissionRepository.existsByCode(perm[0])) {
                Permission permission = new Permission();
                permission.setCode(perm[0]);
                permission.setDescription(perm[1]);
                permissionRepository.save(permission);
                log.info("Created permission: {}", perm[0]);
            }
        }
    }

    private void initLabOperatorRole() {
        if (roleRepository.findByName("LAB_OPERATOR").isEmpty()) {
            Role labOperatorRole = new Role();
            labOperatorRole.setName("LAB_OPERATOR");
            labOperatorRole.setDescription("Lab Operator - manages lab tests, views patient data and prescriptions");

            Set<Permission> permissions = new HashSet<>();
            List<String> permCodes = List.of(
                    "LAB_TEST_CREATE", "LAB_TEST_VIEW", "LAB_TEST_UPDATE",
                    "LAB_TEST_DELETE", "PATIENT_VIEW", "PRESCRIPTION_VIEW"
            );

            for (String code : permCodes) {
                permissionRepository.findByCode(code).ifPresent(permissions::add);
            }

            labOperatorRole.setPermissions(permissions);
            roleRepository.save(labOperatorRole);
            log.info("Created role: LAB_OPERATOR with {} permissions", permissions.size());
        }
    }
}
