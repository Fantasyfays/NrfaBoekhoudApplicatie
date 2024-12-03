package com.example.nrfaboekhoudapplicatie.config;

import com.example.nrfaboekhoudapplicatie.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;

    public DataInitializer(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        roleService.initializeRoles();
        System.out.println("Roles initialized");
    }
}
