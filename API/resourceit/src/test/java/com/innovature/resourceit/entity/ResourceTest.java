/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = ResourceTest.class)
class ResourceTest {

    private Department department;
    private Role role;

    @BeforeEach
    void setUp() {

        department = new Department(1, "Java", 1);
        role = new Role("ADMIN");
        role.setId(1);
    }

    @Test
    void testResourceEntity() {
        Integer id = 1;
        int employeeId = 1515;

        String email = "test@gmail.com";
        String band = "RIT";
        String name = "Test";
        Date joiningDate = new Date(0);
        Integer experience = 4;

        Date createDate = new Date(0);
        Date updateDate = new Date(0);

        byte status = (byte) 0;

        Resource resource = new Resource();
        resource.setId(id);
        resource.setCreatedDate(createDate);
        resource.setDepartment(department);
        resource.setEmail(email);
        resource.setEmployeeId(employeeId);
        resource.setExperience(experience);
        resource.setJoiningDate(joiningDate);
        resource.setName(name);
        resource.setRole(role);
        resource.setStatus(status);
        resource.setUpdatedDate(updateDate);

        assertEquals(id, resource.getId());
        assertEquals(name, resource.getName());
        assertEquals(email, resource.getEmail());
        assertEquals(joiningDate, resource.getJoiningDate());
        assertEquals(employeeId, resource.getEmployeeId());
        assertEquals(experience, resource.getExperience());
        assertEquals(department.getDepartmentId(), resource.getDepartment().getDepartmentId());
        assertEquals("ADMIN", resource.getRole().getName());
        assertEquals(role.getId(), resource.getRole().getId());
        assertEquals(createDate, resource.getCreatedDate());
        assertEquals(updateDate, resource.getUpdatedDate());
        assertEquals(status, resource.getStatus());
    }

    @Test
    void testConstructorWithAllArgument() {
        Date date = new Date(0);
        Skill s = new Skill(1, "Java");
        Set<Skill> skills = new HashSet<>();
        skills.add(s);
        Resource resource = new Resource(1, 1515, department, "test@gmail.com",  "Test", date, 4, 4, role, (byte) 0, date, date, (byte) 0, new Date(),new HashSet<>());
        assertEquals(1, resource.getId());
        assertEquals("Test", resource.getName());
        assertEquals("test@gmail.com", resource.getEmail());
        assertEquals(date, resource.getJoiningDate());
        assertEquals(1515, resource.getEmployeeId());
        assertEquals(4, resource.getExperience());
        assertEquals(department.getDepartmentId(), resource.getDepartment().getDepartmentId());
        assertEquals("ADMIN", resource.getRole().getName());
        assertEquals(role.getId(), resource.getRole().getId());
        assertEquals(date, resource.getCreatedDate());
        assertEquals(date, resource.getUpdatedDate());
        assertEquals((byte) 0, resource.getStatus());
    }

    @Test
    void testStatusEnum() {
        byte activeValue = 1;
        byte inactiveValue = 0;
        byte excludeValue = 2;

        assertEquals(inactiveValue, Resource.Status.INACTIVE.value);
        assertEquals(activeValue, Resource.Status.ACTIVE.value);
        assertEquals(excludeValue, Resource.Status.EXCLUDE.value);
    }

}
