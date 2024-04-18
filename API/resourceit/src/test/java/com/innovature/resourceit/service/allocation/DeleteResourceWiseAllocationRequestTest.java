package com.innovature.resourceit.service.allocation;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceAllocationRequest;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ResourceAllocationRequestRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = DeleteResourceWiseAllocationRequestTest.class)
class DeleteResourceWiseAllocationRequestTest {

    @InjectMocks
    private AllocationServiceImpli allocationService;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;
    @Mock
    private AllocationRepository allocationRepository;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void shouldDeleteResourceWiseAllocationRequest() {
        // Mocking the necessary dependencies and data
        Resource resource = new Resource(1);
        Role role = new Role("HOD");
        resource.setRole(role);
        ResourceAllocationRequest allocationRequest = new ResourceAllocationRequest();
        allocationRequest.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(resource));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any()))
                .thenReturn(allocationRequest);

        // Test the method
        allocationService.deleteResourceWiseAllocationRequest(1);

        // Verify the interactions and results
        verify(resourceRepository, times(1)).findByEmailAndStatus(anyString(), anyByte());
        verify(resourceAllocationRequestRepository, times(1))
                .findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any());
        verify(resourceAllocationRequestRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowBadRequestExceptionWhenResourceNotFound() {
        // Mocking the necessary dependencies and data
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.empty());

        // Test the method and expect an exception
        assertThrows(BadRequestException.class, () -> allocationService.deleteResourceWiseAllocationRequest(1));

        // Verify the interactions and results
        verify(resourceRepository, times(1)).findByEmailAndStatus(anyString(), anyByte());
        verify(resourceAllocationRequestRepository, never()).findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any());
        verify(resourceAllocationRequestRepository, never()).save(any());
    }

    @Test
    void shouldDeleteResourceWiseAllocationRequestByHOD() {
        // Mocking the necessary dependencies and data
        Resource currentUser = new Resource();
        Role role = new Role("HOD");
        currentUser.setRole(role);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any()))
                .thenReturn(request);

        // Test the method
        allocationService.deleteResourceWiseAllocationRequest(1);

        // Verify the interactions and results
        verify(resourceRepository, times(1)).findByEmailAndStatus(anyString(), anyByte());
        verify(resourceAllocationRequestRepository, times(1))
                .findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any());
        verify(resourceAllocationRequestRepository, times(1)).save(any());
    }

    @Test
    void shouldDeleteResourceWiseAllocationRequestByHR() {
        // Mocking the necessary dependencies and data
        Resource currentUser = new Resource();
        Role role = new Role("HR");
        currentUser.setRole(role);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HR.value);
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any()))
                .thenReturn(request);

        // Test the method
        allocationService.deleteResourceWiseAllocationRequest(1);

        // Verify the interactions and results
        verify(resourceRepository, times(1)).findByEmailAndStatus(anyString(), anyByte());
        verify(resourceAllocationRequestRepository, times(1))
                .findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any());
        verify(resourceAllocationRequestRepository, times(1)).save(any());
    }

    @Test
    void shouldDeleteResourceWiseAllocationRequestByPM() {
        Resource currentUser = new Resource();
        Role role = new Role("PROJECT MANAGER");
        currentUser.setRole(role);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value);
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any()))
                .thenReturn(request);

        // Test the method
        allocationService.deleteResourceWiseAllocationRequest(1);

        // Verify the interactions and results
        verify(resourceRepository, times(1)).findByEmailAndStatus(anyString(), anyByte());
        verify(resourceAllocationRequestRepository, times(1))
                .findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any());
        verify(resourceAllocationRequestRepository, times(1)).save(any());
    }

    @Test
    void shouldDeleteResourceWiseAllocationRequestByPMWithAllocation() {
        Resource currentUser = new Resource();
        Role role = new Role("PROJECT MANAGER");
        currentUser.setRole(role);
        Allocation allocation = new Allocation(1);

        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        when(allocationRepository
                .findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(allocation));
        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setAllocation(allocation);
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value);
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any()))
                .thenReturn(request);

        // Test the method
        allocationService.deleteResourceWiseAllocationRequest(1);

        // Verify the interactions and results
        verify(resourceRepository, times(1)).findByEmailAndStatus(anyString(), anyByte());
        verify(resourceAllocationRequestRepository, times(1))
                .findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any());
        verify(resourceAllocationRequestRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowBadRequestExceptionForAccessDenied() {
        Resource currentUser = new Resource();
        Role role = new Role("HOD");
        currentUser.setRole(role);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));

        // Test the method and expect an exception
        assertThrows(BadRequestException.class, () -> allocationService.deleteResourceWiseAllocationRequest(1));

        // Verify the interactions and results
        verify(resourceRepository, times(1)).findByEmailAndStatus(anyString(), anyByte());
        verify(resourceAllocationRequestRepository, times(1)).findByIdAndApprovalFlowNotInAndStatus(anyInt(), any(), any());
        verify(resourceAllocationRequestRepository, never()).save(any());
    }

}
