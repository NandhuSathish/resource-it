package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.ProjectRequest;
import com.innovature.resourceit.entity.dto.responsedto.ProjectRequestResponseDTO;
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
@ContextConfiguration(classes = GetProjectRequestByIdTest.class)
 class GetProjectRequestByIdTest {
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
    void testGetProjectRequestById() {
        Integer projectRequestId = 1;
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectRequestId(projectRequestId);
        when(projectRequestRepository.findById(projectRequestId)).thenReturn(Optional.of(projectRequest));

        ProjectRequestResponseDTO responseDTO = service.getProjectRequestById(projectRequestId);

        assertEquals(projectRequestId, responseDTO.getProjectRequestId());
        verify(projectRequestRepository, times(1)).findById(projectRequestId);
    }

    @Test
    void testGetProjectRequestByIdNotFound() {
        Integer projectRequestId = 1;
        when(projectRequestRepository.findById(projectRequestId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> {
            service.getProjectRequestById(projectRequestId);
        });
        verify(projectRequestRepository, times(1)).findById(projectRequestId);
    }
}

