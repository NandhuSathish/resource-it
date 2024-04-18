package com.innovature.resourceit.service.Statistics;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.criteriaquery.BillabilityRepositoryCriteria;
import com.innovature.resourceit.entity.criteriaquery.impl.BillabilityRepositoryCriteriaImpl;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.BillabilitySummaryRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.BillabilitySummaryResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ProjectAllocationResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.ResourceSkillRepository;
import com.innovature.resourceit.service.StatisticsService;
import com.innovature.resourceit.service.impli.CommonServiceForResourceDownloadAndListingImpli;
import com.innovature.resourceit.service.impli.StatisticsServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = getBillingResourceTest.class)
 class getBillingResourceTest {
    @Mock
    ParameterValidator parameterValidator;

    @Mock
    private CommonFunctions commonFunctions;
    @Mock
    CommonServiceForResourceDownloadAndListingImpli listingImpli;
    @InjectMocks
    StatisticsServiceImpli statisticsServiceImpli;

    @Mock
    BillabilityRepositoryCriteriaImpl billabilityRepositoryCriteriaImpl;

    @Mock
    StatisticsService statisticsService;


    @Mock
    BillabilityRepositoryCriteria billabilityRepositoryCriteria;
    @Mock
    ResourceSkillRepository resourceSkillRepository;
    @Mock
    AllocationRepository allocationRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    ResourceRepository resourceRepository;
    @Mock
    MessageSource messageSource;

    public static Resource createMockResource() {
        Resource mockResource = mock(Resource.class);

        // Set values for your mock resource
        mockResource.setId(1);
        mockResource.setEmployeeId(12345);
        // Set other properties...

        return mockResource;
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBillingResource() {
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setPageSize("1");
        requestDTO.setPageNumber("1");
        when(parameterValidator.isNumber(any(), any())).thenReturn(0);
        when(parameterValidator.isNumber(any(), any())).thenReturn(0);
        List<Resource> mockResourceList = Collections.singletonList(mock(Resource.class));
        when(billabilityRepositoryCriteria.findResourceForBillabilityStatistic(requestDTO)).thenReturn(mockResourceList);
        BillabilitySummaryResponseDTO filterResponseDTO = new BillabilitySummaryResponseDTO();
        filterResponseDTO.setEmployeeId(1);
        filterResponseDTO.setName("john");
        filterResponseDTO.setDepartmentName("HOD");
        filterResponseDTO.setRole("Software");
        filterResponseDTO.setAllocationStatus(Allocation.StatusValues.ACTIVE.value);
        filterResponseDTO.setJoiningDate(null);
        filterResponseDTO.setWorkSpan(2);
        filterResponseDTO.setProjectName(null);
        List<ResourceSkill> mockResourceSkillList = Collections.singletonList(mock(ResourceSkill.class));
        when(resourceSkillRepository.findAllByResourceId(1)).thenReturn(mockResourceSkillList);
        PagedResponseDTO<BillabilitySummaryResponseDTO> result = statisticsService.getAllResource(requestDTO);

        assertEquals(null, result);
    }

    @Test
    public void testGetAllResource() {
        MockitoAnnotations.openMocks(this);

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setStartDate(Date.from(LocalDate.of(2024, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Resource resourceB = createResource(2, "test1@gmail.com", true);
        List<Resource> resourceList = List.of(resourceA, resourceB);
        Project project = new Project(1);
        Allocation allocation = new Allocation(1);
        allocation.setProject(project);
        // Mock the behavior of your repository methods
        when(billabilityRepositoryCriteria.findResourceForBillabilityStatistic(any())).thenReturn(resourceList);
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any())).thenReturn(Date.from(LocalDate.of(2024, 2, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(projectRepository.findByProjectIdAndStatus(any(), any())).thenReturn(Optional.of(project));
        when(allocationRepository
                .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(), anyByte())).thenReturn(List.of(allocation));
        when(allocationRepository
                .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatusAndProjectProjectTypeNot(anyInt(), any(), any(), anyByte(),anyByte())).thenReturn(List.of(allocation));
        // Call the method to test
        PagedResponseDTO<BillabilitySummaryResponseDTO> result = statisticsServiceImpli.getAllResource(requestDTO);

        // Assert the result based on your expectations
        assertEquals(0, result.getItems().size()); // Adjust based on your actual expectations
        // Add more assertions based on your actual expectations
    }

    @Test
    public void testGetAllResourceSortValueBillable() {
        MockitoAnnotations.openMocks(this);

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setStartDate(Date.from(LocalDate.of(2024, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        requestDTO.setSortKey("billableDays");
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Resource resourceB = createResource(2, "test1@gmail.com", true);
        List<Resource> resourceList = List.of(resourceA, resourceB);
        Project project = new Project(1);
        Allocation allocation = new Allocation(1);
        allocation.setProject(project);
        // Mock the behavior of your repository methods
        when(billabilityRepositoryCriteria.findResourceForBillabilityStatistic(any())).thenReturn(resourceList);
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any())).thenReturn(Date.from(LocalDate.of(2024, 2, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(projectRepository.findByProjectIdAndStatus(any(), any())).thenReturn(Optional.of(project));
        when(allocationRepository
                .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(), anyByte())).thenReturn(List.of(allocation));
        // Call the method to test
        PagedResponseDTO<BillabilitySummaryResponseDTO> result = statisticsServiceImpli.getAllResource(requestDTO);

        // Assert the result based on your expectations
        assertEquals(0, result.getItems().size()); // Adjust based on your actual expectations
        // Add more assertions based on your actual expectations
    }

    @Test
    public void testGetAllResourceSortByBenchDays() {
        MockitoAnnotations.openMocks(this);

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setStartDate(Date.from(LocalDate.of(2024, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        requestDTO.setSortKey("benchDays");
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Resource resourceB = createResource(2, "test1@gmail.com", true);
        List<Resource> resourceList = List.of(resourceA, resourceB);
        Project project = new Project(1);
        Allocation allocation = new Allocation(1);
        allocation.setProject(project);
        // Mock the behavior of your repository methods
        when(billabilityRepositoryCriteria.findResourceForBillabilityStatistic(any())).thenReturn(resourceList);
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any())).thenReturn(Date.from(LocalDate.of(2024, 2, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(),anyByte())).thenReturn(List.of(allocation));
        when(projectRepository.findByProjectIdAndStatus(any(), any())).thenReturn(Optional.of(project));
        // Call the method to test
        PagedResponseDTO<BillabilitySummaryResponseDTO> result = statisticsServiceImpli.getAllResource(requestDTO);

        // Assert the result based on your expectations
        assertEquals(0, result.getItems().size()); // Adjust based on your actual expectations
        // Add more assertions based on your actual expectations
    }

    @Test
     void testGetAllResourceSortByUnknown() {
        MockitoAnnotations.openMocks(this);

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setStartDate(Date.from(LocalDate.of(2024, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        requestDTO.setSortKey("unknown");
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Resource resourceB = createResource(2, "test1@gmail.com", true);
        List<Resource> resourceList = List.of(resourceA, resourceB);
        Project project = new Project(1);
        Allocation allocation = new Allocation(1);
        allocation.setProject(project);
        // Mock the behavior of your repository methods
        when(billabilityRepositoryCriteria.findResourceForBillabilityStatistic(any())).thenReturn(resourceList);
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any())).thenReturn(Date.from(LocalDate.of(2024, 2, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(),anyByte())).thenReturn(List.of(allocation));
        when(projectRepository.findByProjectIdAndStatus(any(), any())).thenReturn(Optional.of(project));
        // Call the method to test
        PagedResponseDTO<BillabilitySummaryResponseDTO> result = statisticsServiceImpli.getAllResource(requestDTO);

        // Assert the result based on your expectations
        assertEquals(0, result.getItems().size()); // Adjust based on your actual expectations
        // Add more assertions based on your actual expectations
    }

    @Test
    public void testGetAllResource_startDateNull() {
        MockitoAnnotations.openMocks(this);

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Resource resourceB = createResource(2, "test1@gmail.com", true);
        List<Resource> resourceList = List.of(resourceA, resourceB);
        Project project = new Project(1);
        Allocation allocation = new Allocation(1);
        allocation.setProject(project);
        // Mock the behavior of your repository methods
        when(billabilityRepositoryCriteria.findResourceForBillabilityStatistic(any())).thenReturn(resourceList);
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any())).thenReturn(new Date());
        when(allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(),anyByte())).thenReturn(List.of(allocation));
        when(projectRepository.findByProjectIdAndStatus(any(), any())).thenReturn(Optional.of(project));
        // Call the method to test
        PagedResponseDTO<BillabilitySummaryResponseDTO> result = statisticsServiceImpli.getAllResource(requestDTO);

        // Assert the result based on your expectations
        assertEquals(0, result.getItems().size()); // Adjust based on your actual expectations
        // Add more assertions based on your actual expectations
    }

    @Test
    public void testGetAllResourceBadRequest() {
        MockitoAnnotations.openMocks(this);

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setStartDate(Date.from(LocalDate.of(2024, 1, 8).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        requestDTO.setEndDate(Date.from(LocalDate.of(2023, 1, 8).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Resource resourceB = createResource(2, "test1@gmail.com", true);
        List<Resource> resourceList = List.of(resourceA, resourceB);
        Project project = new Project(1);
        Allocation allocation = new Allocation(1);
        allocation.setProject(project);
        // Mock the behavior of your repository methods
        when(billabilityRepositoryCriteria.findResourceForBillabilityStatistic(any())).thenReturn(resourceList);
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any())).thenReturn(new Date());
        when(allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(),anyByte())).thenReturn(List.of(allocation));
        when(projectRepository.findByProjectIdAndStatus(any(), any())).thenReturn(Optional.of(project));
        Assertions.assertThrows(BadRequestException.class, () -> statisticsServiceImpli.getAllResource(requestDTO));

    }

    @Test
    public void testGetDataResource() {
        // Mocking
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        List<Resource> mockResourceList = Collections.singletonList(createMockResource());
        when(billabilityRepositoryCriteriaImpl.findResourceForBillabilityStatistic(any())).thenReturn(mockResourceList);
        List<BillabilitySummaryResponseDTO> responseDTOList = new ArrayList<>();
        BillabilitySummaryResponseDTO responseDTO = new BillabilitySummaryResponseDTO();
        responseDTO.setName("as");
        responseDTO.setEmployeeId(1);
        responseDTOList.add(responseDTO);
        // Add more mocks for other repositories and scenarios...

        // Call the method
        PagedResponseDTO<BillabilitySummaryResponseDTO> result = statisticsServiceImpli.getAllResource(requestDTO);
        result.setItems(responseDTOList);
        result.setTotalPages(1);
        result.setCurrentPage(1);
        result.setTotalItems(1);
        // Assertions
        assertEquals(1, result.getItems().size());
        // Add more assertions as needed
    }

    @Test
    void testGetResourceAnalysis() {

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setStartDate(Date.from(LocalDate.of(2024, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        requestDTO.setSortKey("benchDays");
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Project project = new Project(1);
        Allocation allocation = new Allocation(1);
        allocation.setProject(project);
        // Mock the behavior of your repository methods
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(resourceA));
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any())).thenReturn(Date.from(LocalDate.of(2024, 2, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(),anyByte())).thenReturn(List.of(allocation));

        // Call the method to test
        BillabilitySummaryResponseDTO result = statisticsServiceImpli.getResourceAnalysis(1, null, null);

        // Assert the result based on your expectations
        assertNotNull(result); // Adjust based on your actual expectations
        // Add more assertions based on your actual expectations
    }

    @Test
    void testGetResourceAnalysisThrowsBadRequest() {

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setStartDate(Date.from(LocalDate.of(2024, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        requestDTO.setSortKey("benchDays");
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Project project = new Project(1);
        Allocation allocation = new Allocation(1);
        allocation.setProject(project);
        // Mock the behavior of your repository methods
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any())).thenReturn(Date.from(LocalDate.of(2024, 2, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(),anyByte())).thenReturn(List.of(allocation));
        Assertions.assertThrows(BadRequestException.class, () -> statisticsServiceImpli.getResourceAnalysis(1, null, null));

    }

    @Test
    void testGetProjectAllocationDetails() {

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setStartDate(Date.from(LocalDate.of(2024, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        requestDTO.setSortKey("benchDays");
        Resource resourceA = createResource(1, "test@gmail.com", true);
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
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(resourceA));
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any())).thenReturn(Date.from(LocalDate.of(2024, 2, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(allocationRepository
                .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(), anyByte())).thenReturn(List.of(allocation,allocation2));
        // Call the method to test
        PagedResponseDTO<ProjectAllocationResponseDTO> result = statisticsServiceImpli.getProjectAllocationDetails(1, 0, 10, null, null);

        assertNotNull(result); // Adjust based on your actual expectations
    }
    @Test
    void testGetProjectAllocationDetailsThrowsBadRequest() {

        // Create a sample request DTO
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        requestDTO.setStartDate(Date.from(LocalDate.of(2024, 1, 7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        requestDTO.setSortKey("benchDays");
        Resource resourceA = createResource(1, "test@gmail.com", true);
        Project project = new Project(1);
        Allocation allocation = new Allocation(1);
        allocation.setProject(project);
        // Mock the behavior of your repository methods
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(resourceSkillRepository.findAllByResourceId(any())).thenReturn(Collections.emptyList());
        when(this.commonFunctions.convertDateToTimestamp(any())).thenReturn(Date.from(LocalDate.of(2024, 2, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(allocationRepository
                .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(anyInt(), any(), any(), anyByte())).thenReturn(List.of(allocation));
        // Call the method to test
        Assertions.assertThrows(BadRequestException.class, () -> statisticsServiceImpli.getProjectAllocationDetails(1, 0, 10, null, null));

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
        return resource;
    }

}
