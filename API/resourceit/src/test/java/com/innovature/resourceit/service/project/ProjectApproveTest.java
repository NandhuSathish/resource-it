package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.ProjectRequest;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.ProjectServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import com.innovature.resourceit.util.CustomValidator;
import com.innovature.resourceit.util.EmailUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = ProjectApproveTest.class)
class ProjectApproveTest {

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
    private CommonFunctions commonFunctions;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;

    @BeforeEach
    public void setUp() {
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
    void testProjectApproveHalfway() {

        Mockito.doNothing().when(customValidator).validateProjectRequestInputParameters(Mockito.anyString(),
                Mockito.any(), Mockito.any(), Mockito.anyByte(), Mockito.anyByte(), Mockito.anyInt());

        ProjectRequest projectRequest = new ProjectRequest();
        Resource resource = new Resource(1);
        projectRequest.setRequestedBy(resource);
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        when(commonFunctions.convertDateToTimestamp(any())).thenReturn(new Date());
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                1, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value))
                .thenReturn(Optional.of(projectRequest));

        // Mock the resourceRepository to return a manager resource
        Resource manager = new Resource();
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(manager));

        ProjectRequestRequestDTO dto = getProjectRequestRequestDTO();

        Project project = new Project();
        project.setProjectCode("PROJ_01");
        project.setName("RESOURCE");
        project.setProjectType((byte) 1);
        project.setClientName("CLIENT_01");
        project.setTeamSize(123);
        project.setManDay(10);
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        when(projectRepository.findByProjectIdAndStatusNot(3, Project.statusValues.DELETED.value))
                .thenReturn(Optional.of(project));
        Project projectToReturn = new Project(dto, new ArrayList<>(), manager);
        when(projectRepository.save(any(Project.class))).thenReturn(projectToReturn);
        ProjectServiceImpli projectServiceMock = mock(ProjectServiceImpli.class);
        when(projectServiceMock.areProjectFieldsNotNull(any(Project.class))).thenCallRealMethod();
        assertDoesNotThrow(() -> projectService.projectApprove(1, dto));

    }

    @Test
    void testProjectApproveWithInvalidApprovalStatus() {
        ProjectRequestRequestDTO projectRequestRequestDTO = new ProjectRequestRequestDTO();
        projectRequestRequestDTO.setApprovalStatus(null);
        assertThrows(BadRequestException.class, () -> {
            projectService.projectApprove(1, projectRequestRequestDTO);
        });
    }

    @Test
    void testProjectApproveActive() {

        Mockito.doNothing().when(customValidator).validateProjectRequestInputParameters(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyByte(), Mockito.anyByte(), Mockito.anyInt());

        ProjectRequest projectRequest = new ProjectRequest();
        Resource resource = new Resource(1);
        projectRequest.setRequestedBy(resource);
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                1, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value))
                .thenReturn(Optional.of(projectRequest));

        // Mock the resourceRepository to return a manager resource
        Resource manager = new Resource();
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(manager));
        when(commonFunctions.convertDateToTimestamp(any())).thenReturn(new Date());

        ProjectRequestRequestDTO dto = getProjectRequestRequestDTO();

        Project project = new Project();
        project.setProjectCode("PROJ_01");
        project.setName("RESOURCE");
        project.setProjectType((byte) 1);
        project.setClientName("CLIENT_01");
        project.setTeamSize(123);
        project.setManDay(10);
        project.setManager(new Resource());
        project.setStartDate(new Date());
        project.setEndDate(new Date());

        // Mock the projectRepository if needed
        when(projectRepository.findByProjectIdAndStatusNot(3, Project.statusValues.DELETED.value))
                .thenReturn(Optional.of(project));

        Project projectToReturn = new Project(dto, new ArrayList<>(), manager);

        // Mock the projectRepository to save the project
        when(projectRepository.save(any(Project.class))).thenReturn(projectToReturn);

        ProjectServiceImpli projectServiceMock = mock(ProjectServiceImpli.class);

        when(projectServiceMock.areProjectFieldsNotNull(any(Project.class))).thenCallRealMethod();
        when(projectServiceMock.areProjectFieldsNotNull(project)).thenReturn(true);
        assertDoesNotThrow(() -> projectService.projectApprove(1, dto));

    }

    @Test
    void testProjectApproveWithoutManager() {
        Mockito.doNothing().when(customValidator).validateProjectRequestInputParameters(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyByte(), Mockito.anyByte(), Mockito.anyInt());
        ProjectRequestRequestDTO dto = getProjectRequestRequestDTO();
        dto.setManagerId(null);
        Assertions.assertThrows(BadRequestException.class, () -> {
            projectService.projectApprove(1, dto);
        });

    }

    @Test
    void testProjectApproveWithInvalidSkillIds() {

        Mockito.doNothing().when(customValidator).validateProjectRequestInputParameters(Mockito.anyString(),
                Mockito.any(), Mockito.any(), Mockito.anyByte(), Mockito.anyByte(), Mockito.anyInt());

        ProjectRequest projectRequest = new ProjectRequest();
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                1, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value))
                .thenReturn(Optional.of(projectRequest));

        // Mock the resourceRepository to return a manager resource
        Resource manager = new Resource();
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(manager));

        ProjectRequestRequestDTO dto = getProjectRequestRequestDTO();
        dto.setSkillIds(Arrays.asList(1, 2, 3));
        Assertions.assertThrows(BadRequestException.class, () -> {
            projectService.projectApprove(1, dto);
        });
    }

    @Test
    void testProjectApproveWithInvalidProjectId() {

        Mockito.doNothing().when(customValidator).validateProjectRequestInputParameters(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyByte(), Mockito.anyByte(), Mockito.anyInt());
        ProjectRequest projectRequest = new ProjectRequest();
        when(projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(
                1, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value))
                .thenReturn(Optional.of(projectRequest));
        Resource manager = new Resource();
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(manager));
        ProjectRequestRequestDTO dto = getProjectRequestRequestDTO();
        when(projectRepository.findByProjectIdAndStatusNot(3, Project.statusValues.DELETED.value))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(BadRequestException.class, () -> {
            projectService.projectApprove(1, dto);
        });
    }

    private ProjectRequestRequestDTO getProjectRequestRequestDTO() {
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setDescription("Description");
        dto.setManDay(5);
        dto.setStartDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        dto.setEndDate(calendar.getTime());
        dto.setApprovalStatus((byte) 1);
        dto.setManagerId(1);
        dto.setSkillIds(new ArrayList<>());
        dto.setProjectId(3);
        dto.setProjectType((byte) 1);
        dto.setProjectCode("CLIENT_01");
        return dto;
    }
}
