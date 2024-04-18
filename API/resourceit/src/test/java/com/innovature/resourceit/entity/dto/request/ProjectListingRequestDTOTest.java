package com.innovature.resourceit.entity.dto.request;

import com.innovature.resourceit.entity.dto.requestdto.ProjectListingRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class ProjectListingRequestDTOTest {

    @Test
    void testProjectListingRequestDTO() {
        ProjectListingRequestDTO requestDTO = new ProjectListingRequestDTO();
        requestDTO.setPageNumber("1");
        requestDTO.setPageSize("10");
        requestDTO.setProjectName("Sample Project");
        requestDTO.setStartDate("2023-01-01");
        requestDTO.setEndDate("2023-12-31");
        requestDTO.setProjectState(Arrays.asList("Active", "Pending"));
        requestDTO.setManagerId(Arrays.asList("1", "2"));
        requestDTO.setProjectType(Arrays.asList("Software", "Hardware"));
        requestDTO.setSortOrder(true);
        requestDTO.setSortKey("projectName");

        assertEquals("1", requestDTO.getPageNumber());
        assertEquals("10", requestDTO.getPageSize());
        assertEquals("Sample Project", requestDTO.getProjectName());
        assertEquals("2023-01-01", requestDTO.getStartDate());
        assertEquals("2023-12-31", requestDTO.getEndDate());
        assertEquals(Arrays.asList("Active", "Pending"), requestDTO.getProjectState());
        assertEquals(Arrays.asList("1", "2"), requestDTO.getManagerId());
        assertEquals(Arrays.asList("Software", "Hardware"), requestDTO.getProjectType());
        assertTrue(requestDTO.getSortOrder());
        assertEquals("projectName", requestDTO.getSortKey());
    }
}
