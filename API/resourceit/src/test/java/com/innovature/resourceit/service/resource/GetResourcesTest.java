package com.innovature.resourceit.service.resource;

import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterSkillAndExperienceRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceListingResponseDTO;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.RoleRepository;
import com.innovature.resourceit.service.impli.CommonServiceForResourceDownloadAndListingImpli;
import com.innovature.resourceit.service.impli.ResourceServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyByte;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = GetResourcesTest.class)
class GetResourcesTest {

    @Mock
    ParameterValidator parameterValidator;

    @Mock
    CommonServiceForResourceDownloadAndListingImpli listingImpli;
    @Mock
    ResourceRepository resourceRepository;

    @InjectMocks
    ResourceServiceImpli resourceServiceImpli;

    @Mock
    MessageSource messageSource;

    @Mock
    RoleRepository roleRepository;

    @Mock
    CommonServiceForResourceDownloadAndListingImpli listingImpl;
    @Mock
    CommonFunctions commonFunctions;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // void getResourcesTest() {
    // // Mocking your ResourceFilterRequestDTO
    // ResourceFilterRequestDTO requestDTO = mock(ResourceFilterRequestDTO.class);
    //
    // // Mocking the dependencies
    // when(parameterValidator.isNumbersNum(any(),
    // any())).thenReturn(Collections.emptyList());
    // when(parameterValidator.isNumber(any(), any())).thenReturn(0);
    //
    // // Mocking the Page and Pageable
    // Page<Object[]> demoDTOs = mock(Page.class);
    // Pageable pageable = mock(Pageable.class);
    // when(resourceRepository.findAllByAllFiltersWithSkillsAndExperience(any(),
    // any(),
    // anyInt(), any(), any(), anyInt(), anyInt(), anyByte(), any(), any(),
    // anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
    // anyInt(),
    // anyInt(), anyInt(),
    // anyInt(), anyInt(), anyInt(), anyInt(),anyInt(),
    // any(Pageable.class))).thenReturn(demoDTOs);
    //
    // // Mocking getDemoDTOs method
    // when(resourceRepository.findAllByAllFiltersWithSkillsAndExperience(any(),
    // any(),
    // anyInt(), any(), any(), anyInt(), anyInt(), anyByte(), any(), any(),
    // anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
    // anyInt(),
    // anyInt(), anyInt(),
    // anyInt(), anyInt(), anyInt(), anyInt(),anyInt(),
    // any(Pageable.class))).thenReturn(demoDTOs);
    //
    // // Mocking getResourceListResponseDTO method
    // PagedResponseDTO<ResourceListingResponseDTO> expectedResponse =
    // mock(PagedResponseDTO.class);
    // when(resourceServiceImpli.getResourceListResponseDTO(eq(demoDTOs), any(),
    // any())).thenReturn(expectedResponse);
    //
    // // Perform the test
    // PagedResponseDTO<ResourceListingResponseDTO> result =
    // resourceServiceImpli.getResources(requestDTO);
    //
    // // Add your assertions here based on the expected behavior
    // // For example:
    // // assertEquals(expectedValue, result.getSomeMethod());
    //
    // // Verify that the methods of the mocked dependencies were called as expected
    // verify(parameterValidator, times(1)).isNumbersNum("projectId",
    // requestDTO.getProjectIds());
    // verify(parameterValidator, times(1)).isNumber("pageNumber",
    // requestDTO.getPageNumber());
    // verify(parameterValidator, times(1)).isNumber("pageSize",
    // requestDTO.getPageSize());
    // verify(resourceRepository,
    // times(1)).findAllByAllFiltersWithSkillsAndExperience(
    // any(), any(), anyInt(), any(), any(), anyInt(), anyInt(), anyByte(), any(),
    // any(),
    // anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
    // anyInt(),
    // anyInt(), anyInt(),
    // anyInt(), anyInt(), anyInt(), anyInt(),anyInt(), any(Pageable.class)
    // );
    // verify(resourceServiceImpli, times(1)).getResourceListResponseDTO(demoDTOs,
    // requestDTO.getAllocationStartDate(), requestDTO.getAllocationEndDate());
    // }

