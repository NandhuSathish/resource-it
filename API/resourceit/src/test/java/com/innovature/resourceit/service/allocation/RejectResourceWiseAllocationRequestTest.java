package com.innovature.resourceit.service.allocation;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceAllocationRequest;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.dto.requestdto.RejectionRequestDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ResourceAllocationRequestRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;
import com.innovature.resourceit.util.EmailUtils;
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
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = RejectResourceWiseAllocationRequestTest.class)
class RejectResourceWiseAllocationRequestTest {

    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private EmailUtils emailUtils;
    @Mock
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AllocationServiceImpli allocationService;

    private MockedStatic<SecurityUtil> mockedSecurityUtil;

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
    void testRejectResourceWiseAllocationRequest() {
        // Mock the required objects
        Integer requestId = 123;
        Resource currentUser = createMockResource(Resource.Roles.HOD.getValue());
        RejectionRequestDTO rejectionRequestDTO = createMockRejectionRequestDTO();

        ResourceAllocationRequest request = createMockAllocationRequest(requestId);
        request.setRequestedBy(currentUser);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), anyList(), anyByte())).thenReturn(request);

        // Test the method
        allocationService.rejectResourceWiseAllocationRequest(requestId, rejectionRequestDTO);

        // Verify the method is called and the request is saved
        verify(resourceAllocationRequestRepository, times(1)).save(request);
    }

    @Test
    void testRejectResourceWiseAllocationRequestWithAllocation() {
        // Mock the required objects
        Integer requestId = 123;
        Resource currentUser = createMockResource(Resource.Roles.HOD.getValue());
        RejectionRequestDTO rejectionRequestDTO = createMockRejectionRequestDTO();

        ResourceAllocationRequest request = createMockAllocationRequestModified(requestId);
        request.setRequestedBy(currentUser);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), anyList(), anyByte())).thenReturn(request);
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(new Allocation(1)));
        // Test the method
        allocationService.rejectResourceWiseAllocationRequest(requestId, rejectionRequestDTO);

        // Verify the method is called and the request is saved
        verify(resourceAllocationRequestRepository, times(1)).save(request);
    }

    @Test
    void testRejectResourceWiseAllocationRequestWithInvalidAllocation() {
        // Mock the required objects
        Integer requestId = 123;
        Resource currentUser = createMockResource(Resource.Roles.HOD.getValue());
        RejectionRequestDTO rejectionRequestDTO = createMockRejectionRequestDTO();

        ResourceAllocationRequest request = createMockAllocationRequestModified(requestId);
        request.setRequestedBy(currentUser);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), anyList(), anyByte())).thenReturn(request);
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.empty());
        // Test the method
        assertThrows(BadRequestException.class, () -> allocationService.rejectResourceWiseAllocationRequest(requestId, rejectionRequestDTO));

    }

    @Test
    void testRejectResourceWiseAllocationRequestWithInvalidRequest() {
        // Mock the required objects
        Integer requestId = 123;
        Resource currentUser = createMockResource(Resource.Roles.HOD.getValue());
        RejectionRequestDTO rejectionRequestDTO = createMockRejectionRequestDTO();

        ResourceAllocationRequest request = createMockAllocationRequestModified(requestId);
        request.setRequestedBy(currentUser);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), anyList(), anyByte())).thenReturn(null);
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.empty());
        // Test the method
        assertThrows(BadRequestException.class, () -> allocationService.rejectResourceWiseAllocationRequest(requestId, rejectionRequestDTO));

    }

    @Test
    void testRejectResourceWiseAllocationRequestWithInvalidUser() {
        // Mock the required objects
        Integer requestId = 123;
        Resource currentUser = createMockResource(Resource.Roles.HOD.getValue());
        RejectionRequestDTO rejectionRequestDTO = createMockRejectionRequestDTO();

        ResourceAllocationRequest request = createMockAllocationRequestModified(requestId);
        request.setRequestedBy(currentUser);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.empty());
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), anyList(), anyByte())).thenReturn(null);
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.empty());
        // Test the method
        assertThrows(BadRequestException.class, () -> allocationService.rejectResourceWiseAllocationRequest(requestId, rejectionRequestDTO));

    }

    @Test
    void testRejectResourceWiseAllocationRequestWithInvalidAccess() {
        // Mock the required objects
        Integer requestId = 123;
        Resource currentUser = createMockResource(Resource.Roles.PM.getValue());
        RejectionRequestDTO rejectionRequestDTO = createMockRejectionRequestDTO();

        ResourceAllocationRequest request = createMockAllocationRequestModified(requestId);
        request.setRequestedBy(currentUser);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), anyList(), anyByte())).thenReturn(request);
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.empty());
        // Test the method
        assertThrows(BadRequestException.class, () -> allocationService.rejectResourceWiseAllocationRequest(requestId, rejectionRequestDTO));

    }

    // Other test methods...

    private Resource createMockResource(String role) {
        // Create and return a mock Resource with the specified role
        Resource resource = new Resource(1);
        resource.setRole(new Role(role));
        return resource;
    }

    private RejectionRequestDTO createMockRejectionRequestDTO() {
        // Create and return a mock RejectionRequestDTO
        RejectionRequestDTO rejectionRequestDTO = new RejectionRequestDTO();
        rejectionRequestDTO.setMessage("Rejection reason");
        return rejectionRequestDTO;
    }

    private ResourceAllocationRequest createMockAllocationRequest(Integer requestId) {
        // Create and return a mock ResourceAllocationRequest
        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setId(requestId);
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value);
        return request;
    }

    private ResourceAllocationRequest createMockAllocationRequestModified(Integer requestId) {
        // Create and return a mock ResourceAllocationRequest
        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setId(requestId);
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value);
        Allocation allocation = new Allocation(1);
        request.setAllocation(allocation);
        return request;
    }
}
