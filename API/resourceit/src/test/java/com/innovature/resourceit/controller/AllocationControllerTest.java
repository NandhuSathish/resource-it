/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.requestdto.*;
import com.innovature.resourceit.entity.dto.responsedto.AllocationConflictsByResourceResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.AllocationRequestResourceFilterResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.AllocationResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceAllocationResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceSkillWiseAllocationResponseListDTO;
import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
import com.innovature.resourceit.service.AllocationService;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author abdul.fahad
 */
class AllocationControllerTest {

    @Mock
    AllocationService allocationService;

    @InjectMocks
    AllocationController allocationController;

    @Mock
    MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void requestResourceWithSkillValid() throws Exception {
        ResourceSkillWiseAllocationRequestDTO requestDTO = new ResourceSkillWiseAllocationRequestDTO();
        requestDTO.setCount("2");
        requestDTO.setDepartmentId("1");
        requestDTO.setProjectId("1");
        requestDTO.setEndDate("23-01-2023");
        requestDTO.setStartDate("10-01-2023");
        requestDTO.setExperience("3");
        ResourceFilterSkillAndExperienceRequestDTO o = new ResourceFilterSkillAndExperienceRequestDTO();
        o.setSkillId("1");
        o.setSkillMinValue("2");
        o.setSkillMaxValue("5");
        List<ResourceFilterSkillAndExperienceRequestDTO> lists = Arrays.asList(o);
        requestDTO.setSkillExperienceRequestDTO(lists);
        ResponseEntity<Object> responseEntity = allocationController.requestResourceWithSkill(requestDTO);
        assertEquals(200, responseEntity.getStatusCode().value());
        verify(allocationService, times(1)).saveSkillWiseResourceRequest(requestDTO);
    }

    @Test
    void testListRequestResourceWithSkill() throws ParseException {

        boolean isRequestList = true;

        ResourceSkillWiseAllocationRequestListDTO requestListDTO = createValidRequestListDTO();
        PagedResponseDTO<ResourceSkillWiseAllocationResponseListDTO> mockResponseDTO = createMockPagedResponseDTO();

        when(allocationService.listSkillWiseResourceRequest(requestListDTO, isRequestList)).thenReturn(mockResponseDTO);

        ResponseEntity<Object> responseEntity = allocationController.listRequestResourceWithSkill(requestListDTO, isRequestList);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponseDTO, responseEntity.getBody());

