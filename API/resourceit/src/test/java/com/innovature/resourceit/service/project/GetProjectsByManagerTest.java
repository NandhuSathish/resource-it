package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.responsedto.ProjectListByManagerResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ProjectRequestRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.SkillRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.ProjectServiceImpli;
import com.innovature.resourceit.util.CustomValidator;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = GetProjectsByManagerTest.class)
class GetProjectsByManagerTest {
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


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ProjectServiceImpli(customValidator, messageSource, projectRepository, skillRepository, resourceRepository, projectRequestRepository);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void testGetProjectsByManagerCurrentUser() {
        Boolean isCurrentUser = true;
        Resource mockResource = new Resource();
        mockResource.setId(1);
        mockResource.setEmail("test@gmail.com");
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.of(mockResource));

        List<Project> mockProjects = new ArrayList<>();
        Project mockProject = new Project();
        mockProject.setProjectId(1);
        mockProject.setName("Sample Project");
        mockProjects.add(mockProject);

        when(projectRepository.findByManagerIdAndStatusAndProjectStateNotAndEdited(1, Project.statusValues.ACTIVE.value, Project.projectStateValues.COMPLETED.value, Project.editedValues.NOT_EDITED.value)).thenReturn(mockProjects);

        List<ProjectListByManagerResponseDTO> responseDTOList = service.getProjectsByManager(isCurrentUser);

        assertEquals(1, responseDTOList.size());
        assertEquals(1, responseDTOList.get(0).getProjectId());
        assertEquals("Sample Project", responseDTOList.get(0).getProjectName());

        verify(resourceRepository, times(1)).findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value);
        verify(projectRepository, times(1)).findByManagerIdAndStatusAndProjectStateNotAndEdited(1, Project.statusValues.ACTIVE.value, Project.projectStateValues.COMPLETED.value, Project.editedValues.NOT_EDITED.value);
    }

    @Test
    void testGetProjectsByManagerNotCurrentUser() {
        Boolean isCurrentUser = false;

        List<Project> mockProjects = new ArrayList<>();
        Project mockProject = new Project();
        mockProject.setProjectId(1);
        mockProject.setName("Sample Project");
        mockProjects.add(mockProject);

        when(projectRepository.findByStatusAndProjectStateNotAndEdited(Project.statusValues.ACTIVE.value, Project.projectStateValues.COMPLETED.value, Project.editedValues.NOT_EDITED.value)).thenReturn(mockProjects);

        List<ProjectListByManagerResponseDTO> responseDTOList = service.getProjectsByManager(isCurrentUser);

        assertEquals(1, responseDTOList.size());
        assertEquals(1, responseDTOList.get(0).getProjectId());
        assertEquals("Sample Project", responseDTOList.get(0).getProjectName());

        verify(resourceRepository, never()).findByEmailAndStatus(anyString(), anyByte());
        verify(projectRepository, times(1)).findByStatusAndProjectStateNotAndEdited(Project.statusValues.ACTIVE.value, Project.projectStateValues.COMPLETED.value, Project.editedValues.NOT_EDITED.value);
    }

    @Test
    void testGetProjectsByManagerCurrentUserNotFound() {
        Boolean isCurrentUser = true;
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> {
            service.getProjectsByManager(isCurrentUser);
        });

        verify(resourceRepository, times(1)).findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value);
        verify(projectRepository, never()).findByManagerIdAndStatusAndProjectStateNotAndEdited(anyInt(), anyByte(), anyByte(), anyByte());
    }
}
