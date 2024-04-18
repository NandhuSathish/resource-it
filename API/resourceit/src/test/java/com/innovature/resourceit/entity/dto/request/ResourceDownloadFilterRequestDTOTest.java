package com.innovature.resourceit.entity.dto.request;

import com.innovature.resourceit.entity.dto.requestdto.ResourceDownloadFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterSkillAndExperienceRequestDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceDownloadFilterRequestDTOTest {

    @Test
    void testGettersAndSetters() {
        ResourceDownloadFilterRequestDTO requestDTO = new ResourceDownloadFilterRequestDTO();

        // Set values
        requestDTO.setName("John Doe");
        requestDTO.setEmployeeId("123");
        requestDTO.setDepartmentIds(Arrays.asList("dept1", "dept2"));
        requestDTO.setProjectIds(Arrays.asList("proj1", "proj2"));
        requestDTO.setRoleIds(Arrays.asList("role1", "role2"));
        requestDTO.setLowerExperience("3");
        requestDTO.setHighExperience("8");
        ResourceFilterSkillAndExperienceRequestDTO skillAndExperienceRequestDTO = new ResourceFilterSkillAndExperienceRequestDTO();
        skillAndExperienceRequestDTO.setSkillId("1");
        skillAndExperienceRequestDTO.setSkillMaxValue("2");
        skillAndExperienceRequestDTO.setSkillMinValue("3");
        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperienceList = new ArrayList<>();
        skillAndExperienceList.add(skillAndExperienceRequestDTO);

        requestDTO.setSkillAndExperiences(skillAndExperienceList);
        requestDTO.setStatus("Active");
        requestDTO.setAllocationStatus(Arrays.asList("status1", "status2"));

        // Verify values using getters
        assertEquals("John Doe", requestDTO.getName());
        assertEquals("123", requestDTO.getEmployeeId());
        assertEquals(Arrays.asList("dept1", "dept2"), requestDTO.getDepartmentIds());
        assertEquals(Arrays.asList("proj1", "proj2"), requestDTO.getProjectIds());
        assertEquals(Arrays.asList("role1", "role2"), requestDTO.getRoleIds());
        assertEquals("3", requestDTO.getLowerExperience());
        assertEquals("8", requestDTO.getHighExperience());
        assertEquals(requestDTO.getSkillAndExperiences(), requestDTO.getSkillAndExperiences());
        assertEquals("Active", requestDTO.getStatus());
        assertEquals(Arrays.asList("status1", "status2"), requestDTO.getAllocationStatus());
    }

}
