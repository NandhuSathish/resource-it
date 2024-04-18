package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.ProjectRequest;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ProjectRequestRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.SkillRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.ProjectServiceImpli;
import com.innovature.resourceit.util.CustomValidator;
import com.innovature.resourceit.util.EmailUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = RejectProjectTest.class)
class RejectProjectTest {
    private MockedStatic<SecurityUtil> mockedSecurityUtil;
    @InjectMocks
    private ProjectServiceImpli service;

    @Mock
    private ProjectRequestRepository projectRequestRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CustomValidator customValidator;

    @Mock
    private EmailUtils emailUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(customValidator, projectRequestRepository, resourceRepository, skillRepository, projectRepository,
                messageSource, emailUtils);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void rejectProject_ValidInput_Success() {
        Integer projRequestId = 1;
        Byte approvalStatus = 2;
        ProjectRequest projectRequest = new ProjectRequest();
        Resource resource = new Resource(1);
        projectRequest.setProjectRequestId(projRequestId);
        projectRequest.setStatus(ProjectRequest.statusValues.ACTIVE.value);
        projectRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.PENDING.value);
        projectRequest.setRequestedBy(resource);

        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(eq(projRequestId), anyByte(), anyByte()))
                .thenReturn(Optional.of(projectRequest));
        when(projectRequestRepository.save(projectRequest))
                .thenReturn(projectRequest);
        service.rejectProject(projRequestId, approvalStatus);

        verify(projectRequestRepository, times(1)).findByProjectRequestIdAndStatusNotAndApprovalStatus(projRequestId, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value);
        verify(projectRequestRepository, times(1)).save(projectRequest);
    }

    @Test
    void rejectProject_ValidInput_Success_And_With_Project() {
        Integer projRequestId = 1;
        Byte approvalStatus = 2;
        Project project=new Project();
        ProjectRequest projectRequest = new ProjectRequest();
        Resource resource = new Resource(1);
        projectRequest.setProjectRequestId(projRequestId);
        projectRequest.setStatus(ProjectRequest.statusValues.ACTIVE.value);
        projectRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.PENDING.value);
        projectRequest.setRequestedBy(resource);
        projectRequest.setProject(project);
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(eq(projRequestId), anyByte(), anyByte()))
                .thenReturn(Optional.of(projectRequest));
        when(projectRequestRepository.save(projectRequest))
                .thenReturn(projectRequest);
        service.rejectProject(projRequestId, approvalStatus);

        verify(projectRequestRepository, times(1)).findByProjectRequestIdAndStatusNotAndApprovalStatus(projRequestId, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value);
        verify(projectRequestRepository, times(1)).save(projectRequest);
    }

    @Test
    void rejectProject_InvalidApprovalStatus_ExceptionThrown() {
        Integer projRequestId = 1;
        Byte invalidApprovalStatus = 1;

        assertThrows(BadRequestException.class, () -> service.rejectProject(projRequestId, invalidApprovalStatus));
        verifyNoInteractions(projectRequestRepository);
    }

    @Test
    void rejectProject_ProjectRequestNotFound_ExceptionThrown() {
        Integer projRequestId = 1;
        Byte approvalStatus = 2;

        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(eq(projRequestId), anyByte(), anyByte()))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> service.rejectProject(projRequestId, approvalStatus));
        verify(projectRequestRepository, times(1)).findByProjectRequestIdAndStatusNotAndApprovalStatus(projRequestId, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value);
        verifyNoMoreInteractions(projectRequestRepository);
    }
}
