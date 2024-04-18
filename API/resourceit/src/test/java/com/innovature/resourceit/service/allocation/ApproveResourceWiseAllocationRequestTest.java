package com.innovature.resourceit.service.allocation;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceAllocationRequestRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
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

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = ApproveResourceWiseAllocationRequestTest.class)
class ApproveResourceWiseAllocationRequestTest {

    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private EmailUtils emailUtils;

    @Mock
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private CommonFunctions commonFunctions;

    @InjectMocks
    private AllocationServiceImpli allocationService; // Mock the service

    private MockedStatic<SecurityUtil> mockedSecurityUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
        when(projectRepository.save(new Project())).thenReturn(new Project(1));
    }

    @AfterEach
    void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void testApproveResourceWiseAllocationRequest_HOD_Approval() {
        Resource currentUser = new Resource();
        currentUser.setEmail("test@gmail.com");
        currentUser.setStatus(Resource.Status.ACTIVE.value);
        currentUser.setRole(new Role(Resource.Roles.HOD.getValue()));

        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setId(1);
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value);

        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(eq(1), anyList(), anyByte())).thenReturn(request);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Test message");

        allocationService.approveResourceWiseAllocationRequest(1);

        verify(resourceAllocationRequestRepository, times(1)).save(request);
    }

    @Test
    void testApproveResourceWiseAllocationRequest_HOD_Approval_ApprovalFlow_HR_Approved() {
        Resource currentUser = new Resource(1);
        currentUser.setEmail("test@gmail.com");
        currentUser.setStatus(Resource.Status.ACTIVE.value);
        currentUser.setRole(new Role(Resource.Roles.HOD.getValue()));
        Project project = new Project(1);
        project.setTeamSize(1);
        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setId(1);
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HR.value);
        request.setRequestedBy(currentUser);
        request.setProject(project);
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(eq(1), anyList(), anyByte())).thenReturn(request);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Test message");

        allocationService.approveResourceWiseAllocationRequest(1);

        verify(resourceAllocationRequestRepository, times(1)).save(request);
    }

    @Test
    void testApproveResourceWiseAllocationRequest_HOD_Approval_ApprovalFlow_HR_Approved_With_Allocation() {
        Resource currentUser = new Resource(1);
        currentUser.setEmail("test@gmail.com");
        currentUser.setStatus(Resource.Status.ACTIVE.value);
        currentUser.setRole(new Role(Resource.Roles.HOD.getValue()));
        Allocation allocation = new Allocation();
        allocation.setId(1);
        Project project = new Project(1);
        project.setTeamSize(1);
        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setId(1);
        request.setAllocation(allocation);
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HR.value);
        request.setRequestedBy(currentUser);
        request.setProject(project);
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(eq(1), anyList(), anyByte())).thenReturn(request);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Test message");
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(allocation));
        allocationService.approveResourceWiseAllocationRequest(1);

        verify(resourceAllocationRequestRepository, times(1)).save(request);
    }

    @Test
    void testApproveResourceWiseAllocationRequest_HR_Approval() {
        Resource currentUser = new Resource(1);
        currentUser.setEmail("test@gmail.com");
        currentUser.setStatus(Resource.Status.ACTIVE.value);
        currentUser.setRole(new Role(Resource.Roles.HR.getValue()));
        Project project = new Project(1);
        project.setTeamSize(1);
        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setId(1);
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);
        request.setRequestedBy(currentUser);
        request.setProject(project);
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(eq(1), anyList(), anyByte())).thenReturn(request);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Test message");

        allocationService.approveResourceWiseAllocationRequest(1);

        verify(resourceAllocationRequestRepository, times(1)).save(request);
    }

    @Test
    void testApproveResourceWiseAllocationRequest_HR_Approval_With_Allocation() {
        Resource currentUser = new Resource(1);
        currentUser.setEmail("test@gmail.com");
        currentUser.setStatus(Resource.Status.ACTIVE.value);
        currentUser.setRole(new Role(Resource.Roles.HR.getValue()));
        Project project = new Project(1);
        project.setTeamSize(1);
        ResourceAllocationRequest request = new ResourceAllocationRequest();
        request.setId(1);
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);
        Allocation allocation = new Allocation();
        allocation.setId(1);
        request.setAllocation(allocation);
        request.setRequestedBy(currentUser);
        request.setProject(project);

        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(eq(1), anyList(), anyByte())).thenReturn(request);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Test message");
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(allocation));

        allocationService.approveResourceWiseAllocationRequest(1);

        verify(resourceAllocationRequestRepository, times(1)).save(request);
    }

    @Test
    void testApproveResourceWiseAllocationRequest_RequestNotFound() {
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(new Resource()));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(eq(1), anyList(), anyByte())).thenReturn(null);

        assertThrows(BadRequestException.class, () -> allocationService.approveResourceWiseAllocationRequest(1));
    }

    @Test
    void testApproveResourceWiseAllocationRequest_AccessDenied() {
        Resource currentUser = new Resource();
        currentUser.setEmail("test@gmail.com");
        currentUser.setStatus(Resource.Status.ACTIVE.value);
        currentUser.setRole(new Role("ADMIN"));
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(currentUser));
        when(resourceAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(eq(1), anyList(), anyByte())).thenReturn(new ResourceAllocationRequest());

        assertThrows(BadRequestException.class, () -> allocationService.approveResourceWiseAllocationRequest(1));
    }
}
