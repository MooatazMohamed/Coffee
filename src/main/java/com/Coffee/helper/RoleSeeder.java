package com.Coffee.helper;

import com.Coffee.model.Role;
import com.Coffee.repository.user.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        createRoleIfNotExist("CUSTOMER");
        createRoleIfNotExist("MANAGER");
        createRoleIfNotExist("ADMIN");
    }

    private void createRoleIfNotExist(String name) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }
}