        verify(allocationService, times(1)).listSkillWiseResourceRequest(requestListDTO, isRequestList);
    }

    private ResourceSkillWiseAllocationRequestListDTO createValidRequestListDTO() {
        ResourceSkillWiseAllocationRequestListDTO requestListDTO = new ResourceSkillWiseAllocationRequestListDTO();
        requestListDTO.setProjectIds(Arrays.asList("1"));
        requestListDTO.setApprovalStatus(Arrays.asList("1"));
        return requestListDTO;
    }

    private PagedResponseDTO<ResourceSkillWiseAllocationResponseListDTO> createMockPagedResponseDTO() {
        ResourceSkillWiseAllocationResponseListDTO res = new ResourceSkillWiseAllocationResponseListDTO();
        res.setProjectId(1);
        res.setProjectName("ceates");
        PagedResponseDTO<ResourceSkillWiseAllocationResponseListDTO> mockResponseDTO = new PagedResponseDTO<>();
        mockResponseDTO.setItems(Arrays.asList(res));
        return mockResponseDTO;
    }

    @Test
    void testAddResourceWiseAllocationRequest() {
        ResourceAllocationRequestDTO allocationRequestDTO = new ResourceAllocationRequestDTO();
        allocationRequestDTO.setAllocationId(1);

        List<ResourceAllocationRequestDTO> allocationRequestDTOs = Arrays.asList(allocationRequestDTO);

        when(messageSource.getMessage(eq("ALLOCATION_REQUEST_ADDED"), eq(null), any()))
                .thenReturn("2103-Request added for allocation.");

        ResponseEntity<Object> responseEntity = allocationController.addResourceWiseAllocationRequest(allocationRequestDTOs);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponseDTO s = (SuccessResponseDTO) responseEntity.getBody();
        assertEquals("2103", s.getSuccessCode());

        verify(allocationService, times(1)).addResourceWiseAllocationRequest(allocationRequestDTOs);

    }

    @Test
    void testListProjectRequests() {
        // Mock data
        Set<Byte> approvalStatus = new HashSet<>();
        Set<Integer> projectIds = new HashSet<>();
        Set<Integer> departmentIds = new HashSet<>();
        Set<Integer> requestedBys = new HashSet<>();
        String searchKey = "test";
        int page = 0;
        int size = 20;
        Boolean sortDirection = true;
        Boolean isRequestList = true;
        // Mock the service method
        when(allocationService.listResourceAllocationRequests(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(createMockPage());

        // Call the controller method
        ResponseEntity<PagedResponseDTO<ResourceAllocationResponseDTO>> responseEntity
                = allocationController.listProjectRequests(page, size, approvalStatus, departmentIds, projectIds, requestedBys, searchKey, sortDirection, isRequestList);

        // Assert the response status
        assert (responseEntity.getStatusCode()).equals(HttpStatus.OK);

    }

    private Page<ResourceAllocationResponseDTO> createMockPage() {
        // Create and return a mock Page object
        return new PageImpl<>(Collections.emptyList());
    }

    @Test
    void testDeleteSkillWiseAllocationRequests() {
        String id = "1";
        ResponseEntity<Object> responseEntity = allocationController.deleteRequestResourceWithSkill(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(allocationService, times(1)).deleteRequestResourceWithSkill(id);
    }

    @Test
    void testResourceWiseAllocationEditRequest() {
        ResourceAllocationRequestDTO requestDTO = new ResourceAllocationRequestDTO();
        when(messageSource.getMessage(eq("ALLOCATION_EDIT_REQUEST_ADDED"), eq(null), any()))
                .thenReturn("2104-Request added for allocation edit.");

        ResponseEntity<Object> responseEntity = allocationController.resourceWiseAllocationEditRequest(requestDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertEquals("2104", successResponseDTO.getSuccessCode());

        verify(allocationService, times(1)).resourceWiseAllocationEditRequest(requestDTO);
    }

    @Test
    void testApproveResourceWiseAllocationRequest() {
        Integer requestId = 1;
        when(messageSource.getMessage(eq("ALLOCATION_REQUEST_APPROVED"), eq(null), any()))
                .thenReturn("2102-Request approved.");

        ResponseEntity<Object> responseEntity = allocationController.approveResourceWiseAllocationRequest(requestId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertEquals("2102", successResponseDTO.getSuccessCode());

        verify(allocationService, times(1)).approveResourceWiseAllocationRequest(requestId);
    }

    @Test
    void testRejectResourceWiseAllocationRequest() {
        Integer requestId = 1;
        RejectionRequestDTO rejectionRequestDTO = new RejectionRequestDTO();
        when(messageSource.getMessage(eq("ALLOCATION_REQUEST_REJECTED"), eq(null), any()))
                .thenReturn("2105-Request rejected.");

        ResponseEntity<Object> responseEntity = allocationController.rejectResourceWiseAllocationRequest(requestId, rejectionRequestDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertEquals("2105", successResponseDTO.getSuccessCode());

        verify(allocationService, times(1)).rejectResourceWiseAllocationRequest(requestId, rejectionRequestDTO);
    }

    @Test
    void testApproveByHODSkillWiseAllocationRequest() {
        Integer requestId = 1;
        when(messageSource.getMessage(eq("ALLOCATION_REQUEST_APPROVED"), eq(null), any()))
                .thenReturn("2102-Request approved.");

        ResponseEntity<Object> responseEntity = allocationController.approveByHODSkillWiseAllocationRequest(requestId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertEquals("2102", successResponseDTO.getSuccessCode());

        verify(allocationService, times(1)).approveByHODSkillWiseAllocationRequest(requestId);
    }

    @Test
    void testApproveByHRSkillWiseAllocationRequest() {
        Integer requestId = 1;
        List<ResourceAllocationRequestDTO> dtoList = Collections.emptyList();
        when(messageSource.getMessage(eq("ALLOCATION_REQUEST_APPROVED"), eq(null), any()))
                .thenReturn("2102-Request approved.");

        ResponseEntity<Object> responseEntity = allocationController.approveByHRSkillWiseAllocationRequest(requestId, dtoList);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertEquals("2102", successResponseDTO.getSuccessCode());

        verify(allocationService, times(1)).approveByHRSkillWiseAllocationRequest(requestId, dtoList);
    }

    @Test
    void testRejectSkillWiseAllocationRequest() {
        Integer requestId = 1;
        RejectionRequestDTO rejectionRequestDTO = new RejectionRequestDTO();
        when(messageSource.getMessage(eq("ALLOCATION_REQUEST_REJECTED"), eq(null), any()))
                .thenReturn("2105-Request rejected.");

        ResponseEntity<Object> responseEntity = allocationController.rejectSkillWiseAllocationRequest(requestId, rejectionRequestDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertEquals("2105", successResponseDTO.getSuccessCode());

        verify(allocationService, times(1)).rejectSkillWiseAllocationRequest(requestId, rejectionRequestDTO);
    }

    @Test
    void testGetAllocationConflictList() {
        List<Integer> resourceIdList = Collections.singletonList(1);
        Date allocationStartDate = new Date(789456);
        Date allocationEndDate = new Date(809456);

        when(allocationService.getAllocationConflictList(resourceIdList, allocationStartDate, allocationEndDate, null))
                .thenReturn(Collections.emptyList());

        ResponseEntity<Object> responseEntity = allocationController.getAllocationConflict(resourceIdList, allocationStartDate, allocationEndDate, null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.emptyList(), responseEntity.getBody());

        verify(allocationService, times(1)).getAllocationConflictList(resourceIdList, allocationStartDate, allocationEndDate, null);
    }

    @Test
    void testGetAllocationDetailsByProjectId() {
        int projectId = 1;
        String page = "1";
        String size = "2";
        AllocationResponseDTO allocationResponseDTO = new AllocationResponseDTO();
        allocationResponseDTO.setId(1);
        PagedResponseDTO<AllocationResponseDTO> allocationResponseDTOs = createNewAllocationResponseDTO();


        when(allocationService.getAllocationListByProjectId(projectId, page, size,false)).thenReturn(allocationResponseDTOs);

        ResponseEntity<Object> responseEntity = allocationController.getAllocationDetailsByProjectId(projectId, page, size,false);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    private PagedResponseDTO<AllocationResponseDTO> createNewAllocationResponseDTO() {
        AllocationResponseDTO responseDTO = new AllocationResponseDTO();
        responseDTO.setId(1);

        PagedResponseDTO<AllocationResponseDTO> pagedResponseDTO
                = new PagedResponseDTO<>(Collections.singletonList(responseDTO), 1, 1, 0);

        return pagedResponseDTO;
    }

    @Test
    void testDeleteResourceWiseAllocationRequest() {
        int requestId = 1;
        when(messageSource.getMessage(eq("ALLOCATION_REQUEST_DELETED"), eq(null), any()))
                .thenReturn("2113-Allocation request deleted successfully.");
        ResponseEntity<Object> responseEntity = allocationController.deleteResourceWiseAllocationRequest(requestId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertEquals("2113", successResponseDTO.getSuccessCode());
        verify(allocationService, times(1)).deleteResourceWiseAllocationRequest(requestId);

    }

    @Test
    void testlistResourcesForAllocation() {
        PagedResponseDTO<AllocationRequestResourceFilterResponseDTO> mockResponse = createMockResponse();
        when(allocationService.listResourcesForAllocation(any())).thenReturn(mockResponse);

        // Create a request object for testing
        AllocationRequestResourceFilterRequestDTO requestDTO = createMockRequest();

        // Call the controller method
        ResponseEntity<Object> responseEntity = allocationController.listResourcesForAllocation(requestDTO);

        // Verify that the service method was called with the correct argument
        verify(allocationService, times(1)).listResourcesForAllocation(requestDTO);

        // Verify the response entity status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());

    }

    private AllocationRequestResourceFilterRequestDTO createMockRequest() {
        return  new AllocationRequestResourceFilterRequestDTO();
    }

    private PagedResponseDTO<AllocationRequestResourceFilterResponseDTO> createMockResponse() {
        AllocationRequestResourceFilterResponseDTO responseDTO = new AllocationRequestResourceFilterResponseDTO();
        responseDTO.setId(1);

        PagedResponseDTO<AllocationRequestResourceFilterResponseDTO> pagedResponseDTO
                = new PagedResponseDTO<>(Collections.singletonList(responseDTO), 1, 1, 0);

        return pagedResponseDTO;
    }

    @Test
    void testSkillWiseAllocationRequestById() {
        String id = "8";

        ResourceSkillWiseAllocationResponseListDTO allocationResponseListDTO = new ResourceSkillWiseAllocationResponseListDTO();
        allocationResponseListDTO.setId(8);

        when(allocationService.getRequestResourceWithSkill(id)).thenReturn(allocationResponseListDTO);

        ResponseEntity<Object> responseEntity = allocationController.getRequestResourceWithSkill(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ResourceSkillWiseAllocationResponseListDTO responseListDTO = (ResourceSkillWiseAllocationResponseListDTO) responseEntity.getBody();

        assertEquals(allocationResponseListDTO.getId(), responseListDTO.getId());
    }

    @Test
    void testDeleteAllocation() {
        // Mock data
        Integer allocationId = 1;

        // Mock the service method
        when(messageSource.getMessage(eq("ALLOCATION_DELETED"), eq(null), any()))
                .thenReturn("2114-Allocation deleted successfully.");

        // Call the controller method
        ResponseEntity<Object> responseEntity = allocationController.deleteAllocation(allocationId);

        // Assert the response status
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Assert the response body
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertEquals("2114", successResponseDTO.getSuccessCode());

        // Verify that the service method was called with the correct argument
        verify(allocationService, times(1)).deleteAllocation(allocationId);
    }


}
