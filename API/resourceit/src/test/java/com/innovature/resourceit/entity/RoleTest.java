package com.innovature.resourceit.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Objects;

@SpringBootTest
@ContextConfiguration(classes = RoleTest.class)
public class RoleTest {

    @Test
    public void testRoleEntity() {
        Integer roleId = 1;
        String name = "ADMIN";
        Date currentDate = new Date();

        Role role = new Role();
        role.setId(roleId);
        role.setName(name);
        role.setCreatedDate(currentDate);
        role.setUpdatedDate(currentDate);

        assertEquals(roleId, role.getId());
        assertEquals(name, role.getName());
        assertEquals(currentDate, role.getCreatedDate());
        assertEquals(currentDate, role.getUpdatedDate());
    }

    @Test
    void testConstructorWithAllArgument() {
        Integer roleId = 1;
        String name = "ADMIN";
        Date currentDate = new Date();

        Role role = new Role(roleId, name, currentDate, currentDate);

        assertEquals(roleId, role.getId());
        assertEquals(name, role.getName());
        assertEquals(currentDate, role.getCreatedDate());
        assertEquals(currentDate, role.getUpdatedDate());
    }

    @Test
    void testSetterAndGetterForId() {
        Role role = new Role();
        Integer roleId = 1;
        role.setId(roleId);
        assertEquals(roleId, role.getId());
    }

    @Test
    void testSetterAndGetterForName() {
        Role role = new Role();
        String name = "USER";
        role.setName(name);
        assertEquals(name, role.getName());
    }

    @Test
    void testSetterAndGetterForCreatedDate() {
        Role role = new Role();
        Date currentDate = new Date();
        role.setCreatedDate(currentDate);
        assertEquals(currentDate, role.getCreatedDate());
    }

    @Test
    void testSetterAndGetterForUpdatedDate() {
        Role role = new Role();
        Date currentDate = new Date();
        role.setUpdatedDate(currentDate);
        assertEquals(currentDate, role.getUpdatedDate());
    }

    @Test
    void testToStringMethod() {
        Integer roleId = 1;
        String name = "ADMIN";
        Date currentDate = new Date();

        Role role = new Role(roleId, name, currentDate, currentDate);

        String expectedToString = "Role(id=1, name=ADMIN, createdDate=" + currentDate + ", updatedDate=" + currentDate + ")";
        assertEquals(expectedToString, role.toString());
    }
    @Test
    void testHashCodeMethod() {
        Role role1 = new Role("ADMIN");
        Role role2 = new Role("ADMIN");
        Role role3 = new Role("USER");

        assertEquals(role1.hashCode(), role2.hashCode());
        assertNotEquals(role1.hashCode(), role3.hashCode());
    }

    @Test
    void testEqualsMethod() {
        Role role1 = new Role( "ADMIN");
        Role role2 = new Role("ADMIN");
        Role role3 = new Role("USER");

        assertEquals(role1, role2);
        assertNotEquals(role1, role3);
        assertNotEquals(role2, role3);
    }
    @Test
    void testEqualsMethodReflexive() {
        Role role1 = new Role("ADMIN");

        assertEquals(role1, role1);
    }
    @Test
    void testEqualsMethodWithNull() {
        Role role1 = new Role("ADMIN");

        assertNotEquals(role1, null);
    }
    @Test
    void testEqualsMethodWithDifferentClass() {
        Role role1 = new Role("ADMIN");

        assertNotEquals(role1, "SomeString");
    }
    @Test
    void testHashCodeConsistency() {
        Role role1 = new Role("ADMIN");

        int hashCode1 = role1.hashCode();
        int hashCode2 = role1.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
    @Test
    void testHashCodeWithNull() {
        Role role1 = new Role("ADMIN");

        assertNotEquals(role1.hashCode(), Objects.hashCode(null));
    }
    @Test
    void testHashCodeWithDifferentClass() {
        Role role1 = new Role("ADMIN");

        assertNotEquals(role1.hashCode(), "SomeString".hashCode());
    }

}
