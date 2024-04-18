package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.dto.responsedto.ProjectResponseDTO;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = GetProjectByIdTest.class)
 class GetProjectByIdTest {
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
        mockedSecurityUtil.close();
    }

    @Test
    void testGetProjectById() {
        Integer projectId = 1;
        Project project = new Project();
        project.setProjectId(projectId);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        ProjectResponseDTO responseDTO = service.getProjectById(projectId);

        assertEquals(projectId, responseDTO.getProjectId());
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    void testGetProjectByIdNotFound() {
        Integer projectId = 1;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> {
            service.getProjectById(projectId);
        });
        verify(projectRepository, times(1)).findById(projectId);
    }
}
