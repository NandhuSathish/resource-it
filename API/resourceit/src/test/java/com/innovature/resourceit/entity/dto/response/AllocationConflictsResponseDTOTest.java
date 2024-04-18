package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.responsedto.AllocationConflictsByResourceResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.AllocationConflictsResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AllocationConflictsResponseDTOTest {

    @Test
    void testAllocationConflictsResponseDTO() {
        AllocationConflictsResponseDTO responseDTO = new AllocationConflictsResponseDTO();
        responseDTO.setResourceId(1);

        AllocationConflictsByResourceResponseDTO conflict1 = new AllocationConflictsByResourceResponseDTO();
        conflict1.setProjectCode("P001");
        conflict1.setProjectName("Project 1");
        conflict1.setStartDate(new Date().toString());
        conflict1.setEndDate(new Date().toString());

        AllocationConflictsByResourceResponseDTO conflict2 = new AllocationConflictsByResourceResponseDTO();
        conflict2.setProjectCode("P002");
        conflict2.setProjectName("Project 2");
        conflict2.setStartDate(new Date().toString());
        conflict2.setEndDate(new Date().toString());

        List<AllocationConflictsByResourceResponseDTO> conflicts = Arrays.asList(conflict1, conflict2);
        responseDTO.setConflicts(conflicts);

        assertNotNull(responseDTO, "AllocationConflictsResponseDTO should not be null");
        assertEquals(1, responseDTO.getResourceId(), "ResourceId should be 1");
        assertEquals(conflicts, responseDTO.getConflicts(), "Conflicts should match the provided list");
    }

    @Test
    void testAllocationConflictsByResourceResponseDTO() {
        AllocationConflictsByResourceResponseDTO responseDTO = new AllocationConflictsByResourceResponseDTO();
        responseDTO.setProjectCode("P001");
        responseDTO.setProjectName("Project 1");
        responseDTO.setStartDate(new Date().toString());
        responseDTO.setEndDate(new Date().toString());

        assertNotNull(responseDTO, "AllocationConflictsByResourceResponseDTO should not be null");
        assertEquals("P001", responseDTO.getProjectCode(), "ProjectCode should be P001");
        assertEquals("Project 1", responseDTO.getProjectName(), "ProjectName should be Project 1");
        assertNotNull(responseDTO.getStartDate(), "StartDate should not be null");
        assertNotNull(responseDTO.getEndDate(), "EndDate should not be null");
    }
}
