package com.innovature.resourceit.entity.dto.request;

import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterSkillAndExperienceRequestDTO;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceFilterRequestDTOTest {

    @Test
    void testGettersAndSetters() {
        ResourceFilterRequestDTO requestDTO = new ResourceFilterRequestDTO();

        // Set values
        requestDTO.setName("John Doe");
        requestDTO.setEmployeeId("123");
        requestDTO.setDepartmentIds(Arrays.asList("dept1", "dept2"));
        requestDTO.setProjectIds(Arrays.asList("proj1", "proj2"));
        requestDTO.setRoleIds(Arrays.asList("role1", "role2"));
        requestDTO.setLowerExperience("3");
        requestDTO.setHighExperience("8");
        ResourceFilterSkillAndExperienceRequestDTO skillAndExperience = new ResourceFilterSkillAndExperienceRequestDTO();
        skillAndExperience.setSkillId("1");
        skillAndExperience.setSkillMinValue("2");
        skillAndExperience.setSkillMaxValue("5");

        List<ResourceFilterSkillAndExperienceRequestDTO> expectedSkillAndExperienceList = new ArrayList<>();
        expectedSkillAndExperienceList.add(skillAndExperience);
        requestDTO.setSkillAndExperiences(expectedSkillAndExperienceList);
        requestDTO.setStatus("Active");
        requestDTO.setAllocationStatus(Arrays.asList("status1", "status2"));
        requestDTO.setPageNumber("1");
        requestDTO.setPageSize("10");
        requestDTO.setSortOrder(Boolean.FALSE);
        requestDTO.setSortKey("joiningDate");

        // Verify values using getters
        assertEquals("John Doe", requestDTO.getName());
        assertEquals("123", requestDTO.getEmployeeId());
        assertEquals(Arrays.asList("dept1", "dept2"), requestDTO.getDepartmentIds());
        assertEquals(Arrays.asList("proj1", "proj2"), requestDTO.getProjectIds());
        assertEquals(Arrays.asList("role1", "role2"), requestDTO.getRoleIds());
        assertEquals("3", requestDTO.getLowerExperience());
        assertEquals("8", requestDTO.getHighExperience());
        assertThat(expectedSkillAndExperienceList)
                .extracting(ResourceFilterSkillAndExperienceRequestDTO::getSkillId)
                .containsExactly("1");

        assertThat(expectedSkillAndExperienceList)
                .extracting(ResourceFilterSkillAndExperienceRequestDTO::getSkillMinValue)
                .containsExactly("2");

        assertThat(expectedSkillAndExperienceList)
                .extracting(ResourceFilterSkillAndExperienceRequestDTO::getSkillMaxValue)
                .containsExactly("5");

        assertEquals("Active", requestDTO.getStatus());
        assertEquals(Arrays.asList("status1", "status2"), requestDTO.getAllocationStatus());
        assertEquals("1", requestDTO.getPageNumber());
        assertEquals("10", requestDTO.getPageSize());
        assertEquals("joiningDate", requestDTO.getSortKey());
        assertEquals(Boolean.FALSE, requestDTO.getSortOrder());

    }

}
