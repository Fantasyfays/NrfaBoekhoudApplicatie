package com.example.nrfaboekhoudapplicatie;

import com.example.nrfaboekhoudapplicatie.dal.entity.Role;
import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.enums.RoleType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserTest {

    @Test
    public void testAddRole() {
        User user = new User();
        Role role = new Role();
        role.setName(RoleType.ADMIN);

        user.getRoles().add(role);

        assertEquals(1, user.getRoles().size(), "roles moet 1 zijn");
    }
}
