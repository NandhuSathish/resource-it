package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.*;
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
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = EditProjectTest.class)
class EditProjectTest {
    private MockedStatic<SecurityUtil> mockedSecurityUtil;
    @InjectMocks
    private ProjectServiceImpli service;

    @Mock
    private ProjectRequestRepository projectRequestRepository;
    @Mock
    private CommonFunctions commonFunction;

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
    private EmailUtils emailUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(customValidator, messageSource, projectRepository, skillRepository, resourceRepository, projectRequestRepository, commonFunction, emailUtils);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void testEditProjectHOD() {
        // Mock data
        ProjectRequestRequestDTO dto = createDTO();
        Project existingProject = createProject();
        Resource hod = createResource("HOD");
        List<Integer> userId = new ArrayList<>();
        userId.add(1);
        // Mocking repository methods
        when(projectRepository.findById(dto.getProjectId())).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);
        when(resourceRepository.findByIdAndStatus(dto.getManagerId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(hod));
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(hod));
        when(skillRepository.findAllById(dto.getSkillIds())).thenReturn(Arrays.asList(new Skill(), new Skill()));
        when(resourceRepository.findAllIdByRoleIdAndStatus(Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value)).thenReturn(userId);
        when(commonFunction.convertDateToTimestamp(any())).thenReturn(new Date());

        // Test the method
        service.editProject(dto);

