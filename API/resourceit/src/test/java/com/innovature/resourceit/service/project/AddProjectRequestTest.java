package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.ProjectRequest;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.ProjectRequestResponseDTO;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = AddProjectRequestTest.class)
class AddProjectRequestTest {

    @InjectMocks
    private ProjectServiceImpli projectService;

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
    private EmailUtils emailUtils;
    @Mock
    CustomValidator customValidator;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;


    @BeforeEach
    public void setUp() {
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
        MockitoAnnotations.initMocks(this);
        Mockito.reset(customValidator, projectRequestRepository, resourceRepository, skillRepository, projectRepository,
                messageSource, emailUtils);
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void AddValidProjectRequest() {
        ProjectRequestRequestDTO dto = getProjectRequestRequestDTO();
        ProjectRequest projectRequest = new ProjectRequest();
        when(projectRequestRepository.findByName("RESOURCE_IT")).thenReturn(Optional.of(projectRequest));
        dto.setSkillIds(Arrays.asList(1, 2, 3));
        Assertions.assertThrows(BadRequestException.class, () -> {
            projectService.addProjectRequest(dto);
        });
    }

    @Test
    void addProjectWithValidSkillIds() {
        Resource requestedBy = new Resource(1);
        ProjectRequestRequestDTO dto = getProjectRequestRequestDTO();
        when(projectRequestRepository.findByName("RESOURCE_IT")).thenReturn(Optional.empty());
        dto.setSkillIds(Arrays.asList(1, 2, 3));
        List<Skill> resultSkills = Arrays.asList(new Skill(), new Skill(), new Skill());
        when(skillRepository.findAllById(any())).thenReturn(resultSkills);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setCreatedDate(new Date());
        projectRequest.setUpdatedDate(new Date());
        when(projectRequestRepository.save(any())).thenReturn(projectRequest);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(requestedBy));
        ProjectRequestResponseDTO responseDTO = projectService.addProjectRequest(dto);
        assertNotNull(responseDTO);
    }

    @Test
    void addProjectWithInvalidSkillIds() {
        ProjectRequestRequestDTO dto = getProjectRequestRequestDTO();
        when(projectRequestRepository.findByName("RESOURCE_IT")).thenReturn(Optional.empty());
        dto.setSkillIds(Arrays.asList(1, 2, 3));
        when(skillRepository.findAllById(any())).thenReturn(Collections.emptyList());

        assertThrows(BadRequestException.class, () -> {
            projectService.addProjectRequest(dto);
        });
    }


    @Test
    void addProjectWithNoSkillIds() {
        Resource requestedBy = new Resource(1);
        ProjectRequestRequestDTO dto = getProjectRequestRequestDTO();
        when(projectRequestRepository.findByName("RESOURCE_IT")).thenReturn(Optional.empty());
        dto.setSkillIds(new ArrayList<>());
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setCreatedDate(new Date());
        projectRequest.setUpdatedDate(new Date());
        when(projectRequestRepository.save(any())).thenReturn(projectRequest);
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(requestedBy));
        ProjectRequestResponseDTO responseDTO = projectService.addProjectRequest(dto);
        assertNotNull(responseDTO);
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
        dto.setName("RESOURCE_IT");
        dto.setProjectType((byte) 1);
        dto.setProjectCode("CLIENT_01");
        return dto;
    }

    @Test
    void addProjectWithExistingName() {
        ProjectRequestRequestDTO dto = getProjectRequestRequestDTO();
        when(projectRepository.findByName("RESOURCE_IT")).thenReturn(Optional.of(new Project()));

        assertThrows(BadRequestException.class, () -> {
            projectService.addProjectRequest(dto);
        });
    }


}