    @Test
    void getResourcesTest() {
        ResourceFilterRequestDTO requestDTO = mock(ResourceFilterRequestDTO.class);
        int pageNumber = 0;
        int pageSize = 5;
        boolean sortOrder = false;
        String sortKey = "joining_date";
        List<Byte> proficiency = List.of((byte) 1);
        when(parameterValidator.isNumbersNum(any(), any())).thenReturn(Collections.emptyList());
        when(parameterValidator.isNumber(any(), any())).thenReturn(0);
        when(parameterValidator.isNumber(any(), any())).thenReturn(0);
        when(parameterValidator.isNumbersNum("projectId", requestDTO.getProjectIds())).thenReturn(null);
        // when(resourceServiceImpli.getSortKey(any())).thenReturn("joining_date");

        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                Sort.by(Boolean.FALSE.equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC,
                        sortKey));
        // ResourceFilterRequestDTO requestDTO1 = new ResourceFilterRequestDTO();
        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperiences = Arrays.asList(
                new ResourceFilterSkillAndExperienceRequestDTO("1", "2", "3", proficiency),
                new ResourceFilterSkillAndExperienceRequestDTO("4", "5", "6", proficiency)

        );
        requestDTO.setSkillAndExperiences(skillAndExperiences);
        int[] skills = {1, 4};
        int[] lowExperiences = {24, 60};
        int[] highExperiences = {35, 71};
        List<Byte>[] proficiencyStatusArray = new List[0];
        // Create sample arrays with expected values
        int[] expectedSkills = {1, 4};
        int[] expectedLowExperiences = {24, 60};
        int[] expectedHighExperiences = {35, 71};
        List<Byte>[] expectedproficiencyStatusArray = new List[0];
        // Call the method to be tested
        resourceServiceImpli.processSkillAndExperience(requestDTO.getSkillAndExperiences(), skills,
                lowExperiences,
                highExperiences,proficiencyStatusArray);

