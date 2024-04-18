package com.innovature.resourceit.entity.dto.request;

import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;


import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 class ProjectRequestRequestDTOTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
     void testProjectRequestRequestDTO() {
        // Create a sample ProjectRequestRequestDTO
        ProjectRequestRequestDTO projectRequestRequestDTO = new ProjectRequestRequestDTO();
        projectRequestRequestDTO.setProjectId(1);
        projectRequestRequestDTO.setProjectCode("PROJ001");
        projectRequestRequestDTO.setName("Sample Project");
        projectRequestRequestDTO.setProjectType((byte) 1);
        projectRequestRequestDTO.setDescription("This is a sample project.");
        projectRequestRequestDTO.setClientName("Sample Client");
        projectRequestRequestDTO.setTeamSize(5);
        projectRequestRequestDTO.setManDay(50);
        projectRequestRequestDTO.setManagerId(1);
        projectRequestRequestDTO.setStartDate(new Date());
        projectRequestRequestDTO.setEndDate(new Date());
        projectRequestRequestDTO.setSkillIds(List.of(1, 2, 3));
        projectRequestRequestDTO.setApprovalStatus((byte) 1);

        // Test the getters
        assertEquals(1, projectRequestRequestDTO.getProjectId());
        assertEquals("PROJ001", projectRequestRequestDTO.getProjectCode());
        assertEquals("Sample Project", projectRequestRequestDTO.getName());
        assertEquals((byte) 1, projectRequestRequestDTO.getProjectType());
        assertEquals("This is a sample project.", projectRequestRequestDTO.getDescription());
        assertEquals("Sample Client", projectRequestRequestDTO.getClientName());
        assertEquals(5, projectRequestRequestDTO.getTeamSize());
        assertEquals(50, projectRequestRequestDTO.getManDay());
        assertEquals(1, projectRequestRequestDTO.getManagerId());
        assertNotNull(projectRequestRequestDTO.getStartDate());
        assertNotNull(projectRequestRequestDTO.getEndDate());
        assertEquals(List.of(1, 2, 3), projectRequestRequestDTO.getSkillIds());
        assertEquals((byte) 1, projectRequestRequestDTO.getApprovalStatus());

        // Test validation
        var violations = validator.validate(projectRequestRequestDTO);
        assertTrue(violations.isEmpty(), "There should be no validation errors");

        // Test constraints
        projectRequestRequestDTO.setProjectType((byte) 2); // Set an invalid project type
        violations = validator.validate(projectRequestRequestDTO);
        assertEquals(1, violations.size(), "There should be one validation error for projectType");

        projectRequestRequestDTO.setProjectType(null); // Set projectType to null
        violations = validator.validate(projectRequestRequestDTO);
        assertEquals(1, violations.size(), "There should be one validation error for projectType being null");
    }
}
