package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.responsedto.ResourceListingResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceSkillResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceListingResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        ResourceListingResponseDTO responseDTO = new ResourceListingResponseDTO();

        responseDTO.setId(1);
        responseDTO.setName("John Doe");
        responseDTO.setDepartmentName("IT");
        responseDTO.setProjectName("Sample Project");
        responseDTO.setSkillNameAndExperience("Java (5 years)");
        responseDTO.setTotalExperience("7 years");
        responseDTO.setJoiningDate("2022-01-01");
        responseDTO.setEmail("john.doe@example.com");
        responseDTO.setAllocationStatus("Allocated");
        responseDTO.setRoleName("Developer");
        responseDTO.setStatus("Active");
        responseDTO.setEmployeeId(123);

        List<ResourceSkillResponseDTO> skillResponseDTOs = Arrays.asList(
                new ResourceSkillResponseDTO(),
                new ResourceSkillResponseDTO()
        );

        responseDTO.setResourceSkillResponseDTOs(skillResponseDTOs);

        assertEquals(1, responseDTO.getId());
        assertEquals("John Doe", responseDTO.getName());
        assertEquals("IT", responseDTO.getDepartmentName());
        assertEquals("Sample Project", responseDTO.getProjectName());
        assertEquals("Java (5 years)", responseDTO.getSkillNameAndExperience());
        assertEquals("7 years", responseDTO.getTotalExperience());
        assertEquals("2022-01-01", responseDTO.getJoiningDate());
        assertEquals("john.doe@example.com", responseDTO.getEmail());
        assertEquals("Allocated", responseDTO.getAllocationStatus());
        assertEquals("Developer", responseDTO.getRoleName());
        assertEquals("Active", responseDTO.getStatus());
        assertEquals(123, responseDTO.getEmployeeId());

        assertEquals(skillResponseDTOs, responseDTO.getResourceSkillResponseDTOs());
    }
}
