package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.requestdto.ResourceSkillRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceResponseDTOTest {

    @Test
    void testConstructor() {
        // Create sample data for ResourceResponseDTO
        ResourceResponseDTO resourceResponseDTO = new ResourceResponseDTO();
        resourceResponseDTO.setId(1);
        resourceResponseDTO.setEmployeeId(12345);
        resourceResponseDTO.setDepartmentId(1);
        resourceResponseDTO.setEmail("test@example.com");
        resourceResponseDTO.setName("John Doe");
        resourceResponseDTO.setJoiningDate("2023-01-01");
        resourceResponseDTO.setExperience(5);
        resourceResponseDTO.setRole(1);
        resourceResponseDTO.setAllocationStatus((byte) 1);
        resourceResponseDTO.setCreatedDate("2023-01-01");
        resourceResponseDTO.setUpdatedDate("2023-01-02");
        resourceResponseDTO.setStatus((byte) 1);

        // Create a list of ResourceSkillRequestDTO
        List<ResourceSkillRequestDTO> skills = new ArrayList<>();
        skills.add(new ResourceSkillRequestDTO(1, 4,(byte)1));
        skills.add(new ResourceSkillRequestDTO(2, 3,(byte)1));
        resourceResponseDTO.setSkills(skills);

        // Verify values set by the constructor
        assertEquals(1, resourceResponseDTO.getId());
        assertEquals(12345, resourceResponseDTO.getEmployeeId());
        assertEquals(1, resourceResponseDTO.getDepartmentId());
        assertEquals("test@example.com", resourceResponseDTO.getEmail());
        assertEquals("John Doe", resourceResponseDTO.getName());
        assertEquals("2023-01-01", resourceResponseDTO.getJoiningDate());
        assertEquals(5, resourceResponseDTO.getExperience());
        assertEquals(1, resourceResponseDTO.getRole());
        assertEquals((byte) 1, resourceResponseDTO.getAllocationStatus());
        assertEquals("2023-01-01", resourceResponseDTO.getCreatedDate());
        assertEquals("2023-01-02", resourceResponseDTO.getUpdatedDate());
        assertEquals((byte) 1, resourceResponseDTO.getStatus());

        // Verify the skills list
        assertNotNull(resourceResponseDTO.getSkills());
        assertEquals(2, resourceResponseDTO.getSkills().size());
        assertEquals(1, resourceResponseDTO.getSkills().get(0).getSkillId());
        assertEquals(4, resourceResponseDTO.getSkills().get(0).getExperience());
        assertEquals(2, resourceResponseDTO.getSkills().get(1).getSkillId());
        assertEquals(3, resourceResponseDTO.getSkills().get(1).getExperience());
    }
}