        // Assert that the arrays are initialized correctly
        Assertions.assertArrayEquals(skills, expectedSkills);
        Assertions.assertArrayEquals(lowExperiences, expectedLowExperiences);
        Assertions.assertArrayEquals(highExperiences, expectedHighExperiences);
    }

    @Test
    void testProcessSkillAndExperience() {
        List<Byte> proficiency = List.of((byte) 1);

        // Create an instance of YourClass (replace with your actual class name)
        ResourceFilterRequestDTO requestDTO = new ResourceFilterRequestDTO(); // Update with your actual class
        // name

        // Create a sample list of ResourceFilterSkillAndExperience
        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperiences = Arrays.asList(
                new ResourceFilterSkillAndExperienceRequestDTO("1", "2", "3", proficiency),
                new ResourceFilterSkillAndExperienceRequestDTO("2", "5", "7", proficiency)
                // Add more sample skill and experience data as needed
        );
        // Create arrays to store the result
        int[] skills = {1, 2}; // Assuming two elements for the test
        int[] lowExperiences = {24, 0};
        int[] highExperiences = {35, 1300};
        List[] proficiencyStatusArray = new List[5];
        // Call the method to be tested
        resourceServiceImpli.processSkillAndExperience(skillAndExperiences, skills, lowExperiences,
                highExperiences,proficiencyStatusArray);

        // Assert the results
        int[] expectedSkills = {1, 2}; // Update with expected values
        int[] expectedLowExperiences = {24, 60}; // Update with expected values
        int[] expectedHighExperiences = {36, 84}; // Update with expected values

        Assertions.assertArrayEquals(expectedSkills, skills);
        Assertions.assertArrayEquals(expectedLowExperiences, lowExperiences);
        Assertions.assertArrayEquals(expectedHighExperiences, highExperiences);
    }
    // @Test
    // public void testGetResources() {
    // // Arrange
    // ResourceFilterRequestDTO requestDTO = new ResourceFilterRequestDTO();
    // requestDTO.setProjectIds(Arrays.asList("1"));
    // requestDTO.setPageNumber("1");
    // requestDTO.setPageSize("10");
    // requestDTO.setSortKey("name");
    // requestDTO.setSortOrder(true);
    // requestDTO.setSkillAndExperiences(Arrays.asList(new
    // ResourceFilterSkillAndExperienceRequestDTO("1", "1", "5")));

    // when(parameterValidator.isNumbersNum(any(),
    // any())).thenReturn(Arrays.asList(1));
    // when(parameterValidator.isNumber(any(), any())).thenReturn(1);
    // when(resourceRepository.findAllByAllFilters(any(), any(), any(), any(),
    // any(), any(), any(), any(), any(), any())).thenReturn(new
    // PageImpl<>(Arrays.asList(new Object())));

    // // Act
    // PagedResponseDTO<ResourceListingResponseDTO> result =
    // resourceServiceImpli.getResources(requestDTO);

    // // Assert
    // assertEquals(1, result.getTotalElements());
    // }

    // @Test
    // public void testGetDemoDTOs() {
    // // Arrange
    // ResourceFilterRequestDTO requestDTO = new ResourceFilterRequestDTO();
    // requestDTO.setEmployeeId("1");
    // requestDTO.setRoleIds(Arrays.asList("1"));
    // requestDTO.setDepartmentIds(Arrays.asList("1"));
    // requestDTO.setAllocationStatus(Arrays.asList("1"));
    // requestDTO.setLowerExperience("1");
    // requestDTO.setHighExperience("5");
    // requestDTO.setStatus("1");
    // requestDTO.setBands(Arrays.asList("1"));

    // when(parameterValidator.isNumbersNum(anyString(),
    // any())).thenReturn(Arrays.asList(1));
    // when(parameterValidator.isNumber(anyString(), any())).thenReturn(1);
    // // when(resourceRepository.findAllByAllFilters(any(), any(), any(), any(),
    // any(), any(), any(), any(), any(), any())).thenReturn(new
    // PageImpl<>(Arrays.asList(new Object[]{})));
    // // Act
    // when(resourceRepository.findAllByAllFilters(any(), any(), any(), any(),
    // any(), any(), any(), any(), any(), any())).thenReturn(new
    // PageImpl<>(Arrays.asList(new Object[]{}, new Object[]{})));// Assert
    // // Act
    // Page<Object[]> result = resourceServiceImpli.getDemoDTOs(requestDTO,
    // Arrays.asList(1), Pageable.unpaged(), new int[]{1}, new int[]{1}, new
    // int[]{1});
    // assertEquals(1, result.getTotalElements());
    // }

    @Test
    void testGetResourcesFiltersWithProjectNameSkillsAndExperience() {
        ResourceFilterRequestDTO requestDTO = new ResourceFilterRequestDTO();
        requestDTO.setName("John Doe");
        requestDTO.setEmployeeId("123");
        requestDTO.setRoleIds(Arrays.asList("1"));
        requestDTO.setDepartmentIds(Arrays.asList("1"));
        requestDTO.setProjectIds(Arrays.asList("1"));
        requestDTO.setAllocationStatus(Arrays.asList("1"));
        requestDTO.setLowerExperience("1");
        requestDTO.setHighExperience("5");
        requestDTO.setStatus("1");
        requestDTO.setSortOrder(true);
        ResourceFilterSkillAndExperienceRequestDTO skillAndExperienceRequestDTO = new ResourceFilterSkillAndExperienceRequestDTO();
        skillAndExperienceRequestDTO.setSkillId("1");
        skillAndExperienceRequestDTO.setSkillMaxValue("2");
        skillAndExperienceRequestDTO.setSkillMinValue("3");
        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperienceList = new ArrayList<>();
        skillAndExperienceList.add(skillAndExperienceRequestDTO);

        requestDTO.setSkillAndExperiences(skillAndExperienceList);
        String sortKey = "joining_date";
        requestDTO.setSortKey(sortKey);

        when(parameterValidator.isNumbersNum(any(), any())).thenReturn(new ArrayList<>());
        when(parameterValidator.isNumber(any(), any())).thenReturn(2);
        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(Arrays.asList(1));

        int pageNumber = 1;
        int pageSize = 10;

        when(parameterValidator.isNumber(eq("pageNumber"), anyString())).thenReturn(pageNumber);
        when(parameterValidator.isNumber(eq("pageSize"), anyString())).thenReturn(pageSize);

        Page<Object[]> pageMock = Mockito.mock(Page.class);

        when(resourceRepository.findAllByAllFiltersWithProjectNameSkillsAndExperience(anyString(), anyList(),
                anyInt(), anyList(), anyInt(), anyInt(), anyByte(), anyList(), anyInt(),
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyList(), anyList(),anyList(),anyList(),anyList(),anyList(),
                any(Pageable.class)))
                .thenReturn(pageMock);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize,
                Sort.by(requestDTO.getSortOrder() ? Sort.Direction.ASC : Sort.Direction.DESC, sortKey));
        PagedResponseDTO<ResourceListingResponseDTO> getResources = resourceServiceImpli
                .getResources(requestDTO);
        Page<Object[]> result = resourceServiceImpli.getDemoDTOs(requestDTO, List.of(1), pageRequest,
                new int[5], new int[5], new int[5],new List[5]);
        verify(resourceRepository, times(2)).findAllByAllFiltersWithProjectNameSkillsAndExperience(anyString(),
                anyList(), anyInt(), anyList(), anyInt(), anyInt(), anyByte(), anyList(),
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyList(), anyList(),anyList(),anyList(),anyList(),anyList(),
                any(Pageable.class));

        assertNotNull(getResources);

        resourceServiceImpli.getResourceListResponseDTO(result);

    }

