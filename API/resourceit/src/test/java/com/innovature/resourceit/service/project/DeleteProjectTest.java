package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.ProjectServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = DeleteProjectTest.class)
class DeleteProjectTest {

    private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String PROJECT_NOT_FOUND = "PROJECT_NOT_FOUND";
    private static final String ACCESS_DENIED = "ACCESS_DENIED";

    private MockedStatic<SecurityUtil> mockedSecurityUtil;

    @InjectMocks
    private ProjectServiceImpli service;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;
    @Mock
    private ResourceSkillWiseAllocationRequestRepository resourceSkillWiseAllocationRequestRepository;
    @Mock
    private ProjectRequestRepository projectRequestRepository;
    @Mock
    private CommonFunctions commonFunctions;
    @Mock
    private MessageSource messageSource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(messageSource, resourceRepository, projectRepository);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void testDeleteProjectHR() {
        // Setup
        Integer projectId = 1;

        Resource mockCurrentUser = new Resource();
        mockCurrentUser.setEmail("test@gmail.com");
        mockCurrentUser.setStatus(Resource.Status.ACTIVE.value);
        mockCurrentUser.setRole(new Role(1, "HR", new Date(), new Date()));

        Project mockProject = new Project();
        mockProject.setStatus(Project.statusValues.ACTIVE.value);

        when(resourceRepository.findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(mockCurrentUser));
        when(projectRepository.findByProjectIdAndStatusNot(eq(projectId), eq(Project.statusValues.DELETED.value)))
                .thenReturn(Optional.of(mockProject));

        // Test
        service.deleteProject(projectId);

        // Verify
        verify(resourceRepository, times(1)).findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value));
        verify(projectRepository, times(1)).findByProjectIdAndStatusNot(eq(projectId), eq(Project.statusValues.DELETED.value));
        verify(projectRepository, times(1)).save(any());
    }

    @Test
    void testDeleteProjectAccessDenied() {
        // Setup
        Integer projectId = 1;

        Resource mockCurrentUser = new Resource();
        mockCurrentUser.setEmail("test@gmail.com");
        mockCurrentUser.setStatus(Resource.Status.ACTIVE.value);
        mockCurrentUser.setRole(new Role(1, "SomeOtherRole", new Date(), new Date()));

        Project mockProject = new Project();
        mockProject.setStatus(Project.statusValues.ACTIVE.value);

        when(resourceRepository.findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(mockCurrentUser));
        when(projectRepository.findByProjectIdAndStatusNot(eq(projectId), eq(Project.statusValues.DELETED.value)))
                .thenReturn(Optional.of(mockProject));

        // Test and Verify
        assertThrows(BadRequestException.class, () -> {
            service.deleteProject(projectId);
        });
    }

    @Test
    void testDeleteProjectUserNotFound() {
        // Setup
        Integer projectId = 1;

        when(resourceRepository.findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.empty());
        when(messageSource.getMessage(eq(USER_NOT_FOUND), eq(null), eq(Locale.ENGLISH)))
                .thenReturn("User not found message");

        // Test and Verify
        assertThrows(BadRequestException.class, () -> {
            service.deleteProject(projectId);
        });

        // Verify
        verify(resourceRepository, times(1)).findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value));
        verify(messageSource, times(1)).getMessage(eq(USER_NOT_FOUND), eq(null), eq(Locale.ENGLISH));
    }

    @Test
    void testDeleteProjectNotFound() {
        // Setup
        Integer projectId = 1;

        Resource mockCurrentUser = new Resource();
        mockCurrentUser.setEmail("test@gmail.com");
        mockCurrentUser.setStatus(Resource.Status.ACTIVE.value);
        mockCurrentUser.setRole(new Role(1, "HR", new Date(), new Date()));

        when(resourceRepository.findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(mockCurrentUser));
        when(projectRepository.findByProjectIdAndStatusNot(eq(projectId), eq(Project.statusValues.DELETED.value)))
                .thenReturn(Optional.empty());
        when(messageSource.getMessage(eq(PROJECT_NOT_FOUND), eq(null), eq(Locale.ENGLISH)))
                .thenReturn("Project not found message");

        // Test and Verify
        assertThrows(BadRequestException.class, () -> {
            service.deleteProject(projectId);
        });

        // Verify
        verify(resourceRepository, times(1)).findByEmailAndStatus(eq("test@gmail.com"), eq(Resource.Status.ACTIVE.value));
        verify(projectRepository, times(1)).findByProjectIdAndStatusNot(eq(projectId), eq(Project.statusValues.DELETED.value));
        verify(messageSource, times(1)).getMessage(eq(PROJECT_NOT_FOUND), eq(null), eq(Locale.ENGLISH));
    }
}
