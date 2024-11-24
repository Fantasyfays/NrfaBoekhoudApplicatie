package com.example.nrfaboekhoudapplicatie;

import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.enums.RoleType;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void testRolesInitialized() {
        User user = new User();
        assertEquals(new HashSet<>(), user.getRoles(), "Roles moet leeg geïnitialiseerd zijn");
    }

    @Test
    void testAddRole() {
        User user = new User();
        user.setRoles(new HashSet<>());

        user.getRoles().add(RoleType.ADMIN);

        assertEquals(1, user.getRoles().size(), "Roles moet 1 zijn");
        assertEquals(RoleType.ADMIN, user.getRoles().iterator().next(), "Role moet ADMIN zijn");
    }
}
