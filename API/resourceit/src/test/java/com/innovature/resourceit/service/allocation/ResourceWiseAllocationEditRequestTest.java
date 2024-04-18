package com.innovature.resourceit.service.allocation;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.criteriaquery.AllocationRepositoryCriteria;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.AllocationRequestResourceFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceAllocationRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterSkillAndExperienceRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.AllocationRequestResourceFilterResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;
import com.innovature.resourceit.service.impli.CommonServiceForResourceDownloadAndListingImpli;
import com.innovature.resourceit.util.CommonFunctions;
import com.innovature.resourceit.util.EmailUtils;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = ResourceWiseAllocationEditRequestTest.class)
class ResourceWiseAllocationEditRequestTest {

    @InjectMocks
    private AllocationServiceImpli allocationService;
    @Mock
    private EmailUtils emailUtils;
    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;

    @Mock
    private CommonFunctions commonFunctions;
    @Mock
    private MessageSource messageSource;

    private MockedStatic<SecurityUtil> mockedSecurityUtil;

    @Mock
    private ParameterValidator parameterValidator;

    @Mock
    CommonServiceForResourceDownloadAndListingImpli listingImpli;

    @Mock
    private ResourceSkillRepository resourceSkillRepository;

    @Mock
    AllocationRepositoryCriteria allocationRepositoryCriteria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void testResourceWiseAllocationEditRequest() {
        // Mock the required objects
        ResourceAllocationRequestDTO dto = createMockRequestDTO();

        Resource requestedBy = createMockResource();
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(requestedBy));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(new Project()));
        Allocation allocation = createMockAllocation();
        dto.setAllocationId(allocation.getId());
        Role role = new Role("HOD");
        requestedBy.setRole(role);
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(allocation));

        when(commonFunctions.checkRequestConflicts(any(), any(), any(), any())).thenReturn(true);

        // Test the method
        allocationService.resourceWiseAllocationEditRequest(dto);

        // Verify that the save method is called
        verify(resourceAllocationRequestRepository, times(1)).save(any());
    }

    @Test
    void testResourceWiseAllocationEditRequestHR() {
        // Mock the required objects
        ResourceAllocationRequestDTO dto = createMockRequestDTO();

        Resource requestedBy = createMockResource();
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(requestedBy));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(new Project()));

        Allocation allocation = createMockAllocation();
        dto.setAllocationId(allocation.getId());
        Role role = new Role("HR");
        requestedBy.setRole(role);
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(allocation));

        when(commonFunctions.checkRequestConflicts(any(), any(), any(), any())).thenReturn(true);

        // Test the method
        allocationService.resourceWiseAllocationEditRequest(dto);

        // Verify that the save method is called
        verify(resourceAllocationRequestRepository, times(1)).save(any());
    }

    @Test
    void testResourceWiseAllocationEditRequestDefault() {
        // Mock the required objects
        ResourceAllocationRequestDTO dto = createMockRequestDTO();

        Resource requestedBy = createMockResource();
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(requestedBy));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(new Project()));

        Allocation allocation = createMockAllocation();
        dto.setAllocationId(allocation.getId());
        Role role = new Role("PM");
        requestedBy.setRole(role);
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(allocation));

        when(commonFunctions.checkRequestConflicts(any(), any(), any(), any())).thenReturn(true);

        // Test the method
        allocationService.resourceWiseAllocationEditRequest(dto);

        // Verify that the save method is called
        verify(resourceAllocationRequestRepository, times(1)).save(any());
    }

    @Test
    void testResourceWiseAllocationEditRequestAllocationNotFound() {
        // Mock the required objects
        ResourceAllocationRequestDTO dto = createMockRequestDTO();

        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(createMockResource()));
        when(allocationRepository.findByIdAndStatus(any(), any())).thenReturn(Optional.empty());

        // Test the method and expect BadRequestException
        assertThrows(BadRequestException.class, () -> allocationService.resourceWiseAllocationEditRequest(dto));

        // Verify that the save method is not called
        verify(resourceAllocationRequestRepository, never()).save(any());
    }

    @Test
    void testResourceWiseAllocationEditRequestConflictsInRequests() {
        // Mock the required objects
        ResourceAllocationRequestDTO dto = createMockRequestDTO();

        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(createMockResource()));
        when(allocationRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(createMockAllocation()));
        when(commonFunctions.checkRequestConflicts(any(), any(), any(), any())).thenReturn(false);

        // Test the method and expect BadRequestException
        assertThrows(BadRequestException.class, () -> allocationService.resourceWiseAllocationEditRequest(dto));

        // Verify that the save method is not called
        verify(resourceAllocationRequestRepository, never()).save(any());
    }

    private ResourceAllocationRequestDTO createMockRequestDTO() {
        // Create and return a mock ResourceAllocationRequestDTO
        ResourceAllocationRequestDTO dto = new ResourceAllocationRequestDTO();
        dto.setProjectId(1);
        return dto;
    }

    private Resource createMockResource() {
        // Create and return a mock Resource
        Resource resource = new Resource();
        resource.setEmail("test@gmail.com");
        return resource;
    }

    private Allocation createMockAllocation() {
        // Create and return a mock Allocation
        Allocation allocation = new Allocation();
        allocation.setId(1);
        return allocation;
    }

    @Test
    void testListResourcesForAllocation() {

        List<Integer> departments = List.of(1);

        List<ResourceFilterSkillAndExperienceRequestDTO> skillsAndExperiences = new ArrayList<>();

        AllocationRequestResourceFilterRequestDTO arrfrdto = new AllocationRequestResourceFilterRequestDTO();
        arrfrdto.setPageNumber(0);
        arrfrdto.setPageSize(10);
        arrfrdto.setDepartmentIds(departments);
        arrfrdto.setExperienceMaxValue(1300);
        arrfrdto.setExperienceMinValue(0);
        arrfrdto.setSkillsAndExperiences(skillsAndExperiences);
        arrfrdto.setResourceName("test");

        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "joiningDate");
        Department d = new Department();
        d.setDepartmentId(1);
        d.setName("Software");
        Resource r = new Resource(1);
        r.setDepartment(d);
        r.setExperience(12);
        List<Resource> resources = List.of(r);
        Skill sk = new Skill(1, "java");
        ResourceSkill rs = new ResourceSkill();
        rs.setId(1);
        rs.setSkill(sk);
        rs.setExperience(48);
        List<ResourceSkill> resourceSkills = List.of(rs);


        when(allocationRepositoryCriteria.findFilteredResourceAllocationWithPagination(arrfrdto)).thenReturn(resources);
        when(resourceSkillRepository.findAllByResourceId(r.getId())).thenReturn(resourceSkills);

        PagedResponseDTO<AllocationRequestResourceFilterResponseDTO> results = allocationService.listResourcesForAllocation(arrfrdto);

        Assertions.assertEquals(resources.size(), results.getItems().size());

    }
}
