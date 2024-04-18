package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.requestdto.ProjectDownloadRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ProjectListingRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceDownloadFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceSkillWiseAllocationRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceSkillWiseAllocationRequestListDTO;
import com.innovature.resourceit.entity.dto.requestdto.SkillExperienceRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ProjectListResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceSkillWiseAllocationResponseListDTO;
import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@SpringBootTest
@ContextConfiguration(classes = ProjectListControllerTest.class)
class ProjectListControllerTest {

    private ProjectController projectController;
    private ProjectService projectService;
    private MessageSource messageSource;
    private ResourceRepository resourceRepository;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        projectService = mock(ProjectService.class);
        messageSource = mock(MessageSource.class);
        resourceRepository = mock(ResourceRepository.class);
        projectController = new ProjectController(projectService, messageSource, resourceRepository);
    }

    @Test
    void testGetProjects() throws ParseException {

        List<String> managerIds = Arrays.asList("1");
        List<String> projectStates = Arrays.asList("1");
        List<String> projectTypes = Arrays.asList("1");
        Boolean sortOrder = false;
        String sortKey = "projectId";

        ProjectListingRequestDTO projectListingRequestDTO = new ProjectListingRequestDTO();
        projectListingRequestDTO.setEndDate("01-02-2022");
        projectListingRequestDTO.setManagerId(managerIds);
        projectListingRequestDTO.setPageNumber("0");
        projectListingRequestDTO.setProjectName("Test Project");
        projectListingRequestDTO.setProjectState(projectStates);
        projectListingRequestDTO.setProjectType(projectTypes);
        projectListingRequestDTO.setPageSize("10");
        projectListingRequestDTO.setSortKey(sortKey);
        projectListingRequestDTO.setSortOrder(sortOrder);

        projectListingRequestDTO.setStartDate("01-01-2022");

        ProjectListResponseDTO projectListResponseDTO = new ProjectListResponseDTO();
        projectListResponseDTO.setProjectId(1);
        projectListResponseDTO.setName("Test Project");
        List<ProjectListResponseDTO> projectListResponseDTOs = new ArrayList<>();
        projectListResponseDTOs.add(projectListResponseDTO);

        PagedResponseDTO<ProjectListResponseDTO> pagedResponseDTO = new PagedResponseDTO<>();
        pagedResponseDTO.setCurrentPage(0);
        pagedResponseDTO.setTotalItems(5);
        pagedResponseDTO.setTotalPages(3);
        pagedResponseDTO.setItems(projectListResponseDTOs);
        when(projectService.getProjects(projectListingRequestDTO)).thenReturn(pagedResponseDTO);
        ResponseEntity<PagedResponseDTO<ProjectListResponseDTO>> retuenPagedResponseDTO = projectController.getProjects(projectListingRequestDTO);

        assertEquals(200, retuenPagedResponseDTO.getStatusCode().value());
        assertEquals(pagedResponseDTO, retuenPagedResponseDTO.getBody());

    }

    @Test
    public void projectDownloadValid() throws Exception {
        doNothing().when(projectService).projectExcelDownload(any(HttpServletResponse.class), any(ProjectDownloadRequestDTO.class));

        ProjectDownloadRequestDTO requestDTO = new ProjectDownloadRequestDTO();

        ResponseEntity<Object> responseEntity = projectController.exportProjects(response, requestDTO);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully downloaded", ((SuccessResponseDTO) responseEntity.getBody()).getSuccessMessage());

        // Verify that setContentDisposition is called with the correct argument
        verify(response).setHeader(eq(HttpHeaders.CONTENT_DISPOSITION), eq("attachment; filename=\"project.xlsx\""));
    }

    
}
