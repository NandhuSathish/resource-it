/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.entity.criteriaquery.ProjectRepositoryCriteria;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.ProjectDownloadRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.ProjectListResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.CommonServiceForResourceDownloadAndListingImpli;
import com.innovature.resourceit.service.impli.ProjectServiceImpli;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.*;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = ProjectDownloadTest.class)
public class ProjectDownloadTest {

    @Mock
    private ParameterValidator parameterValidator;

    @Mock
    private CommonServiceForResourceDownloadAndListingImpli listingImpli;

    @Mock
    private MessageSource messageSource;

    @Mock
    private XSSFWorkbook workbook;

    @Mock
    private XSSFSheet sheet;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletOutputStream servletOutputStream;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProjectServiceImpli projectServiceImpli;

    @Mock
    private ProjectRepositoryCriteria projectRepositoryCriteria;

    private MockedStatic<SecurityUtil> mockedSecurityUtil;

    @Mock
    private ResourceRepository resourceRepository;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        mockedSecurityUtil.close();
    }

    @Test
    public void projectDownloadTest() throws ParseException, IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<String> projectStateString = Arrays.asList("1");
        List<Integer> projectStateInt = Arrays.asList(1);
        List<String> projectTypeString = Arrays.asList("1");
        List<Integer> projectTypeInt = Arrays.asList(1);
        List<String> managerIdsString = Arrays.asList("1");
        List<Integer> managerIdsInt = Arrays.asList(1);
        String projectName = "TestProject";
        String startDate = "01-01-2022";
        String endDate = "01-02-2022";

        Resource r = new Resource(1);
        r.setName("manager");

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(1, "Java"));

        Project p = new Project(1);
        p.setStartDate(sdf.parse(startDate));
        p.setEndDate(sdf.parse(endDate));
        p.setCreatedDate(sdf.parse(startDate));
        p.setUpdatedDate(sdf.parse(startDate));
        p.setProjectState((byte) 1);
        p.setProjectType((byte) 1);
        p.setManager(r);
        p.setManDay(23);
        p.setSkill(skills);
        p.setTeamSize(2);

        List<Project> projectList = new ArrayList<>();
        projectList.add(p);

        ProjectDownloadRequestDTO projectDownloadRequestDTO = new ProjectDownloadRequestDTO();
        projectDownloadRequestDTO.setEndDate(endDate);
        projectDownloadRequestDTO.setManagerId(managerIdsString);
        projectDownloadRequestDTO.setProjectName(projectName);
        projectDownloadRequestDTO.setProjectState(projectStateString);
        projectDownloadRequestDTO.setProjectType(projectTypeString);
        projectDownloadRequestDTO.setStartDate(startDate);

        ProjectListResponseDTO projectListResponseDTO = new ProjectListResponseDTO();
        projectListResponseDTO.setProjectId(p.getProjectId());
        projectListResponseDTO.setStartDate(startDate);
        projectListResponseDTO.setEndDate(endDate);
        projectListResponseDTO.setCreatedDate(startDate);
        projectListResponseDTO.setUpdatedDate(startDate);

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);

        resource.setRole(ro);
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        when(response.getOutputStream()).thenReturn(servletOutputStream);
        when(parameterValidator.isNumbersNum("projectType", projectTypeString)).thenReturn(projectTypeInt);
        when(parameterValidator.isNumbersNum("projectState", projectStateString)).thenReturn(projectStateInt);
        when(parameterValidator.isNumbersNum("managerId", managerIdsString)).thenReturn(managerIdsInt);
        when(projectRepositoryCriteria.findFilteredProjectsForDownload(projectName, sdf.parse(startDate), sdf.parse(endDate),
                projectStateInt, managerIdsInt, projectTypeInt)).thenReturn(projectList);

        projectServiceImpli.projectExcelDownload(response, projectDownloadRequestDTO);
        Assertions.assertEquals(1, projectList.size());
    }
    @Test
    public void projectDownloadTestDoThrow() throws ParseException, IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<String> projectStateString = Arrays.asList("1");
        List<Integer> projectStateInt = Arrays.asList(1);
        List<String> projectTypeString = Arrays.asList("1");
        List<Integer> projectTypeInt = Arrays.asList(1);
        List<String> managerIdsString = Arrays.asList("1");
        List<Integer> managerIdsInt = Arrays.asList(1);
        String projectName = "TestProject";
        String startDate = "01-01-2022";
        String endDate = "01-02-2022";

        Resource r = new Resource(1);
        r.setName("manager");

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(1, "Java"));

        Project p = new Project(1);
        p.setStartDate(sdf.parse(startDate));
        p.setEndDate(sdf.parse(endDate));
        p.setCreatedDate(sdf.parse(startDate));
        p.setUpdatedDate(sdf.parse(startDate));
        p.setProjectState((byte) 1);
        p.setProjectType((byte) 1);
        p.setManager(r);
        p.setManDay(23);
        p.setSkill(skills);
        p.setTeamSize(2);

        List<Project> projectList = new ArrayList<>();
        projectList.add(p);

        ProjectDownloadRequestDTO projectDownloadRequestDTO = new ProjectDownloadRequestDTO();
        projectDownloadRequestDTO.setEndDate(endDate);
        projectDownloadRequestDTO.setManagerId(managerIdsString);
        projectDownloadRequestDTO.setProjectName(projectName);
        projectDownloadRequestDTO.setProjectState(projectStateString);
        projectDownloadRequestDTO.setProjectType(projectTypeString);
        projectDownloadRequestDTO.setStartDate(startDate);

        ProjectListResponseDTO projectListResponseDTO = new ProjectListResponseDTO();
        projectListResponseDTO.setProjectId(p.getProjectId());
        projectListResponseDTO.setStartDate(startDate);
        projectListResponseDTO.setEndDate(endDate);
        projectListResponseDTO.setCreatedDate(startDate);
        projectListResponseDTO.setUpdatedDate(startDate);

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);

        resource.setRole(ro);
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        when(response.getOutputStream()).thenReturn(servletOutputStream);
        when(parameterValidator.isNumbersNum("projectType", projectTypeString)).thenReturn(projectTypeInt);
        when(parameterValidator.isNumbersNum("projectState", projectStateString)).thenReturn(projectStateInt);
        when(parameterValidator.isNumbersNum("managerId", managerIdsString)).thenReturn(managerIdsInt);
        when(projectRepositoryCriteria.findFilteredProjectsForDownload(projectName, sdf.parse(startDate), sdf.parse(endDate),
                projectStateInt, managerIdsInt, projectTypeInt)).thenReturn(projectList);

        doThrow(new IOException("Simulated IOException")).when(response).getOutputStream();

        Assertions.assertThrows(BadRequestException.class, () -> {
            projectServiceImpli.projectExcelDownload(response, projectDownloadRequestDTO);
        });

    }
}
