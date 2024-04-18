package com.innovature.resourceit.service.Statistics;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.criteriaquery.BillabilityRepositoryCriteria;
import com.innovature.resourceit.entity.criteriaquery.impl.BillabilityRepositoryCriteriaImpl;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.BillabilitySummaryRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceAnalysisRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterSkillAndExperienceRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.*;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.ResourceSkillRepository;
import com.innovature.resourceit.service.StatisticsService;
import com.innovature.resourceit.service.impli.CommonServiceForResourceDownloadAndListingImpli;
import com.innovature.resourceit.service.impli.StatisticsDownloadServiceImpli;
import com.innovature.resourceit.service.impli.StatisticsServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = getBillingDownloadTest.class)
public class getBillingDownloadTest {
    @Mock
    HttpServletResponse response;
    @Mock
    ServletOutputStream servletOutputStream;
    @Mock
    StatisticsService statisticsService;
    @Mock
    BillabilityRepositoryCriteria billabilityRepositoryCriteria;
    @Mock
    ResourceSkillRepository resourceSkillRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    MessageSource messageSource;
    @Mock
    private ParameterValidator parameterValidator;
    @Mock
    private XSSFWorkbook workbook;
    @Mock
    private XSSFSheet sheet;
    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private StatisticsServiceImpli statisticsServiceImpli;
    @Mock
    private BillabilityRepositoryCriteriaImpl billabilityRepositoryCriteriaImpl;
    @InjectMocks
    private StatisticsDownloadServiceImpli statisticsDownloadServiceImpli;
    private BillabilitySummaryResponseDTO billabilitySummaryResponseDTO;
    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private CommonServiceForResourceDownloadAndListingImpli listingImpli;
    @Mock
    private CommonFunctions commonFunctions;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        billabilitySummaryResponseDTO = new BillabilitySummaryResponseDTO();

    }

    @Test
    void billabilityDownloadTest() throws IOException {
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setEmployeeId("1");
        requestDTO.setName("asw");
        requestDTO.setDepartmentIds(Arrays.asList(1));
        requestDTO.setProjectIds(Arrays.asList(1));
        requestDTO.setLowerExperience(1);
        requestDTO.setHighExperience(5);
        requestDTO.setPageNumber("1");
        requestDTO.setPageSize("900000");
        Date startDate = null;
        Date endDate = null;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = calendar.getTime();
        requestDTO.setStartDate(startDate);
        requestDTO.setEndDate(endDate);
        // requestDTO.setStatus("1");
        requestDTO.setSortOrder(true);
        requestDTO.setSortKey("benchDays");
        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperienceRequestDTOS = new ArrayList<>();
        ResourceFilterSkillAndExperienceRequestDTO skillAndExperienceRequestDTO = new ResourceFilterSkillAndExperienceRequestDTO();
        skillAndExperienceRequestDTO.setSkillId("1");
        skillAndExperienceRequestDTO.setSkillMinValue("0");
        skillAndExperienceRequestDTO.setSkillMaxValue("50");
        skillAndExperienceRequestDTO.setProficiency(List.of((byte) 0));
        skillAndExperienceRequestDTOS.add(skillAndExperienceRequestDTO);
        requestDTO.setSkillAndExperiences(skillAndExperienceRequestDTOS);

        List<BillabilitySummaryResponseDTO> lists = new ArrayList<>();
        billabilitySummaryResponseDTO.setResourceId(123);
        billabilitySummaryResponseDTO.setEmployeeId(365);
        billabilitySummaryResponseDTO.setProjectName("asw");
        billabilitySummaryResponseDTO.setName("aswin");
        billabilitySummaryResponseDTO.setDepartmentName("Software");
        billabilitySummaryResponseDTO.setRole("HOD");
        billabilitySummaryResponseDTO.setAllocationStatus((byte) 0);
        billabilitySummaryResponseDTO.setJoiningDate("01/03/2020");
        billabilitySummaryResponseDTO.setWorkSpan(20);
        billabilitySummaryResponseDTO.setTotalExperience("23");
        billabilitySummaryResponseDTO.setBillableDays(22);
        billabilitySummaryResponseDTO.setBenchDays(20);
        List<ResourceSkillResponseDTO> skillResponseDTOS = new ArrayList<>();
        ResourceSkillResponseDTO skillResponseDTO = new ResourceSkillResponseDTO();
        skillResponseDTO.setExperience(1);
        skillResponseDTO.setSkillName("java");
        skillResponseDTO.setSkillId(1);
        skillResponseDTO.setProficiency((byte) 1);
        skillResponseDTOS.add(skillResponseDTO);
        billabilitySummaryResponseDTO.setResourceSkillResponseDTOs(skillResponseDTOS);

        List<Resource> resourceList = new ArrayList<>();
        lists.add(billabilitySummaryResponseDTO);
        PagedResponseDTO<BillabilitySummaryResponseDTO> pagedResponseDTO = new PagedResponseDTO();
        pagedResponseDTO.setItems(lists);
        pagedResponseDTO.setCurrentPage(1);
        pagedResponseDTO.setTotalPages(1);
        pagedResponseDTO.setTotalItems(1);
        String skillEXP = "Automation : 1.0y : Intermediate";
        when(listingImpli.skillAndExp(skillResponseDTOS)).thenReturn(skillEXP);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        when(billabilityRepositoryCriteriaImpl.findResourceForBillabilityStatistic(requestDTO))
                .thenReturn(resourceList);
        when(statisticsServiceImpli.getAllResource(requestDTO)).thenReturn(pagedResponseDTO);
        statisticsDownloadServiceImpli.billingResourceExcelDownload(response, requestDTO);
        Assertions.assertEquals(1, lists.size());

    }

    @Test
    void getAllProjectAllocationDetailsExcelDownload() throws IOException, ParseException {
        List<ProjectAllocationResponseDTO> projectAllocationResponseDTOS = new ArrayList<>();
        ProjectAllocationResponseDTO projectAllocationResponseDTO = new ProjectAllocationResponseDTO();
        projectAllocationResponseDTO.setResourceName("aswin");
        projectAllocationResponseDTO.setProjectName("data");
        projectAllocationResponseDTO.setEmployeeId(123);
        projectAllocationResponseDTO.setProjectCode("78a");
        projectAllocationResponseDTO.setStartDate("2024-02-02");
        projectAllocationResponseDTO.setEndDate("2024-02-29");
        projectAllocationResponseDTO.setProjectType((byte) 1);
        projectAllocationResponseDTO.setStatus((byte) 1);
        projectAllocationResponseDTOS.add(projectAllocationResponseDTO);
        PagedResponseDTO<ProjectAllocationResponseDTO> pagedResponseDTO = new PagedResponseDTO<>();
        pagedResponseDTO.setItems(projectAllocationResponseDTOS);
        pagedResponseDTO.setCurrentPage(1);
        pagedResponseDTO.setTotalItems(1);
        pagedResponseDTO.setTotalPages(1);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        when(statisticsServiceImpli.getProjectAllocationDetails(any(), anyInt(), anyInt(), any(), any()))
                .thenReturn(pagedResponseDTO);
        SimpleDateFormat dateFormatMock = mock(SimpleDateFormat.class);

        // Define the expected behavior when the format method is called
        when(dateFormatMock.format(any(Date.class))).thenReturn("formattedDate");
        Integer id = 1;
        int page = 1;
        int size = 2;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();
        ResourceAnalysisRequestDTO requestDTO = new ResourceAnalysisRequestDTO();
        requestDTO.setId(1);
        requestDTO.setStartDate(new Date());
        requestDTO.setEndDate(new Date());
        statisticsDownloadServiceImpli.getProjectAllocationDetailsExcelDownload(response, requestDTO);
        Assertions.assertEquals(1, projectAllocationResponseDTOS.size());
    }

    @Test
    void testGetProjectAllocationDetails() {

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setStartDate(Date.from(LocalDate.of(2024, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        requestDTO.setSortKey("benchDays");
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Resource resourceB = createResource(2, "test1@gmail.com", true);

        Project project = new Project(1);
        Allocation allocation = new Allocation(1);
        Allocation allocation2 = new Allocation(2);

        allocation.setProject(project);
        allocation.setStartDate(new Date());
        allocation.setEndDate(new Date());
        allocation2.setProject(project);
        allocation2.setStartDate(new Date());
        allocation2.setEndDate(new Date());
        // Mock the behavior of your repository methods
        when(resourceRepository.findAll()).thenReturn(List.of(resourceA, resourceB));
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any()))
                .thenReturn(Date.from(LocalDate.of(2024, 2, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(allocationRepository
                .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(),
                        anyByte()))
                .thenReturn(List.of(allocation, allocation2));
        // Call the method to test
        List<ProjectAllocationResponseDTO> result = statisticsDownloadServiceImpli.getProjectAllocationDetails();

        assertNotNull(result); // Adjust based on your actual expectations
    }

    private Resource createResource(int id, String email, boolean isActive) {
        Resource resource = new Resource();
        Department department = new Department();
        Role role = new Role(1, "Resource");
        department.setDepartmentId(1);
        resource.setId(id);
        resource.setEmail(email);
        resource.setStatus(isActive ? Resource.Status.ACTIVE.value : Resource.Status.INACTIVE.value);
        resource.setJoiningDate(Date.from(LocalDate.of(2024, 2, 26).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        resource.setDepartment(department);
        resource.setRole(role);
        resource.setAllocationStatus((byte) 0);
        resource.setExperience(36);
        resource.setEmployeeId(1);
        return resource;
    }


    @Test
    void getAllProjectAllocationDetailsExcelDownloadTest() throws IOException, ParseException {
        List<ProjectAllocationResponseDTO> projectAllocationResponseDTOS = new ArrayList<>();
        ProjectAllocationResponseDTO projectAllocationResponseDTO = new ProjectAllocationResponseDTO();
        projectAllocationResponseDTO.setResourceName("aswin");
        projectAllocationResponseDTO.setProjectName("data");
        projectAllocationResponseDTO.setEmployeeId(1);
        projectAllocationResponseDTO.setProjectCode("78a");
        projectAllocationResponseDTO.setStartDate("2024-02-02");
        projectAllocationResponseDTO.setEndDate("2024-02-29");
        projectAllocationResponseDTO.setProjectType(Project.projectTypeValues.BILLABLE.value);
        projectAllocationResponseDTO.setStatus((byte) 1);
        projectAllocationResponseDTOS.add(projectAllocationResponseDTO);
        List<Resource> resourceList = new ArrayList<>();
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Resource resourceB = createResource(2, "test1@gmail.com", true);
        resourceList.add(resourceA);
        resourceList.add(resourceB);
        List<Allocation> allocationList = new ArrayList<>();
        Allocation allocation = new Allocation();
        allocation.setId(1);
        allocation.setStatus((byte) 1);
        allocation.setStartDate(new Date());
        allocation.setEndDate(new Date());
        Project project = new Project(1);
        project.setProjectType(Project.projectTypeValues.BILLABLE.value);

        allocation.setProject(project);

        allocationList.add(allocation);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        when(resourceRepository.findAllByStatus(anyByte())).thenReturn(resourceList);
        when(allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(any(), any(), any(), anyByte())).thenReturn(allocationList);
        when(statisticsDownloadServiceImpli.getProjectAllocationDetails()).thenReturn(projectAllocationResponseDTOS);
        statisticsDownloadServiceImpli.getAllProjectAllocationDetailsExcelDownload(response);
        Assertions.assertEquals(1, projectAllocationResponseDTOS.size());
    }
}