        // Assertions
        verify(projectRepository, times(2)).save(any());
    }

    @Test
    void testEditProjectHODManagerEdit() {
        // Mock data
        ProjectRequestRequestDTO dto = createDTOModified();
        Project existingProject = createProject();
        Resource hod = createResourceModified("HOD");
        List<Integer> userId = new ArrayList<>();
        userId.add(1);
        // Mocking repository methods
        when(projectRepository.findById(dto.getProjectId())).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);
        when(resourceRepository.findByIdAndStatus(dto.getManagerId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(hod));
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(hod));
        when(skillRepository.findAllById(dto.getSkillIds())).thenReturn(Arrays.asList(new Skill(), new Skill()));
        when(resourceRepository.findAllIdByRoleIdAndStatus(Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value)).thenReturn(userId);
        when(allocationRepository.findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(anyInt(), anyInt(), anyByte())).thenReturn(new Allocation(1));
        when(commonFunction.convertDateToTimestamp(any())).thenReturn(new Date());

        // Test the method
        service.editProject(dto);

        // Assertions
        verify(projectRepository, times(2)).save(any());
    }

    @Test
    void testEditProjectWithInvalidSkillId() {
        // Mock data
        ProjectRequestRequestDTO dto = createDTOModified();
        Project existingProject = createProject();
        Resource hod = createResourceModified("HOD");
        List<Integer> userId = new ArrayList<>();
        userId.add(1);
        // Mocking repository methods
        when(projectRepository.findById(dto.getProjectId())).thenReturn(Optional.empty());
        when(projectRepository.save(existingProject)).thenReturn(existingProject);
        when(resourceRepository.findByIdAndStatus(dto.getManagerId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(hod));
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(hod));
        when(skillRepository.findAllById(dto.getSkillIds())).thenReturn(Arrays.asList(new Skill(), new Skill()));
        Assertions.assertThrows(BadRequestException.class, () -> {
            service.editProject(dto);
        });
    }

    @Test
    void testEditProjectHR() {
        // Mock data
        ProjectRequestRequestDTO dto = createDTO();
        dto.setManagerId(2); // Simulating an HR scenario
        Project existingProject = createProject();
        Resource hr = createResource("HR");

        // Mocking repository methods
        when(projectRepository.findById(dto.getProjectId())).thenReturn(Optional.of(existingProject));
        when(projectRepository.findByProjectIdAndStatusNot(dto.getProjectId(), Project.statusValues.DELETED.value)).thenReturn(Optional.of(existingProject));
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(hr));
        when(skillRepository.findAllById(dto.getSkillIds())).thenReturn(Arrays.asList(new Skill(), new Skill()));
        when(resourceRepository.findByIdAndStatus(2, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(hr));
        when(commonFunction.checkProjectChanges(dto)).thenReturn(Boolean.TRUE);


        // Test the method
        service.editProject(dto);

        // Assertions
        verify(projectRepository, times(1)).save(any());
    }

    @Test
    void testEditProjectPM() {
        // Mock data
        ProjectRequestRequestDTO dto = createDTO();
        Resource pm = createResource("PM");
        Project existingProject = createProject();

        // Mocking repository methods
        when(projectRepository.findById(dto.getProjectId())).thenReturn(Optional.of(existingProject));
        when(projectRepository.findByProjectIdAndStatusNot(dto.getProjectId(), Project.statusValues.DELETED.value)).thenReturn(Optional.of(existingProject));
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(pm));
        when(skillRepository.findAllById(dto.getSkillIds())).thenReturn(Arrays.asList(new Skill(), new Skill()));
        when(resourceRepository.findByIdAndStatus(2, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(pm));
        when(commonFunction.checkProjectChanges(dto)).thenReturn(Boolean.TRUE);
        // Test the method
        service.editProject(dto);

        // Assertions
        verify(projectRepository, times(1)).save(any());
    }

    @Test
    void testEditProjectHRChangeOnFields() {
        // Mock data
        ProjectRequestRequestDTO dto = createDTO();
        dto.setManagerId(2); // Simulating an HR scenario
        Project existingProject = createProject();
        Resource hr = createResource("HR");
        List<Integer> userId = new ArrayList<>();
        userId.add(1);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setCreatedDate(new Date());
        projectRequest.setUpdatedDate(new Date());
        when(projectRequestRepository.save(any())).thenReturn(projectRequest);
        // Mocking repository methods
        when(projectRepository.findById(dto.getProjectId())).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);
        when(projectRepository.findByProjectIdAndStatusNot(dto.getProjectId(), Project.statusValues.DELETED.value)).thenReturn(Optional.of(existingProject));
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(hr));
        when(skillRepository.findAllById(dto.getSkillIds())).thenReturn(Arrays.asList(new Skill(), new Skill()));
        when(resourceRepository.findByIdAndStatus(2, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(hr));
        when(commonFunction.checkProjectChanges(dto)).thenReturn(Boolean.FALSE);
        when(resourceRepository.findAllIdByRoleIdAndStatus(Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value)).thenReturn(userId);


        // Test the method
        service.editProject(dto);

        // Assertions
        verify(projectRequestRepository, times(1)).save(any());
        verify(projectRepository, times(1)).save(any());
    }

    @Test
    void testEditProjectPMChangeOnFields() {
        // Mock data
        ProjectRequestRequestDTO dto = createDTO();
        Resource pm = createResource("PM");
        Project existingProject = createProject();
        List<Integer> userId = new ArrayList<>();
        userId.add(1);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setCreatedDate(new Date());
        projectRequest.setUpdatedDate(new Date());
        when(projectRequestRepository.save(any())).thenReturn(projectRequest);
        // Mocking repository methods
        when(projectRepository.findById(dto.getProjectId())).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);
        when(projectRepository.findByProjectIdAndStatusNot(dto.getProjectId(), Project.statusValues.DELETED.value)).thenReturn(Optional.of(existingProject));
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(pm));
        when(skillRepository.findAllById(dto.getSkillIds())).thenReturn(Arrays.asList(new Skill(), new Skill()));
        when(resourceRepository.findByIdAndStatus(2, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(pm));
        when(commonFunction.checkProjectChanges(dto)).thenReturn(Boolean.FALSE);
        when(resourceRepository.findAllIdByRoleIdAndStatus(Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value)).thenReturn(userId);

        // Test the method
        service.editProject(dto);

        // Assertions
        verify(projectRequestRepository, times(1)).save(any());
        verify(projectRepository, times(1)).save(any());
    }

    private ProjectRequestRequestDTO createDTO() {
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setProjectId(1);
        dto.setManagerId(2);
        dto.setSkillIds(Arrays.asList(1, 2));
        dto.setStartDate(new Date());
        dto.setEndDate(new Date(System.currentTimeMillis() + 100000));
        dto.setProjectType((byte) 0);
        return dto;
    }

    private ProjectRequestRequestDTO createDTOModified() {
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setProjectId(1);
        dto.setManagerId(1);
        dto.setSkillIds(Arrays.asList(1, 2));
        dto.setStartDate(new Date());
        dto.setEndDate(new Date(System.currentTimeMillis() + 100000));
        dto.setProjectType((byte) 0);
        return dto;
    }

    private Project createProject() {
        Project project = new Project();
        project.setProjectId(1);
        project.setName("Sample Project");
        project.setProjectCode("P123");
        project.setProjectType((byte) 0);
        project.setClientName("Client XYZ");
        Resource hr = createResource("HR");
        project.setManager(hr);
        return project;
    }

    private Resource createResource(String role) {
        Resource resource = new Resource();
        resource.setId(2);
        resource.setName("John Doe");
        resource.setEmail("john.doe@example.com");
        resource.setRole(new Role(role));
        return resource;
    }

    private Resource createResourceModified(String role) {
        Resource resource = new Resource();
        resource.setId(1);
        resource.setName("John Doe");
        resource.setEmail("john.doe@example.com");
        resource.setRole(new Role(role));
        return resource;
    }
    @Test
    void testHandleProjectManagerAllocation() {
        // Arrange
        Project project = new Project();
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setManager(new Resource(1));

        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setProjectId(1);
        dto.setManagerId(1);
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());

        Allocation prevAllocation = new Allocation();
        prevAllocation.setStatus(Allocation.StatusValues.ACTIVE.value);

        when(allocationRepository.findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(anyInt(), anyInt(), anyByte()))
                .thenReturn(prevAllocation);
        when(commonFunction.convertDateToTimestamp(any())).thenReturn(new Date());

        // Act
        service.handleProjectManagerAllocation(project, dto);

        // Assert
        verify(allocationRepository, times(1)).save(any(Allocation.class));
    }

    @Test
    void testHandleProjectManagerAllocationOnApproval() {
        // Arrange
        Project project = new Project();
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setManager(new Resource(1));

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProject(project);
        projectRequest.setStartDate(new Date());
        projectRequest.setEndDate(new Date());

        Allocation managerAllocation = new Allocation(1);
        managerAllocation.setStatus(Allocation.StatusValues.ACTIVE.value);

        when(allocationRepository.findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(projectRequest.getProject().getProjectId(), project.getManager().getId(), Allocation.StatusValues.ACTIVE.value))
                .thenReturn(managerAllocation);
        when(commonFunction.convertDateToTimestamp(any())).thenReturn(new Date());

        // Act
        service.handleProjectManagerAllocationOnApproval(project, projectRequest);

        // Assert
        verify(allocationRepository, times(1)).save(any(Allocation.class));
    }

    @Test
    void testHandleProjectManagerAllocationOnProjectMangerChange() {
        // Arrange
        Allocation prevAllocation = new Allocation();
        prevAllocation.setStartDate(new Date());
        prevAllocation.setEndDate(new Date());

        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setManagerId(1);
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());

        Project project = new Project();
        project.setManager(new Resource(2));

        Resource requestedBy = new Resource();
        requestedBy.setStatus(Resource.Status.ACTIVE.value);

        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(requestedBy));
        when(commonFunction.convertDateToTimestamp(any())).thenReturn(new Date());

        // Act
        service.handleProjectManagerAllocationOnProjectMangerChange(prevAllocation, LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(), dto, project);

        // Assert
        verify(allocationRepository, times(1)).saveAll(anyList());
    }

}