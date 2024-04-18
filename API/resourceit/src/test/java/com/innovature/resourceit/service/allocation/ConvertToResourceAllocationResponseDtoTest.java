package com.innovature.resourceit.service.allocation;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.dto.responsedto.ResourceAllocationResponseDTO;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ConvertToResourceAllocationResponseDtoTest {

    @InjectMocks
    private AllocationServiceImpli allocationService;

    @Mock
    private ResourceAllocationRequest resourceAllocationRequest;
    @Mock
    private CommonFunctions commonFunctions;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertToResourceAllocationResponseDto() throws ParseException {
        // Mock the required objects
        when(resourceAllocationRequest.getProject()).thenReturn(createMockProject());
        when(resourceAllocationRequest.getResource()).thenReturn(createMockResource());
        when(resourceAllocationRequest.getRequestedBy()).thenReturn(createMockRequestedBy());
        when(resourceAllocationRequest.getStartDate()).thenReturn(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2023"));
        when(resourceAllocationRequest.getEndDate()).thenReturn(new SimpleDateFormat("dd-MM-yyyy").parse("31-01-2023"));
        when(resourceAllocationRequest.getCreatedDate()).thenReturn(new SimpleDateFormat("dd-MM-yyyy").parse("01-12-2022"));
        when(resourceAllocationRequest.getUpdatedDate()).thenReturn(new SimpleDateFormat("dd-MM-yyyy").parse("05-12-2022"));
        when(resourceAllocationRequest.getRejectionReason()).thenReturn("Rejection Reason");
        when(resourceAllocationRequest.getApprovalFlow()).thenReturn((byte) 1);
        when(commonFunctions.calculateConflictDays(1, createMockAllocation(), new Date(), new Date())).thenReturn(1);


        ResourceAllocationResponseDTO responseDTO = allocationService.convertToResourceAllocationResponseDto(resourceAllocationRequest);

        // Verify the result
        assertEquals(1, responseDTO.getProjectId());
        assertEquals("ProjectCode", responseDTO.getProjectCode());
        assertEquals("ProjectName", responseDTO.getProjectName());
        assertEquals(1, responseDTO.getResourceId());
        assertEquals("ResourceName", responseDTO.getResourceName());
        assertEquals("123", responseDTO.getResourceEmployeeId());
        assertEquals(456, responseDTO.getDepartmentId());
        assertEquals("DepartmentName", responseDTO.getDepartmentName());
        assertEquals("789", responseDTO.getRequestedByEmployeeId());
        assertEquals("RequestedByName", responseDTO.getRequestedByName());
        assertEquals("01-01-2023", responseDTO.getStartDate());
        assertEquals("31-01-2023", responseDTO.getEndDate());
        assertEquals("Rejection Reason", responseDTO.getRejectionReason());
        assertEquals((byte) 1, responseDTO.getApprovalFlow());
        assertEquals("01-12-2022", responseDTO.getCreatedDate());
        assertEquals("05-12-2022", responseDTO.getUpdatedDate());
    }

    private Project createMockProject() {
        Project project = new Project();
        project.setProjectId(1);
        project.setProjectCode("ProjectCode");
        project.setName("ProjectName");
        return project;
    }

    private Resource createMockResource() {
        Resource resource = new Resource();
        resource.setId(1);
        resource.setName("ResourceName");
        resource.setEmployeeId(123);
        resource.setDepartment(createMockDepartment());
        return resource;
    }

    private Department createMockDepartment() {
        Department department = new Department();
        department.setDepartmentId(456);
        department.setName("DepartmentName");
        return department;
    }

    private Resource createMockRequestedBy() {
        Resource requestedBy = new Resource();
        requestedBy.setEmployeeId(789);
        requestedBy.setName("RequestedByName");
        return requestedBy;
    }

    private Allocation createMockAllocation() {
        Allocation allocation = new Allocation();
        allocation.setId(1);
        return allocation;
    }
}
