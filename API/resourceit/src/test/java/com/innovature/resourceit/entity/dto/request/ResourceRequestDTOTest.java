package com.innovature.resourceit.entity.dto.request;

import com.innovature.resourceit.entity.dto.requestdto.ResourceRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceSkillRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

 class ResourceRequestDTOTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
     void testResourceRequestDTO() {
        // Create a sample ResourceRequestDTO
        ResourceRequestDTO resourceRequestDTO = new ResourceRequestDTO();
        resourceRequestDTO.setEmployeeId(1);
        resourceRequestDTO.setName("John Doe");
        resourceRequestDTO.setEmail("john.doe@example.com");
        resourceRequestDTO.setExperience(5);
        resourceRequestDTO.setJoiningDate(new Date());
        resourceRequestDTO.setRole(2);
        resourceRequestDTO.setDepartmentId(3);

        Set<ResourceSkillRequestDTO> skills = new HashSet<>();
        skills.add(new ResourceSkillRequestDTO(1, 1,(byte)1));
        skills.add(new ResourceSkillRequestDTO(2, 1,(byte)1));
        resourceRequestDTO.setSkills(skills);

        // Test the getters
        assertEquals(1, resourceRequestDTO.getEmployeeId());
        assertEquals("John Doe", resourceRequestDTO.getName());
        assertEquals("john.doe@example.com", resourceRequestDTO.getEmail());
        assertEquals(5, resourceRequestDTO.getExperience());
        assertNotNull(resourceRequestDTO.getJoiningDate());
        assertEquals(2, resourceRequestDTO.getRole());
        assertEquals(3, resourceRequestDTO.getDepartmentId());
        assertEquals(skills, resourceRequestDTO.getSkills());

        // Test validation
        var violations = validator.validate(resourceRequestDTO);
        assertTrue(violations.isEmpty(), "There should be no validation errors");

        // Test constraints
        resourceRequestDTO.setRole(null); // Set role to null
        violations = validator.validate(resourceRequestDTO);
        assertEquals(1, violations.size(), "There should be one validation error for role being null");
        resourceRequestDTO.setRole(2);
        resourceRequestDTO.setSkills(null); // Set skills to null
        violations = validator.validate(resourceRequestDTO);
        assertEquals(0, violations.size(), "There should be one validation error for skills being null");
    }
}
