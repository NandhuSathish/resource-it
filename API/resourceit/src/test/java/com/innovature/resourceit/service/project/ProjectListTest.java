package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.criteriaquery.ProjectRepositoryCriteria;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.ProjectListingRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ProjectListResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.ProjectServiceImpli;
import com.innovature.resourceit.util.CustomValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;

@SpringBootTest
@ContextConfiguration(classes = ProjectListTest.class)
class ProjectListTest {

    @InjectMocks
    ProjectServiceImpli projectServiceImpli;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    MessageSource messageSource;

    @Mock
    CustomValidator customValidator;

    @Mock
    ParameterValidator parameterValidator;

    @Mock
    ProjectRepositoryCriteria projectRepositoryCriteria;

    private MockedStatic<SecurityUtil> mockedSecurityUtil;
    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private ResourceRepository resourceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        mockedSecurityUtil.close();
    }

    @Test
    void testGetProjectsWithSortOrderFalse() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String pageNo = "0";
        String size = "10";
        int pageNoInt = 0;
        int sizeInt = 10;
        List<String> projectStateString = Arrays.asList("1");
        List<Integer> projectStateInt = Arrays.asList(1);
        List<String> projectTypeString = Arrays.asList("1");
        List<Integer> projectTypeInt = Arrays.asList(1);
        List<String> managerIdsString = Arrays.asList("1");
        List<Integer> managerIdsInt = Arrays.asList(1);
        String projectName = "TestProject";
        String startDate = "01-01-2022";
        String endDate = "01-02-2022";
        Boolean sortOrder = false;
        String sortKey = "projectCode";

        Project p = new Project(1);
        p.setStartDate(sdf.parse(startDate));
        p.setEndDate(sdf.parse(endDate));
        p.setCreatedDate(sdf.parse(startDate));
        p.setUpdatedDate(sdf.parse(startDate));

        List<Project> projectList = new ArrayList<>();
        projectList.add(p);

        ProjectListingRequestDTO projectListingRequestDTO = new ProjectListingRequestDTO();
        projectListingRequestDTO.setEndDate(endDate);
        projectListingRequestDTO.setManagerId(managerIdsString);
        projectListingRequestDTO.setPageNumber(pageNo);
        projectListingRequestDTO.setProjectName(projectName);
        projectListingRequestDTO.setProjectState(projectStateString);
        projectListingRequestDTO.setProjectType(projectTypeString);
        projectListingRequestDTO.setPageSize(size);
        projectListingRequestDTO.setSortKey(sortKey);
        projectListingRequestDTO.setStartDate(startDate);
        projectListingRequestDTO.setSortOrder(sortOrder);

        Pageable pageable = PageRequest.of(pageNoInt, sizeInt, Sort.by(Boolean.FALSE.equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortKey));
        Page<Project> projectPage = new PageImpl<>(projectList, pageable, projectList.size());

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);

        resource.setRole(ro);
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        when(parameterValidator.isNumber("pageNumber", pageNo)).thenReturn(pageNoInt);
        when(parameterValidator.isNumber("pageSize", size)).thenReturn(sizeInt);
        when(parameterValidator.isNumbersNum("projectType", projectTypeString)).thenReturn(projectTypeInt);
        when(parameterValidator.isNumbersNum("projectState", projectStateString)).thenReturn(projectStateInt);
        when(parameterValidator.isNumbersNum("managerId", managerIdsString)).thenReturn(managerIdsInt);
        when(projectRepositoryCriteria.findFilteredProjectsWithPagination(projectName, sdf.parse(startDate), sdf.parse(endDate),
                projectStateInt, managerIdsInt, projectTypeInt, pageable)).thenReturn(projectPage);

        ProjectListResponseDTO projectListResponseDTO = new ProjectListResponseDTO();
        projectListResponseDTO.setProjectId(p.getProjectId());
        projectListResponseDTO.setStartDate(startDate);
        projectListResponseDTO.setEndDate(endDate);
        projectListResponseDTO.setCreatedDate(startDate);
        projectListResponseDTO.setUpdatedDate(startDate);

        PagedResponseDTO<ProjectListResponseDTO> pagedResponseDTO1 = new PagedResponseDTO<>(Arrays.asList(projectListResponseDTO), projectPage.getTotalPages(), projectPage.getTotalElements(), projectPage.getNumber());

        PagedResponseDTO<ProjectListResponseDTO> pagedResponseDTO = projectServiceImpli.getProjects(projectListingRequestDTO);

        Assertions.assertEquals(pagedResponseDTO1.getItems().get(0).getProjectId(), pagedResponseDTO.getItems().get(0).getProjectId());
    }

    @Test
    void testGetProjectsWithStartDateFormatError() throws ParseException {

        String pageNo = "0";
        String size = "10";
        int pageNoInt = 0;
        int sizeInt = 10;
        List<String> projectStateString = Arrays.asList("1");
        List<Integer> projectStateInt = Arrays.asList(1);
        List<String> projectTypeString = Arrays.asList("1");
        List<Integer> projectTypeInt = Arrays.asList(1);
        List<String> managerIdsString = Arrays.asList("1");
        List<Integer> managerIdsInt = Arrays.asList(1);
        String projectName = "TestProject";
        String startDate = "01/01/2022";
        String endDate = "01-02-2022";
        Boolean sortOrder = false;
        String sortKey = "name";

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);

        resource.setRole(ro);
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        ProjectListingRequestDTO projectListingRequestDTO = new ProjectListingRequestDTO();
        projectListingRequestDTO.setEndDate(endDate);
        projectListingRequestDTO.setManagerId(managerIdsString);
        projectListingRequestDTO.setPageNumber(pageNo);
        projectListingRequestDTO.setProjectName(projectName);
        projectListingRequestDTO.setProjectState(projectStateString);
        projectListingRequestDTO.setProjectType(projectTypeString);
        projectListingRequestDTO.setPageSize(size);
        projectListingRequestDTO.setSortKey(sortKey);
        projectListingRequestDTO.setStartDate(startDate);
        projectListingRequestDTO.setSortOrder(sortOrder);

        when(parameterValidator.isNumber("pageNumber", pageNo)).thenReturn(pageNoInt);
        when(parameterValidator.isNumber("pageSize", size)).thenReturn(sizeInt);
        when(parameterValidator.isNumbersNum("projectType", projectTypeString)).thenReturn(projectTypeInt);
        when(parameterValidator.isNumbersNum("projectState", projectStateString)).thenReturn(projectStateInt);
        when(parameterValidator.isNumbersNum("managerId", managerIdsString)).thenReturn(managerIdsInt);

        when(messageSource.getMessage(eq("DATE_FORMAT_ERROR"), eq(null), any()))
                .thenReturn("1336-Date format error.");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            projectServiceImpli.getProjects(projectListingRequestDTO);
        });

        assertEquals("1336-Date format error.", exception.getBody().getDetail());
    }

    @Test
    void testGetProjectsWithInvalid() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String pageNo = "0";
        String size = "10";
        int pageNoInt = 0;
        int sizeInt = 10;
        List<String> projectStateString = Arrays.asList("1");
        List<Integer> projectStateInt = Arrays.asList(1);
        List<String> projectTypeString = Arrays.asList("1");
        List<Integer> projectTypeInt = Arrays.asList(1);
        List<String> managerIdsString = Arrays.asList("1");
        List<Integer> managerIdsInt = Arrays.asList(1);
        String projectName = "TestProject";
        String startDate = "01-01-2022";
        String endDate = "01-02-2022";
        Boolean sortOrder = false;
        String sortKey = "teamSize";

        Project p = new Project(1);
        p.setStartDate(sdf.parse(startDate));
        p.setEndDate(sdf.parse(endDate));

        List<Project> projectList = new ArrayList<>();
        projectList.add(p);

        ProjectListingRequestDTO projectListingRequestDTO = new ProjectListingRequestDTO();
        projectListingRequestDTO.setEndDate(endDate);
        projectListingRequestDTO.setManagerId(managerIdsString);
        projectListingRequestDTO.setPageNumber(pageNo);
        projectListingRequestDTO.setProjectName(projectName);
        projectListingRequestDTO.setProjectState(projectStateString);
        projectListingRequestDTO.setProjectType(projectTypeString);
        projectListingRequestDTO.setPageSize(size);
        projectListingRequestDTO.setSortKey(sortKey);
        projectListingRequestDTO.setStartDate(startDate);
        projectListingRequestDTO.setSortOrder(sortOrder);

        Pageable pageable = PageRequest.of(pageNoInt, sizeInt, Sort.by(Boolean.FALSE.equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortKey));
        Page<Project> projectPage = new PageImpl<>(projectList, pageable, projectList.size());

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);

        resource.setRole(ro);
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        when(parameterValidator.isNumber("pageNumber", pageNo)).thenReturn(pageNoInt);
        when(parameterValidator.isNumber("pageSize", size)).thenReturn(sizeInt);
        when(parameterValidator.isNumbersNum("projectType", projectTypeString)).thenReturn(projectTypeInt);
        when(parameterValidator.isNumbersNum("projectState", projectStateString)).thenReturn(projectStateInt);
        when(parameterValidator.isNumbersNum("managerId", managerIdsString)).thenReturn(managerIdsInt);
        when(projectRepositoryCriteria.findFilteredProjectsWithPagination(projectName, sdf.parse(startDate), sdf.parse(endDate),
                projectStateInt, managerIdsInt, projectTypeInt, pageable)).thenReturn(projectPage);

        ProjectListResponseDTO projectListResponseDTO = new ProjectListResponseDTO();
        projectListResponseDTO.setProjectId(p.getProjectId());

        when(messageSource.getMessage(eq("PROJECT_LIST_FETCHING_FAILED"), eq(null), any()))
                .thenReturn("1337-Project list fetching failed");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            projectServiceImpli.getProjects(projectListingRequestDTO);
        });

        assertEquals("1337-Project list fetching failed", exception.getBody().getDetail());
    }
    @Test
    void getSortKeyTestCaseNull() {
        String sortKey = "empId";
        String result = projectServiceImpli.getSortKey(null);
        assertEquals("createdDate", result);
    }
    @Test
    void getSortKeyTestCaseProjectName() {
        String sortKey = "projectName";
        String result = projectServiceImpli.getSortKey(sortKey);
        assertEquals("name", result);
    }
    @Test
    void getSortKeyTestCaseEndDate() {
        String sortKey = "endDate";
        String result = projectServiceImpli.getSortKey(sortKey);
        assertEquals("endDate", result);
    }
    @Test
    void getSortKeyTestCaseStartDate() {
        String sortKey = "startDate";
        String result = projectServiceImpli.getSortKey(sortKey);
        assertEquals("startDate", result);
    }

   
}