//    @Test
//    void testGetResourcesFilterWithSkillsAndExperience() {
//        ResourceFilterRequestDTO requestDTO = new ResourceFilterRequestDTO();
//        requestDTO.setName("John Doe");
//        requestDTO.setEmployeeId("123");
//        requestDTO.setRoleIds(Arrays.asList("1"));
//        requestDTO.setDepartmentIds(Arrays.asList("1"));
//        requestDTO.setProjectIds(Arrays.asList(""));
//        requestDTO.setAllocationStatus(Arrays.asList("1"));
//        requestDTO.setAllocationType(Arrays.asList("1"));
//        requestDTO.setLowerExperience("1");
//        requestDTO.setHighExperience("5");
//        requestDTO.setStatus("1");
//        requestDTO.setSortOrder(true);
//        ResourceFilterSkillAndExperienceRequestDTO skillAndExperienceRequestDTO = new ResourceFilterSkillAndExperienceRequestDTO();
//        skillAndExperienceRequestDTO.setSkillId("1");
//        skillAndExperienceRequestDTO.setSkillMaxValue("2");
//        skillAndExperienceRequestDTO.setSkillMinValue("3");
//        List<Byte>proficiencyStatus = Arrays.asList((byte)0);
//        skillAndExperienceRequestDTO.setProficiency(proficiencyStatus);
//        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperienceList = new ArrayList<>();
//        skillAndExperienceList.add(skillAndExperienceRequestDTO);
//
//        requestDTO.setSkillAndExperiences(skillAndExperienceList);
//        String sortKey = "joining_date";
//        requestDTO.setSortKey(sortKey);
//
//        // when(parameterValidator.isNumbersNum(any(), any())).thenReturn(new
//        // ArrayList<>());
//        when(parameterValidator.isNumber(any(), any())).thenReturn(2);
//        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(Arrays.asList(1));
//
//        int pageNumber = 1;
//        int pageSize = 10;
//        when(parameterValidator.isNumber(eq("pageNumber"), anyString())).thenReturn(pageNumber);
//        when(parameterValidator.isNumber(eq("pageSize"), anyString())).thenReturn(pageSize);
////        when(resourceServiceImpli.getProficiencyStatus()).thenReturn(new ArrayList<>());
//        Page<Object[]> pageMock = Mockito.mock(Page.class);
//        when(resourceRepository.findAllByAllFiltersWithSkillsAndExperience(anyString(), anyList(), anyInt(),
//                anyList(), anyInt(), anyInt(), anyByte(), anyList(), anyInt(), anyInt(),
//                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
//                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyList(), any(Pageable.class)))
//                .thenReturn(pageMock);
//        PageRequest pageRequest = PageRequest.of(0, pageSize,
//                Sort.by(requestDTO.getSortOrder() ? Sort.Direction.ASC : Sort.Direction.DESC, sortKey));
//        // PagedResponseDTO<ResourceListingResponseDTO> getResources =
//        // resourceServiceImpli.getResources(requestDTO);
//        Page<Object[]> result = resourceServiceImpli.getDemoDTOs(requestDTO, Collections.emptyList(), pageRequest, new int[5],
//                new int[5], new int[5]);
//
//        verify(resourceRepository, times(1)).findAllByAllFiltersWithSkillsAndExperience(
//                anyString(), anyList(), anyInt(), anyList(),
//                anyInt(), anyInt(), anyByte(), anyList(), anyInt(),
//                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
//                anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(),
//                anyInt(), anyInt(),anyList(), any(Pageable.class));
//    }

    @Test
    void testGetResourcesAllFilter() {
        ResourceFilterRequestDTO requestDTO = new ResourceFilterRequestDTO();
        requestDTO.setName("John Doe");
        requestDTO.setEmployeeId("123");
        requestDTO.setRoleIds(Arrays.asList("1"));
        requestDTO.setDepartmentIds(Arrays.asList("1"));
        requestDTO.setProjectIds(Arrays.asList("1"));
        requestDTO.setAllocationStatus(Arrays.asList("1"));
        requestDTO.setLowerExperience("1");
        requestDTO.setHighExperience("5");
        requestDTO.setStatus("1");
        requestDTO.setSortOrder(true);
        ResourceFilterSkillAndExperienceRequestDTO skillAndExperienceRequestDTO = new ResourceFilterSkillAndExperienceRequestDTO();
        skillAndExperienceRequestDTO.setSkillId("");
        skillAndExperienceRequestDTO.setSkillMaxValue("");
        skillAndExperienceRequestDTO.setSkillMinValue("");
        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperienceList = new ArrayList<>();
        skillAndExperienceList.add(skillAndExperienceRequestDTO);

        requestDTO.setSkillAndExperiences(null);
        String sortKey = "joining_date";
        requestDTO.setSortKey(sortKey);

        when(parameterValidator.isNumbersNum(any(), any())).thenReturn(new ArrayList<>());
        when(parameterValidator.isNumber(any(), any())).thenReturn(2);
        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(Arrays.asList(1));

        int pageNumber = 1;
        int pageSize = 10;

        when(parameterValidator.isNumber(eq("pageNumber"), anyString())).thenReturn(pageNumber);
        when(parameterValidator.isNumber(eq("pageSize"), anyString())).thenReturn(pageSize);

        Page<Object[]> pageMock = Mockito.mock(Page.class);
        when(resourceRepository.findAllByAllFilters(anyString(), anyList(), anyInt(), anyList(),
                anyInt(), anyInt(), anyByte(), anyList(), any(Pageable.class)))
                .thenReturn(pageMock);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize,
                Sort.by(requestDTO.getSortOrder() ? Sort.Direction.ASC : Sort.Direction.DESC, sortKey));
        // PagedResponseDTO<ResourceListingResponseDTO> getResources =
        // resourceServiceImpli.getResources(requestDTO);
        // Page<Object[]> result = resourceServiceImpli.getDemoDTOs(requestDTO, null,
        // pageRequest, new int[5], new int[5], new int[5]);
        Page<Object[]> result = resourceServiceImpli.getDemoDTOs(requestDTO, Collections.emptyList(), pageRequest, new int[5],
                new int[5], new int[5],new List[0]);

        // Add logging statements to check which methods are being called
        System.out.println("findAllByAllFilters invocation:");
        verify(resourceRepository, times(1)).findAllByAllFilters(anyString(), anyList(), anyInt(), anyList(),
                anyInt(), anyInt(), anyByte(), anyList(), any(Pageable.class));

    }

    @Test
    void testGetResourcesAllFilterWithProjectName() {
        ResourceFilterRequestDTO requestDTO = new ResourceFilterRequestDTO();
        requestDTO.setName("John Doe");
        requestDTO.setEmployeeId("123");
        requestDTO.setRoleIds(Arrays.asList("1"));
        requestDTO.setDepartmentIds(Arrays.asList("1"));
        requestDTO.setProjectIds(Arrays.asList("1"));
        requestDTO.setAllocationStatus(Arrays.asList("1"));
        requestDTO.setLowerExperience("1");
        requestDTO.setHighExperience("5");
        requestDTO.setStatus("1");
        requestDTO.setSortOrder(true);
        ResourceFilterSkillAndExperienceRequestDTO skillAndExperienceRequestDTO = new ResourceFilterSkillAndExperienceRequestDTO();
        skillAndExperienceRequestDTO.setSkillId("");
        skillAndExperienceRequestDTO.setSkillMaxValue("");
        skillAndExperienceRequestDTO.setSkillMinValue("");
        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperienceList = new ArrayList<>();
        skillAndExperienceList.add(skillAndExperienceRequestDTO);

        requestDTO.setSkillAndExperiences(null);
        String sortKey = "joining_date";
        requestDTO.setSortKey(sortKey);

        when(parameterValidator.isNumbersNum(any(), any())).thenReturn(new ArrayList<>());
        when(parameterValidator.isNumber(any(), any())).thenReturn(2);
        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(Arrays.asList(1));

        int pageNumber = 1;
        int pageSize = 10;

        when(parameterValidator.isNumber(eq("pageNumber"), anyString())).thenReturn(pageNumber);
        when(parameterValidator.isNumber(eq("pageSize"), anyString())).thenReturn(pageSize);

        Page<Object[]> pageMock = Mockito.mock(Page.class);
        when(resourceRepository.findAllByAllFilters(anyString(), anyList(), anyInt(), anyList(),
                anyInt(), anyInt(), anyByte(), anyList(), any(Pageable.class)))
                .thenReturn(pageMock);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize,
                Sort.by(requestDTO.getSortOrder() ? Sort.Direction.ASC : Sort.Direction.DESC, sortKey));
        // PagedResponseDTO<ResourceListingResponseDTO> getResources =
        // resourceServiceImpli.getResources(requestDTO);
        // Page<Object[]> result = resourceServiceImpli.getDemoDTOs(requestDTO, null,
        // pageRequest, new int[5], new int[5], new int[5]);
        Page<Object[]> result = resourceServiceImpli.getDemoDTOs(requestDTO, List.of(1), pageRequest,
                new int[5], new int[5], new int[5],new List[5]);

        // Add logging statements to check which methods are being called
        verify(resourceRepository, times(1)).findAllByAllFiltersWithProjectName(anyString(), anyList(),
                anyInt(), anyList(), anyInt(), anyInt(), anyByte(), anyList(), anyList(),
                any(Pageable.class));
    }

    @Test
    void getResourceListResponseDtoTest() {
        ResourceFilterRequestDTO requestDTO = new ResourceFilterRequestDTO();
        requestDTO.setName("John Doe");
        requestDTO.setEmployeeId("123");
        requestDTO.setRoleIds(Arrays.asList("1"));
        requestDTO.setDepartmentIds(Arrays.asList("1"));
        requestDTO.setProjectIds(Arrays.asList("1"));
        requestDTO.setAllocationStatus(Arrays.asList("1"));
        requestDTO.setLowerExperience("1");
        requestDTO.setHighExperience("5");
        requestDTO.setStatus("1");
        requestDTO.setSortOrder(true);
        ResourceFilterSkillAndExperienceRequestDTO skillAndExperienceRequestDTO = new ResourceFilterSkillAndExperienceRequestDTO();
        skillAndExperienceRequestDTO.setSkillId("");
        skillAndExperienceRequestDTO.setSkillMaxValue("");
        skillAndExperienceRequestDTO.setSkillMinValue("");
        List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperienceList = new ArrayList<>();
        skillAndExperienceList.add(skillAndExperienceRequestDTO);

        requestDTO.setSkillAndExperiences(skillAndExperienceList);
        String sortKey = "joining_date";
        requestDTO.setSortKey(sortKey);

        when(parameterValidator.isNumbersNum(any(), any())).thenReturn(new ArrayList<>());
        when(parameterValidator.isNumber(any(), any())).thenReturn(2);
        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(Arrays.asList(1));

        int pageNumber = 1;
        int pageSize = 10;

        when(parameterValidator.isNumber(eq("pageNumber"), anyString())).thenReturn(pageNumber);
        when(parameterValidator.isNumber(eq("pageSize"), anyString())).thenReturn(pageSize);

        Page<Object[]> pageMock = Mockito.mock(Page.class);
        when(resourceRepository.findAllByAllFilters(anyString(), anyList(), anyInt(), anyList(),
                anyInt(), anyInt(), anyByte(), anyList(), any(Pageable.class)))
                .thenReturn(pageMock);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, Sort.by(requestDTO.getSortOrder() ? Sort.Direction.ASC : Sort.Direction.DESC, sortKey));
        when(resourceRepository.findAllByAllFiltersWithProjectNameSkillsAndExperience(anyString(), anyList(), anyInt(), anyList(), anyInt(), anyInt(), anyByte(), anyList(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyList(), anyList(), anyList(), anyList(), anyList(), anyList(), any(Pageable.class)))
                .thenReturn(pageMock);


        Page<Object[]> result = resourceServiceImpli.getDemoDTOs(requestDTO, List.of(1), pageRequest, new int[5], new int[5], new int[5],new List[5]);

        List<Object[]> contentList = new ArrayList<>();
        Object[] sampleResult = new Object[14];
        sampleResult[12] = 1;  // DepartmentId
        sampleResult[11] = 2;  // Role
        sampleResult[7] = "AllocationStatus";
        sampleResult[3] = "ProjectName";
        sampleResult[2] = "DepartmentName";
        sampleResult[6] = "email@example.com";
        sampleResult[0] = 123;  // Assuming id is an integer
        sampleResult[10] = 456; // Assuming employeeId is an integer
        sampleResult[1] = "John Doe";
        sampleResult[4] = 3;    // Assuming totalExperience is an integer
        sampleResult[9] = (byte) 1;  // Assuming status is a byte
//        sampleResult[13] = Date.valueOf(LocalDate.now().minusDays(10)); // Assuming joiningDate is a Date
        sampleResult[13] = new java.util.Date();
        contentList.add(sampleResult);


        Page<Object[]> pageMock1 = new PageImpl<>(contentList);
        PagedResponseDTO<ResourceListingResponseDTO> resourceListResponseDTO = resourceServiceImpli.getResourceListResponseDTO(pageMock1);
        assertNotNull(resourceListResponseDTO);
    }

    @Test
    void getSortKeyTest() {
        String sortKey = "joining_date";
        String result = resourceServiceImpli.getSortKey(sortKey);
        Assert.assertEquals("joining_date", result);
    }


    @Test
    void getSortKeyTestCaseEmpId() {
        String sortKey = "empId";
        String result = resourceServiceImpli.getSortKey(sortKey);
        assertEquals("employee_id", result);
    }

    @Test
    void getSortKeyTestcase() {
        String sortKey = "resourceName";
        String result = resourceServiceImpli.getSortKey(sortKey);
        assertEquals("name", result);
    }

    @Test
    void getSortKeyTestcaseNull() {
        String result = resourceServiceImpli.getSortKey(null);
        assertEquals("joining_date", result);
    }


}
