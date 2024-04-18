package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.ProjectServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import com.innovature.resourceit.util.CustomValidator;
import com.innovature.resourceit.util.EmailUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest
@ContextConfiguration(classes = ProjectApproveByIdTest.class)
class ProjectApproveByIdTest {

    @InjectMocks
    private ProjectServiceImpli projectService;

    @Mock
    private ProjectRequestRepository projectRequestRepository;
    @Mock
    private EmailUtils emailUtils;
    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private MessageSource messageSource;

    @Mock
    private CustomValidator customValidator;

    @Mock
    private EntityManager entityManager;
    @Mock
    private CommonFunctions commonFunctions;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }
    @Test
    void testProjectApproveById() {
        ProjectRequest projectRequest = new ProjectRequest();
        Project project = new Project();
        Role role = new Role("HR");
        Resource resource = new Resource(1);
        resource.setRole(role);
        project.setManager(resource);
        project.setProjectId(1);
        project.setStatus(Project.statusValues.HALFWAY.value);
        projectRequest.setProject(project);
        projectRequest.setRequestedBy(resource);
        projectRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.PENDING.value);
        projectRequest.setStatus(ProjectRequest.statusValues.ACTIVE.value);
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                1, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value))
                .thenReturn(Optional.of(projectRequest));
        project.setStatus((byte) 1);
        when(projectRepository.findByProjectIdAndStatusNot(1, Project.statusValues.DELETED.value))
                .thenReturn(Optional.of(project));
        ProjectServiceImpli projectServiceMock = mock(ProjectServiceImpli.class);
        when(projectServiceMock.areProjectRequestFieldsNotNull(any(ProjectRequest.class)))
                .thenCallRealMethod();
        projectRequest.setProjectCode("PROJ_01");
        projectRequest.setName("RESOURCE");
        projectRequest.setProjectType(ProjectRequest.projectTypeValues.INTERNAL.value);
        projectRequest.setClientName("Innovature");
        projectRequest.setTeamSize(11);
        projectRequest.setManDay(100);
        Skill skill1 = new Skill(); // You may need to set properties of the skill
        projectRequest.setSkill(new ArrayList<>(Collections.singletonList(skill1)));
        Skill mergedSkill = new Skill(); // You may need to set properties of the merged skill
        when(entityManager.merge(skill1)).thenReturn(mergedSkill);
        projectRequest.setManager(new Resource());
        projectRequest.setStartDate(new Date());
        projectRequest.setEndDate(new Date());
        projectRequest.setProject(project);
        when(projectRepository.save(project)).thenReturn(project);
        Allocation allocation = new Allocation();
        when(allocationRepository.findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(anyInt(), anyInt(), anyByte())).thenReturn(allocation);
        when(projectServiceMock.areProjectRequestFieldsNotNull(eq(projectRequest)))
                .thenReturn(true);
        when(commonFunctions.convertDateToTimestamp(any())).thenReturn(new Date());
        assertDoesNotThrow(() -> projectService.projectApproveById(1, (byte) 1));

    }

    @Test
    void testProjectApproveByIdByNotHR() {
        ProjectRequest projectRequest = new ProjectRequest();
        Project project = new Project();
        Role role = new Role(2, "HOD");
        Resource resource = new Resource(1);
        resource.setRole(role);
        project.setManager(resource);
        project.setProjectId(1);
        project.setStatus(Project.statusValues.HALFWAY.value);
        projectRequest.setProject(project);
        projectRequest.setRequestedBy(resource);
        projectRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.PENDING.value);
        projectRequest.setStatus(ProjectRequest.statusValues.ACTIVE.value);
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                1, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value))
                .thenReturn(Optional.of(projectRequest));
        project.setStatus((byte) 1);
        when(projectRepository.findByProjectIdAndStatusNot(1, Project.statusValues.DELETED.value))
                .thenReturn(Optional.of(project));
        ProjectServiceImpli projectServiceMock = mock(ProjectServiceImpli.class);
        when(projectServiceMock.areProjectRequestFieldsNotNull(any(ProjectRequest.class)))
                .thenCallRealMethod();
        projectRequest.setProjectCode("PROJ_01");
        projectRequest.setName("RESOURCE");
        projectRequest.setProjectType(ProjectRequest.projectTypeValues.INTERNAL.value);
        projectRequest.setClientName("Innovature");
        projectRequest.setTeamSize(11);
        projectRequest.setManDay(100);
        projectRequest.setRequestedBy(resource);
        Skill skill1 = new Skill(); // You may need to set properties of the skill
        projectRequest.setSkill(new ArrayList<>(Collections.singletonList(skill1)));
        Skill mergedSkill = new Skill(); // You may need to set properties of the merged skill
        when(entityManager.merge(skill1)).thenReturn(mergedSkill);
        projectRequest.setManager(new Resource(1));
        projectRequest.setStartDate(new Date());
        projectRequest.setEndDate(new Date());
        projectRequest.setProject(project);
        when(projectRepository.save(project)).thenReturn(project);
        Allocation allocation = new Allocation();
        when(allocationRepository.findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(anyInt(), anyInt(), anyByte())).thenReturn(allocation);
        when(projectServiceMock.areProjectRequestFieldsNotNull(eq(projectRequest)))
                .thenReturn(true);
        when(commonFunctions.convertDateToTimestamp(any())).thenReturn(new Date());
        assertDoesNotThrow(() -> projectService.projectApproveById(1, (byte) 1));

    }

    @Test
    void testInValidStatus() {
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                1, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value))
                .thenReturn(Optional.of(new ProjectRequest()));
        Assertions.assertThrows(BadRequestException.class, () -> {
            projectService.projectApproveById(1, (byte) 2);
        });
    }

    @Test
    void testProjectNull() {
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProject(null);
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                1, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value))
                .thenReturn(Optional.of(projectRequest));
        Assertions.assertThrows(BadRequestException.class, () -> {
            projectService.projectApproveById(1, (byte) 1);
        });
    }

    @Test
    void testAlreadyApprovedProject() {
        Project project = new Project();
        Resource manager = new Resource(1);
        project.setManager(manager);
        project.setProjectId(1);
        project.setStatus(Project.statusValues.ACTIVE.value);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProject(project);

        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                1, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value))
                .thenReturn(Optional.of(projectRequest));
        when(projectRepository.findByProjectIdAndStatusNot(1, Project.statusValues.DELETED.value))
                .thenReturn(Optional.of(project));
        when(allocationRepository.findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(anyInt(), anyInt(), anyByte())).thenReturn(new Allocation());

        Assertions.assertThrows(BadRequestException.class, () -> {
            projectService.projectApproveById(1, (byte) 1);
        });
    }

    @Test
    void testProjectActiveWithIncompleteProject() {
        Project project = new Project();
        project.setProjectId(1);
        Resource manager = new Resource(1);
        project.setManager(manager);
        project.setStatus(Project.statusValues.HALFWAY.value);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProject(project);

        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                1, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value))
                .thenReturn(Optional.of(projectRequest));
        when(projectRepository.findByProjectIdAndStatusNot(1, Project.statusValues.DELETED.value))
                .thenReturn(Optional.of(project));
        when(allocationRepository.findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(anyInt(), anyInt(), anyByte())).thenReturn(new Allocation());

        Assertions.assertThrows(BadRequestException.class, () -> {
            projectService.projectApproveById(1, (byte) 1);
        });
        ProjectServiceImpli projectServiceMock = mock(ProjectServiceImpli.class);
        when(allocationRepository.findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(anyInt(), anyInt(), anyByte())).thenReturn(new Allocation());
        when(projectServiceMock.areProjectRequestFieldsNotNull(any(ProjectRequest.class))).thenCallRealMethod();
        Assertions.assertThrows(BadRequestException.class, () -> {
            projectService.projectApproveById(1, (byte) 1);
        });
    }

}
