package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.responsedto.ResourceAllocationResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceAllocationResponseDTOTest {

    @Test
    void testResourceAllocationResponseDTO() {
        ResourceAllocationResponseDTO responseDTO = new ResourceAllocationResponseDTO();
        responseDTO.setProjectName("Project 1");
        responseDTO.setProjectCode("P001");
        responseDTO.setResourceName("John Doe");
        responseDTO.setResourceEmployeeId("EMP001");
        responseDTO.setDepartmentId(1);
        responseDTO.setDepartmentName("IT Department");
        responseDTO.setRequestedByName("Manager");
        responseDTO.setRequestedByEmployeeId("EMP002");
        responseDTO.setStartDate("2023-01-01");
        responseDTO.setEndDate("2023-01-31");
        responseDTO.setRejectionReason("Not approved");
        responseDTO.setApprovalFlow((byte) 2);
        responseDTO.setCreatedDate("2023-01-01 12:00:00");
        responseDTO.setUpdatedDate("2023-01-02 14:30:00");

        assertNotNull(responseDTO, "ResourceAllocationResponseDTO should not be null");
        assertEquals("Project 1", responseDTO.getProjectName(), "ProjectName should be Project 1");
        assertEquals("P001", responseDTO.getProjectCode(), "ProjectCode should be P001");
        assertEquals("John Doe", responseDTO.getResourceName(), "ResourceName should be John Doe");
        assertEquals("EMP001", responseDTO.getResourceEmployeeId(), "ResourceEmployeeId should be EMP001");
        assertEquals(1, responseDTO.getDepartmentId(), "DepartmentId should be 1");
        assertEquals("IT Department", responseDTO.getDepartmentName(), "DepartmentName should be IT Department");
        assertEquals("Manager", responseDTO.getRequestedByName(), "RequestedByName should be Manager");
        assertEquals("EMP002", responseDTO.getRequestedByEmployeeId(), "RequestedByEmployeeId should be EMP002");
        assertEquals("2023-01-01", responseDTO.getStartDate(), "StartDate should be 2023-01-01");
        assertEquals("2023-01-31", responseDTO.getEndDate(), "EndDate should be 2023-01-31");
        assertEquals("Not approved", responseDTO.getRejectionReason(), "RejectionReason should be Not approved");
        assertEquals((byte) 2, responseDTO.getApprovalFlow(), "ApprovalFlow should be 2");
        assertEquals("2023-01-01 12:00:00", responseDTO.getCreatedDate(), "CreatedDate should be 2023-01-01 12:00:00");
        assertEquals("2023-01-02 14:30:00", responseDTO.getUpdatedDate(), "UpdatedDate should be 2023-01-02 14:30:00");
    }
}
