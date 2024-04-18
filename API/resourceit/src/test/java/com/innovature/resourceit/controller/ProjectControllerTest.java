package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.*;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.ProjectService;
import com.innovature.resourceit.util.CommonFunctions;
import com.innovature.resourceit.util.CustomValidator;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.springframework.test.context.ContextConfiguration;

import java.util.*;

@SpringBootTest
@ContextConfiguration(classes = ProjectControllerTest.class)
class ProjectControllerTest {

    @InjectMocks
    private ProjectController projectController;
    @Mock
    private ProjectService projectService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CustomValidator customValidator;
    @Mock
    private CommonFunctions commonFunction;

    @Mock
    private ResourceRepository resourceRepository;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(customValidator, messageSource, projectService, resourceRepository, commonFunction);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@example.com");
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }


    @Test
    void testAddProjectRequest() {
        // Create a sample ProjectRequestRequestDTO for the test
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setDescription(null);
        dto.setManDay(null);
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setProjectType(null);
        dto.setManagerId(null);
        doNothing().when(customValidator).validateProjectRequestInputParameters(
                dto.getDescription(),
                dto.getStartDate(),
                dto.getEndDate(),
                null,
                dto.getProjectType(),
                dto.getManagerId());
        when(messageSource.getMessage("PROJECT_REQUEST_ADDED", null, Locale.ENGLISH)).thenReturn("Success-Message");
        ResponseEntity<Object> responseEntity = projectController.addProjectRequest(dto);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testListProjectRequests() {
        Boolean isRequestList = Boolean.TRUE;
        ProjectRequestResponseDTO response = new ProjectRequestResponseDTO();
        response.setName("sample");
        List<ProjectRequestResponseDTO> sampleContent = new ArrayList<>();
        sampleContent.add(response);
        Page<ProjectRequestResponseDTO> samplePage = new PageImpl<>(sampleContent);
        Resource requestedBy = new Resource(1);
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(new Resource()));
        when(projectService.listProjectRequests(eq(Collections.singleton((byte) 1)), eq(Collections.singleton(1)), eq(Collections.singleton((byte) 1)), any(), any(Pageable.class)))
                .thenReturn(samplePage);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(requestedBy));

        ResponseEntity<PagedResponseDTO<ProjectRequestResponseDTO>> responseEntity = projectController.listProjectRequests(0, 20, Collections.singleton((byte) 1), Collections.singleton((byte) 1), Collections.singleton(1), isRequestList);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(resourceRepository, times(1)).findById(1);
        verify(projectService, times(1)).listProjectRequests(
                eq(Collections.singleton((byte) 1)),
                eq(Collections.singleton(1)),
                eq(Collections.singleton((byte) 1)),
                eq(requestedBy),
                any(Pageable.class)
        );
    }


    @Test
    void testListProjectRequestsWithInvalidProjectType() {
        assertThrows(BadRequestException.class, () -> {
            projectController.listProjectRequests(0, 20, Collections.singleton((byte) 1), Collections.singleton((byte) 1), Collections.singleton(1), anyBoolean());
        });
    }

    @Test
    void testListProjectRequestsWithInvalidApprovalStatus() {
        assertThrows(BadRequestException.class, () -> {
            projectController.listProjectRequests(0, 20, Collections.singleton((byte) 3), Collections.singleton((byte) 1), Collections.singleton(1), false);
        });
    }

    @Test
    void testListProjectRequestsWithInvalidManagerId() {
        when(resourceRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> {
            projectController.listProjectRequests(0, 20, Collections.singleton((byte) 1), Collections.singleton((byte) 1), Collections.singleton(1), anyBoolean());
        });
    }

    @Test
    void testProjectApprove() {
        ProjectRequestRequestDTO projectRequestDTO = new ProjectRequestRequestDTO();
        projectRequestDTO.setDescription("Description");
        projectRequestDTO.setApprovalStatus((byte) 1);

        Mockito.when(projectService.projectApprove(Mockito.anyInt(), Mockito.any(ProjectRequestRequestDTO.class)))
                .thenReturn(true);

        when(messageSource.getMessage(eq("PROJECT_APPROVED"), eq(null), any()))
                .thenReturn("1323-Project approved successfully");

        ResponseEntity<Object> response = projectController.projectApprove(1, projectRequestDTO);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void testProjectApproveById() {
        ProjectRequestRequestDTO projectRequestDTO = new ProjectRequestRequestDTO();
        projectRequestDTO.setDescription("Description");
        Mockito.doNothing().when(projectService).projectApproveById(1, (byte) 1);
        when(messageSource.getMessage(eq("PROJECT_APPROVED"), eq(null), any()))
                .thenReturn("1323-Project approved successfully");
        ResponseEntity<Object> response = projectController.projectApproveById(1, (byte) 1);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testProjectRejectById() {
        ProjectRequestRequestDTO projectRequestDTO = new ProjectRequestRequestDTO();
        projectRequestDTO.setDescription("Description");
        Mockito.doNothing().when(projectService).rejectProject(1, (byte) 2);
        when(messageSource.getMessage(eq("PROJECT_REJECTED"), eq(null), any()))
                .thenReturn("1324-Project rejected successfully");
        ResponseEntity<Object> response = projectController.rejectProject(1, (byte) 2);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testEditProjectCheckProjectChangesTrue() {
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setDescription("Sample Description");
        dto.setManDay(10);
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());
        dto.setProjectType((byte) 1);
        dto.setManagerId(1);
        Role role = new Role(1, "PM", new Date(), new Date());
        Resource mockResource = new Resource();
        mockResource.setId(1);
        mockResource.setEmail("test@example.com");
        mockResource.setStatus(Resource.Status.ACTIVE.value);
        mockResource.setRole(role);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(mockResource));
        when(commonFunction.checkProjectChanges(dto)).thenReturn(Boolean.TRUE);
        when(messageSource.getMessage("PROJECT_EDITED", null, Locale.ENGLISH)).thenReturn("Success-Message");
        when(messageSource.getMessage("PROJECT_REQUEST_ADDED", null, Locale.ENGLISH)).thenReturn("Success-Message");

        ResponseEntity<Object> responseEntity = projectController.editProject(dto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertNotNull(successResponseDTO);
        assertEquals("Success", successResponseDTO.getSuccessCode());
        assertEquals("Message", successResponseDTO.getSuccessMessage());

        verify(resourceRepository, times(1)).findByEmailAndStatus(eq("test@example.com"), eq(Resource.Status.ACTIVE.value));
        verify(projectService, times(1)).editProject(dto);
        verify(messageSource, times(1)).getMessage("PROJECT_EDITED", null, Locale.ENGLISH);

    }

    @Test
    void testEditProjectCheckProjectChangesFalse() {
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setDescription("Sample Description");
        dto.setManDay(10);
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());
        dto.setProjectType((byte) 1);
        dto.setManagerId(1);
        Role role = new Role(1, "PM", new Date(), new Date());
        Resource mockResource = new Resource();
        mockResource.setId(1);
        mockResource.setEmail("test@example.com");
        mockResource.setStatus(Resource.Status.ACTIVE.value);
        mockResource.setRole(role);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(mockResource));
        when(commonFunction.checkProjectChanges(dto)).thenReturn(Boolean.FALSE);
        when(messageSource.getMessage("PROJECT_EDITED", null, Locale.ENGLISH)).thenReturn("Success-Message");
        when(messageSource.getMessage("PROJECT_REQUEST_ADDED", null, Locale.ENGLISH)).thenReturn("Success-Message");

        ResponseEntity<Object> responseEntity = projectController.editProject(dto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertNotNull(successResponseDTO);
        assertEquals("Success", successResponseDTO.getSuccessCode());
        assertEquals("Message", successResponseDTO.getSuccessMessage());

        verify(resourceRepository, times(1)).findByEmailAndStatus(eq("test@example.com"), eq(Resource.Status.ACTIVE.value));
        verify(projectService, times(1)).editProject(dto);
        verify(messageSource, times(1)).getMessage("PROJECT_REQUEST_ADDED", null, Locale.ENGLISH);

    }

    @Test
    void testEditProjectByHOD() {
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setDescription("Sample Description");
        dto.setManDay(10);
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());
        dto.setProjectType((byte) 1);
        dto.setManagerId(1);
        Role role = new Role(1, "HOD", new Date(), new Date());
        Resource mockResource = new Resource();
        mockResource.setId(1);
        mockResource.setEmail("test@example.com");
        mockResource.setStatus(Resource.Status.ACTIVE.value);
        mockResource.setRole(role);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(mockResource));
        when(commonFunction.checkProjectChanges(dto)).thenReturn(Boolean.FALSE);
        when(messageSource.getMessage("PROJECT_EDITED", null, Locale.ENGLISH)).thenReturn("Success-Message");
        when(messageSource.getMessage("PROJECT_REQUEST_ADDED", null, Locale.ENGLISH)).thenReturn("Success-Message");

        ResponseEntity<Object> responseEntity = projectController.editProject(dto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertNotNull(successResponseDTO);
        assertEquals("Success", successResponseDTO.getSuccessCode());
        assertEquals("Message", successResponseDTO.getSuccessMessage());

        verify(resourceRepository, times(1)).findByEmailAndStatus(eq("test@example.com"), eq(Resource.Status.ACTIVE.value));
        verify(projectService, times(1)).editProject(dto);
        verify(messageSource, times(1)).getMessage("PROJECT_EDITED", null, Locale.ENGLISH);

    }

    @Test
    void testGetProjectById() {
        Integer projectId = 1;
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        when(projectService.getProjectById(projectId)).thenReturn(projectResponseDTO);

        ResponseEntity<Object> responseEntity = projectController.getProjectById(projectId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(projectResponseDTO, responseEntity.getBody());
        verify(projectService, times(1)).getProjectById(projectId);
    }

    @Test
    void testGetProjectRequestById() {
        Integer projectRequestId = 1;
        ProjectRequestResponseDTO projectRequestResponseDTO = new ProjectRequestResponseDTO();
        when(projectService.getProjectRequestById(projectRequestId)).thenReturn(projectRequestResponseDTO);

        ResponseEntity<Object> responseEntity = projectController.getProjectRequestById(projectRequestId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(projectRequestResponseDTO, responseEntity.getBody());
        verify(projectService, times(1)).getProjectRequestById(projectRequestId);
    }

    @Test
    void testGetProjectsByManager() {
        Boolean isCurrentUser = false;
        List<ProjectListByManagerResponseDTO> projectList = new ArrayList<>();
        when(projectService.getProjectsByManager(isCurrentUser)).thenReturn(projectList);

        ResponseEntity<Object> responseEntity = projectController.getProjectsByManager(isCurrentUser);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(projectList, responseEntity.getBody());
        verify(projectService, times(1)).getProjectsByManager(isCurrentUser);
    }

    @Test
    void testDeleteProjectRequest() {
        Integer projectId = 1;
        doNothing().when(projectService).deleteProjectRequest(projectId);
        when(messageSource.getMessage("PROJECT_REQUEST_DELETED", null, Locale.ENGLISH)).thenReturn("Success-Message");

        ResponseEntity<Object> responseEntity = projectController.deleteProjectRequest(projectId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertNotNull(successResponseDTO);
        assertEquals("Success", successResponseDTO.getSuccessCode());
        assertEquals("Message", successResponseDTO.getSuccessMessage());

        verify(projectService, times(1)).deleteProjectRequest(projectId);
        verify(messageSource, times(1)).getMessage("PROJECT_REQUEST_DELETED", null, Locale.ENGLISH);
    }

    @Test
    void testDeleteProject() {
        Integer projectId = 1;
        doNothing().when(projectService).deleteProject(projectId);
        when(messageSource.getMessage("PROJECT_DELETED", null, Locale.ENGLISH)).thenReturn("Success-Message");

        ResponseEntity<Object> responseEntity = projectController.deleteProject(projectId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        SuccessResponseDTO successResponseDTO = (SuccessResponseDTO) responseEntity.getBody();
        assertNotNull(successResponseDTO);
        assertEquals("Success", successResponseDTO.getSuccessCode());
        assertEquals("Message", successResponseDTO.getSuccessMessage());

        verify(projectService, times(1)).deleteProject(projectId);
        verify(messageSource, times(1)).getMessage("PROJECT_DELETED", null, Locale.ENGLISH);
    }


}
