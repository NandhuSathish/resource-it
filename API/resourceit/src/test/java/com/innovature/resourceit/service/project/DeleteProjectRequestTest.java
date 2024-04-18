package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.ProjectRequest;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ProjectRequestRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.ProjectServiceImpli;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = DeleteProjectRequestTest.class)
class DeleteProjectRequestTest {

    private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String PROJECT_REQUEST_NOT_FOUND = "PROJECT_REQUEST_NOT_FOUND";
    private static final String ACCESS_DENIED = "ACCESS_DENIED";

    private MockedStatic<SecurityUtil> mockedSecurityUtil;

    @InjectMocks
    private ProjectServiceImpli service;

    @Mock
    private ProjectRequestRepository projectRequestRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(messageSource, resourceRepository, projectRequestRepository);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void testDeleteProjectRequestHR() {
        // Setup
        Integer requestId = 1;

        Resource mockCurrentUser = new Resource();
        mockCurrentUser.setEmail("test@gmail.com");
        mockCurrentUser.setStatus(Resource.Status.ACTIVE.value);
        mockCurrentUser.setRole(new Role(1, "HR", new Date(), new Date()));

        ProjectRequest mockRequest = new ProjectRequest();
        mockRequest.setStatus(ProjectRequest.statusValues.ACTIVE.value);
        mockRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.PENDING.value);

        when(resourceRepository.findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(mockCurrentUser));
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                eq(requestId),
                eq(ProjectRequest.statusValues.DELETED.value),
                eq(ProjectRequest.approvalStatusValues.PENDING.value)))
                .thenReturn(Optional.of(mockRequest));

        // Test
        service.deleteProjectRequest(requestId);

        // Verify
        verify(resourceRepository, times(1)).findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value));
        verify(projectRequestRepository, times(1)).findByProjectRequestIdAndStatusNotAndApprovalStatus(
                eq(requestId),
                eq(ProjectRequest.statusValues.DELETED.value),
                eq(ProjectRequest.approvalStatusValues.PENDING.value));
        verify(projectRequestRepository, times(1)).save(any());
    }

    @Test
    void testDeleteProjectRequestPM() {
        // Setup
        Integer requestId = 1;

        Resource mockCurrentUser = new Resource();
        mockCurrentUser.setEmail("test@gmail.com");
        mockCurrentUser.setStatus(Resource.Status.ACTIVE.value);
        mockCurrentUser.setRole(new Role(1, "PROJECT MANAGER", new Date(), new Date()));

        ProjectRequest mockRequest = new ProjectRequest();
        mockRequest.setStatus(ProjectRequest.statusValues.ACTIVE.value);
        mockRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.PENDING.value);

        when(resourceRepository.findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(mockCurrentUser));
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                eq(requestId),
                eq(ProjectRequest.statusValues.DELETED.value),
                eq(ProjectRequest.approvalStatusValues.PENDING.value)))
                .thenReturn(Optional.of(mockRequest));

        // Test
        service.deleteProjectRequest(requestId);

        // Verify
        verify(resourceRepository, times(1)).findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value));
        verify(projectRequestRepository, times(1)).findByProjectRequestIdAndStatusNotAndApprovalStatus(
                eq(requestId),
                eq(ProjectRequest.statusValues.DELETED.value),
                eq(ProjectRequest.approvalStatusValues.PENDING.value));
        verify(projectRequestRepository, times(1)).save(any());
    }

    @Test
    void testDeleteProjectRequestAccessDenied() {
        // Setup
        Integer requestId = 1;

        Resource mockCurrentUser = new Resource();
        mockCurrentUser.setEmail("test@gmail.com");
        mockCurrentUser.setStatus(Resource.Status.ACTIVE.value);
        mockCurrentUser.setRole(new Role(1, "SomeOtherRole", new Date(), new Date()));

        ProjectRequest mockRequest = new ProjectRequest();
        mockRequest.setStatus(ProjectRequest.statusValues.ACTIVE.value);
        mockRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.PENDING.value);

        when(resourceRepository.findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(mockCurrentUser));
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                eq(requestId),
                eq(ProjectRequest.statusValues.DELETED.value),
                eq(ProjectRequest.approvalStatusValues.PENDING.value)))
                .thenReturn(Optional.of(mockRequest));

        // Test and Verify
        assertThrows(BadRequestException.class, () -> {
            service.deleteProjectRequest(requestId);
        });
    }

    @Test
    void testDeleteProjectRequestUserNotFound() {
        // Setup
        Integer requestId = 1;

        when(resourceRepository.findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.empty());
        when(messageSource.getMessage(eq(USER_NOT_FOUND), eq(null), eq(Locale.ENGLISH)))
                .thenReturn("User not found message");

        // Test and Verify
        assertThrows(BadRequestException.class, () -> {
            service.deleteProjectRequest(requestId);
        });

        // Verify
        verify(resourceRepository, times(1)).findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value));
        verify(messageSource, times(1)).getMessage(eq(USER_NOT_FOUND), eq(null), eq(Locale.ENGLISH));
    }

    @Test
    void testDeleteProjectRequestNotFound() {
        // Setup
        Integer requestId = 1;

        Resource mockCurrentUser = new Resource();
        mockCurrentUser.setEmail("test@gmail.com");
        mockCurrentUser.setStatus(Resource.Status.ACTIVE.value);
        mockCurrentUser.setRole(new Role(1, "HR", new Date(), new Date()));

        when(resourceRepository.findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(mockCurrentUser));
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                eq(requestId),
                eq(ProjectRequest.statusValues.DELETED.value),
                eq(ProjectRequest.approvalStatusValues.PENDING.value)))
                .thenReturn(Optional.empty());
        when(messageSource.getMessage(eq(PROJECT_REQUEST_NOT_FOUND), eq(null), eq(Locale.ENGLISH)))
                .thenReturn("Project request not found message");

        // Test and Verify
        assertThrows(BadRequestException.class, () -> {
            service.deleteProjectRequest(requestId);
        });

        // Verify
        verify(resourceRepository, times(1)).findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value));
        verify(projectRequestRepository, times(1)).findByProjectRequestIdAndStatusNotAndApprovalStatus(
                eq(requestId),
                eq(ProjectRequest.statusValues.DELETED.value),
                eq(ProjectRequest.approvalStatusValues.PENDING.value));
        verify(messageSource, times(1)).getMessage(eq(PROJECT_REQUEST_NOT_FOUND), eq(null), eq(Locale.ENGLISH));
    }
}
