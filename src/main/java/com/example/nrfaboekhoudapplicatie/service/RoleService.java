package com.example.nrfaboekhoudapplicatie.service;

import com.example.nrfaboekhoudapplicatie.dal.entity.Role;
import com.example.nrfaboekhoudapplicatie.dal.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }

    public void initializeRoles() {
        if (roleRepository.findByName("CLIENT").isEmpty()) {
            roleRepository.save(new Role("CLIENT"));
        }

        if (roleRepository.findByName("ACCOUNTANT").isEmpty()) {
            roleRepository.save(new Role("ACCOUNTANT"));
        }
    }

}
