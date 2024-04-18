package com.innovature.resourceit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SkillTest {

    @Mock
    private Department department;

    private Skill skill;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        skill = new Skill(1, "Java");
        skill.setDepartment(department);
        Date dt = new Date();
        skill.setCreatedDate(dt);
        skill.setUpdatedDate(dt);
    }

    @Test
    void testSkillConstructor() {
        assertEquals(1, skill.getId());
        assertEquals("Java", skill.getName());
        assertEquals(department, skill.getDepartment());
        assertNotNull(skill.getCreatedDate());
        assertNotNull(skill.getUpdatedDate());
    }

    @Test
    void testParameterizedConstructor() {
        Date dt = new Date();
        Skill newSkill = new Skill(2, "Python", department, dt, dt);
        assertEquals(2, newSkill.getId());
        assertEquals("Python", newSkill.getName());
        assertEquals(department, newSkill.getDepartment());
        assertNotNull(newSkill.getCreatedDate());
        assertNotNull(newSkill.getUpdatedDate());
    }

    @Test
    void testGetterAndSetter() {
        Skill newSkill = new Skill();
        newSkill.setId(3);
        newSkill.setName("C++");
        newSkill.setDepartment(department);
        newSkill.setCreatedDate(new Date());
        newSkill.setUpdatedDate(new Date());

        assertEquals(3, newSkill.getId());
        assertEquals("C++", newSkill.getName());
        assertEquals(department, newSkill.getDepartment());
        assertNotNull(newSkill.getCreatedDate());
        assertNotNull(newSkill.getUpdatedDate());
    }

    @Test
    void testParameterizedConstructorWithNullDepartment() {
        Date dt = new Date();
        Skill newSkill = new Skill(2, "Python", null, dt, dt);
        assertEquals(2, newSkill.getId());
        assertEquals("Python", newSkill.getName());
        assertNull(newSkill.getDepartment());
        assertNotNull(newSkill.getCreatedDate());
        assertNotNull(newSkill.getUpdatedDate());
    }

    @Test
    void testDefaultConstructor() {
        Skill newSkill = new Skill();
        assertEquals((Integer) null, newSkill.getId());
        assertNull(newSkill.getName());
        assertNull(newSkill.getDepartment());
        assertNull(newSkill.getCreatedDate());
        assertNull(newSkill.getUpdatedDate());
    }

    @Test
    void testToString() {
        String expectedToString = "Skill(id=1, name=Java, department=" + department + ", createdDate=" + skill.getCreatedDate() + ", updatedDate=" + skill.getUpdatedDate() + ")";
        assertEquals(expectedToString, skill.toString());
    }

    @Test
    void testSkillIntegerConstructor() {
        Skill newSkill = new Skill(2);
        assertEquals(2, newSkill.getId());
        assertNull(newSkill.getName());
        assertNull(newSkill.getDepartment());
        assertNull(newSkill.getCreatedDate());
        assertNull(newSkill.getUpdatedDate());
    }

    @Test
    void testSkillConstructorWIthId() {
        Integer expectedSkillId = 42;
        Skill skill = new Skill(expectedSkillId);
        assertEquals(expectedSkillId, skill.getId());
    }


}
