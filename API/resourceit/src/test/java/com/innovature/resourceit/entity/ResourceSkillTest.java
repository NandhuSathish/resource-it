package com.innovature.resourceit.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = ResourceSkillTest.class)
 class ResourceSkillTest {

    @Test
    void testResourceSkillEntity() {
        Resource resource = new Resource();
        resource.setId(1);

        Skill skill = new Skill();
        skill.setId(2);

        ResourceSkill resourceSkill = new ResourceSkill();
        resourceSkill.setId(3);
        resourceSkill.setResource(resource);
        resourceSkill.setSkill(skill);
        resourceSkill.setExperience(4);

        assertEquals(3, resourceSkill.getId());
        assertEquals(resource, resourceSkill.getResource());
        assertEquals(skill, resourceSkill.getSkill());
        assertEquals(4, resourceSkill.getExperience());
    }

    @Test
    void testSetterAndGetterForId() {
        ResourceSkill resourceSkill = new ResourceSkill();
        resourceSkill.setId(1);
        assertEquals(1, resourceSkill.getId());
    }

    @Test
    void testSetterAndGetterForResource() {
        Resource resource = new Resource();
        resource.setId(1);

        ResourceSkill resourceSkill = new ResourceSkill();
        resourceSkill.setResource(resource);
        assertEquals(resource, resourceSkill.getResource());
    }

    @Test
    void testSetterAndGetterForSkill() {
        Skill skill = new Skill();
        skill.setId(2);

        ResourceSkill resourceSkill = new ResourceSkill();
        resourceSkill.setSkill(skill);
        assertEquals(skill, resourceSkill.getSkill());
    }

    @Test
    void testSetterAndGetterForExperience() {
        ResourceSkill resourceSkill = new ResourceSkill();
        resourceSkill.setExperience(4);
        assertEquals(4, resourceSkill.getExperience());
    }
    @Test
    void testAllArgsConstructor() {
        Resource resource = new Resource();
        resource.setId(1);

        Skill skill = new Skill();
        skill.setId(2);


        ResourceSkill resourceSkill = new ResourceSkill(3, resource, skill, 4,(byte) 1);

        assertEquals(3, resourceSkill.getId());
        assertEquals(resource, resourceSkill.getResource());
        assertEquals(skill, resourceSkill.getSkill());
        assertEquals(4, resourceSkill.getExperience());
    }
}
