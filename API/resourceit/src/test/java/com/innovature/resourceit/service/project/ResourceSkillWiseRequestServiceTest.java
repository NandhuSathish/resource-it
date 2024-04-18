/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.ResourceAllocationRequest;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceSkillWiseAllocationRequest;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.entity.SkillExperience;
import com.innovature.resourceit.entity.criteriaquery.ProjectRepositoryCriteria;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.*;
import com.innovature.resourceit.entity.dto.responsedto.AllocationConflictsByResourceResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.AllocationResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceAllocationResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceSkillWiseAllocationResponseListDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.DepartmentRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceAllocationRequestRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.ResourceSkillWiseAllocationRequestRepository;
import com.innovature.resourceit.repository.SkillRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;
import com.innovature.resourceit.service.impli.CommonServiceForResourceDownloadAndListingImpli;
import com.innovature.resourceit.util.CommonFunctions;
import com.innovature.resourceit.util.CustomValidator;

import java.text.ParseException;
import java.util.*;

import com.innovature.resourceit.util.EmailUtils;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = ResourceSkillWiseRequestServiceTest.class)
class ResourceSkillWiseRequestServiceTest {

    @InjectMocks
    AllocationServiceImpli allocationServiceImpli;
    @Mock
    ProjectRepository projectRepository;

    @Mock
    MessageSource messageSource;

    @Mock
    CustomValidator customValidator;

    @Mock
    ParameterValidator parameterValidator;

    @Mock
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;

    @Mock
    ProjectRepositoryCriteria projectRepositoryCriteria;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CommonServiceForResourceDownloadAndListingImpli listingImpli;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private CommonFunctions commonFunctions;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private EmailUtils emailUtils;

    @Mock
    ResourceSkillWiseAllocationRequestRepository skillWiseAllocationRequestRepository;

    private MockedStatic<SecurityUtil> mockedSecurityUtil;

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
    void testSaveSkillWiseResourceRequest() throws ParseException {

        Resource r = new Resource(1);
        Role ro = new Role();
        ro.setId(1);
        ro.setName("RESOURCE");
        r.setRole(ro);
        when(resourceRepository.findByEmailAndStatus("test@gmail.com", Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(r));
        ResourceSkillWiseAllocationRequestDTO requestDTO = createValidRequestDTO();
        when(parameterValidator.isNumber(anyString(), anyString())).thenReturn(1); // Mock isNumber method
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(new Project()));
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.of(new Department()));
        when(skillRepository.findById(anyInt())).thenReturn(Optional.of(new Skill()));
        when(listingImpli.getDateFromStringHyphen(anyString())).thenReturn(new Date());
        when(resourceRepository.findAllIdByRoleIdAndStatus(Resource.Roles.HOD.getId(), Resource.Status.ACTIVE.value)).thenReturn(List.of(1));
        when(skillWiseAllocationRequestRepository.save(any())).thenReturn(new ResourceSkillWiseAllocationRequest());
        allocationServiceImpli.saveSkillWiseResourceRequest(requestDTO);

        verify(skillWiseAllocationRequestRepository, times(1)).save(any(ResourceSkillWiseAllocationRequest.class));
    }

    @Test
    void testSaveSkillWiseResourceRequest_InvalidSkill() throws ParseException {

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);

        resource.setRole(ro);
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(resource));
        // Given
        ResourceSkillWiseAllocationRequestDTO requestDTO = createInvalidSkillRequestDTO();
        when(parameterValidator.isNumber(anyString(), anyString())).thenReturn(1); // Mock isNumber method
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(new Project()));
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.of(new Department()));
        when(skillRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(listingImpli.getDateFromStringHyphen(anyString())).thenReturn(new Date());

        // When, Then
        assertThrows(BadRequestException.class, () -> allocationServiceImpli.saveSkillWiseResourceRequest(requestDTO));
        // Verify that save method of skillWiseAllocationRequestRepository was not
        // called
        verify(skillWiseAllocationRequestRepository, never()).save(any());
    }

    // Create a valid request DTO for testing
    private ResourceSkillWiseAllocationRequestDTO createValidRequestDTO() {
        ResourceSkillWiseAllocationRequestDTO requestDTO = new ResourceSkillWiseAllocationRequestDTO();
        // Set fields...
        ResourceFilterSkillAndExperienceRequestDTO skillDTO = new ResourceFilterSkillAndExperienceRequestDTO();
        skillDTO.setSkillId("1");
        skillDTO.setSkillMinValue("2");
        skillDTO.setSkillMaxValue("3");
        requestDTO.setSkillExperienceRequestDTO(Collections.singletonList(skillDTO));
        return requestDTO;
    }

    // Create an invalid request DTO with missing skillId for testing
    private ResourceSkillWiseAllocationRequestDTO createInvalidSkillRequestDTO() {
        ResourceSkillWiseAllocationRequestDTO requestDTO = createValidRequestDTO();
        requestDTO.getSkillExperienceRequestDTO().get(0).setSkillId(null);
        return requestDTO;
    }

    @Test
    void testListSkillWiseResourceRequestInValid() {

        boolean isRequestList = true;

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);

        resource.setRole(ro);
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(resource));
        // Given
        ResourceSkillWiseAllocationRequestListDTO requestDTO = createValidRequestsDTO();
        when(parameterValidator.isNumber(anyString(), anyString())).thenReturn(1);
        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(Collections.singletonList(1));

        Page<ResourceSkillWiseAllocationRequest> mockPagedList = createMockPagedListInValid();
        when(projectRepositoryCriteria.findFilteredResourceSkillWiseAllocationWithPagination(anyList(), anyList(),
                anyList(), anyList(),
                any(Pageable.class)))
                .thenReturn(mockPagedList);

        when(messageSource.getMessage(eq("SKILL_WISE_ALLOCATION_LIST_FETCHING_FAILED"), eq(null), any()))
                .thenReturn("2066-Skill wise listing failed");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            allocationServiceImpli.listSkillWiseResourceRequest(requestDTO, isRequestList);
        });

        assertEquals("2066-Skill wise listing failed", exception.getBody().getDetail());

    }

    @Test
    void testListSkillWiseResourceRequestAllocationStatus() {
        boolean isRequestList = true;

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);
        ro.setName("HOD");

        List<String> approvalStatus = Arrays.asList("0");
        List<Integer> approvalStatusInt = Arrays.asList(0);

        resource.setRole(ro);
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(resource));
        // Given
        ResourceSkillWiseAllocationRequestListDTO requestDTO = createValidRequestsDTO();
        when(parameterValidator.isNumber(anyString(), anyString())).thenReturn(1);
        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(Collections.singletonList(1));
        when(parameterValidator.isNumbersNum("approvalStatus", approvalStatus)).thenReturn(approvalStatusInt);

        Page<ResourceSkillWiseAllocationRequest> mockPagedList = createMockPagedList();
        when(projectRepositoryCriteria.findFilteredResourceSkillWiseAllocationWithPagination(anyList(), anyList(),
                anyList(), anyList(),
                any(Pageable.class)))
                .thenReturn(mockPagedList);

        // When
        PagedResponseDTO<ResourceSkillWiseAllocationResponseListDTO> responseDTO = allocationServiceImpli
                .listSkillWiseResourceRequest(requestDTO, isRequestList);

        // Then
        assertEquals(1, responseDTO.getItems().size());
        assertEquals(1, responseDTO.getTotalPages());
        assertEquals(1, responseDTO.getTotalItems());
        assertEquals(0, responseDTO.getCurrentPage());
    }

    @Test
    void testListSkillWiseResourceRequestAllocationStatusDateNull() {
        boolean isRequestList = true;

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);
        ro.setName("HOD");

        List<String> approvalStatus = Arrays.asList("0");
        List<Integer> approvalStatusInt = Arrays.asList(0);

        resource.setRole(ro);
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(resource));
        // Given
        ResourceSkillWiseAllocationRequestListDTO requestDTO = createValidRequestsDTO();
        when(parameterValidator.isNumber(anyString(), anyString())).thenReturn(1);
        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(Collections.singletonList(1));
        when(parameterValidator.isNumbersNum("approvalStatus", approvalStatus)).thenReturn(approvalStatusInt);

        Page<ResourceSkillWiseAllocationRequest> mockPagedList = createMockPagedListDateNull();
        when(projectRepositoryCriteria.findFilteredResourceSkillWiseAllocationWithPagination(anyList(), anyList(),
                anyList(), anyList(),
                any(Pageable.class)))
                .thenReturn(mockPagedList);

        // When
        PagedResponseDTO<ResourceSkillWiseAllocationResponseListDTO> responseDTO = allocationServiceImpli
                .listSkillWiseResourceRequest(requestDTO, isRequestList);

        // Then
        assertEquals(1, responseDTO.getItems().size());
        assertEquals(1, responseDTO.getTotalPages());
        assertEquals(1, responseDTO.getTotalItems());
        assertEquals(0, responseDTO.getCurrentPage());
    }

    @Test
    void testListSkillWiseResourceRequest() {
        boolean isRequestList = false;
        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role("HOD");
        ro.setId(1);

        resource.setRole(ro);
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(resource));
        // Given
        ResourceSkillWiseAllocationRequestListDTO requestDTO = createValidRequestsDTO();
        when(parameterValidator.isNumber(anyString(), anyString())).thenReturn(1);
        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(Collections.singletonList(1));

        Page<ResourceSkillWiseAllocationRequest> mockPagedList = createMockPagedList();
        when(projectRepositoryCriteria.findFilteredResourceSkillWiseAllocationWithPagination(anyList(), anyList(),
                anyList(), anyList(),
                any(Pageable.class)))
                .thenReturn(mockPagedList);

        // When
        PagedResponseDTO<ResourceSkillWiseAllocationResponseListDTO> responseDTO = allocationServiceImpli
                .listSkillWiseResourceRequest(requestDTO, isRequestList);

        // Then
        assertEquals(1, responseDTO.getItems().size());
        assertEquals(1, responseDTO.getTotalPages());
        assertEquals(1, responseDTO.getTotalItems());
        assertEquals(0, responseDTO.getCurrentPage());
    }

    // Create a valid request DTO for testing
    private ResourceSkillWiseAllocationRequestListDTO createValidRequestsDTO() {
        ResourceSkillWiseAllocationRequestListDTO requestDTO = new ResourceSkillWiseAllocationRequestListDTO();
        requestDTO.setPageNumber("0");
        requestDTO.setPageSize("1");
        requestDTO.setDepartmentIds(Collections.singletonList("1"));
        requestDTO.setApprovalStatus(Collections.singletonList("0"));
        requestDTO.setProjectIds(Collections.singletonList("1"));
        return requestDTO;
    }

    private Page<ResourceSkillWiseAllocationRequest> createMockPagedListInValid() {
        List<ResourceSkillWiseAllocationRequest> content = new ArrayList<>();
        content.add(new ResourceSkillWiseAllocationRequest());
        return new PageImpl<>(content);
    }

    private Page<ResourceSkillWiseAllocationRequest> createMockPagedList() {
        List<ResourceSkillWiseAllocationRequest> content = new ArrayList<>();
        Resource r = new Resource(1);
        r.setName("test");
        ResourceSkillWiseAllocationRequest rswar = new ResourceSkillWiseAllocationRequest();
        rswar.setId(1);
        rswar.setProject(new Project());
        rswar.setStartDate(new Date(1234567));
        rswar.setEndDate(new Date(1234599));
        rswar.setDepartment(new Department());
        rswar.setRequestedBy(r);
        Set<SkillExperience> skillExperiences = new HashSet<>();
        skillExperiences.add(new SkillExperience(1, 2, 3, "Java", List.of((byte) 1)));
        rswar.setSkillExperiences(skillExperiences);
        content.add(rswar);
        return new PageImpl<>(content);
    }

    private Page<ResourceSkillWiseAllocationRequest> createMockPagedListDateNull() {
        List<ResourceSkillWiseAllocationRequest> content = new ArrayList<>();
        Resource r = new Resource(1);
        r.setName("test");
        ResourceSkillWiseAllocationRequest rswar = new ResourceSkillWiseAllocationRequest();
        rswar.setId(1);
        rswar.setProject(new Project());
        rswar.setStartDate(null);
        rswar.setEndDate(null);
        rswar.setDepartment(new Department());
        rswar.setRequestedBy(r);
        Set<SkillExperience> skillExperiences = new HashSet<>();
        skillExperiences.add(new SkillExperience(1, 1, 1, "Java", List.of((byte) 1)));
        rswar.setSkillExperiences(skillExperiences);
        content.add(rswar);
        return new PageImpl<>(content);
    }

    @Test
    void testAddResourceWiseAllocationRequest() {
        List<ResourceAllocationRequestDTO> dtoList = createMockDTOList();

        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(createMockResource()));

        when(commonFunctions.checkRequestConflicts(any(), any(), any(), any()))
                .thenReturn(Boolean.TRUE);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(new Project()));

        allocationServiceImpli.addResourceWiseAllocationRequest(dtoList);
        verify(resourceAllocationRequestRepository, times(1)).saveAll(anyList());
    }

    private List<ResourceAllocationRequestDTO> createMockDTOList() {
        ResourceAllocationRequestDTO dto = new ResourceAllocationRequestDTO();
        dto.setResourceId(1);
        dto.setStartDate(new Date());
        dto.setEndDate(new Date());
        dto.setProjectId(1);
        return Collections.singletonList(dto);
    }

    private Resource createMockResource() {
        Role role = new Role("HOD");
        Resource resource = new Resource(1);
        resource.setRole(role);
        return resource;
    }

    @Test
    void testListResourceAllocationRequests() {

        Set<Byte> approvalStatus = new HashSet<>();
        Set<Integer> projectIds = new HashSet<>();
        Set<Integer> departmentIds = new HashSet<>();
        Set<Integer> requestedBys = new HashSet<>();
        String searchKey = "test";
        Boolean isRequestList = null;
        Resource resource = new Resource(1);
        resource.setEmail("test@gmail.com");
        Role role = new Role("HOD");
        resource.setRole(role);
        Pageable pageable = PageRequest.of(0, 20);

        when(resourceAllocationRequestRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(createMockPage());
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte()))
                .thenReturn(Optional.of(resource));

        Page<ResourceAllocationResponseDTO> resultPage = allocationServiceImpli.listResourceAllocationRequests(
                approvalStatus, departmentIds, projectIds, requestedBys, searchKey, isRequestList, pageable);

        verify(resourceAllocationRequestRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));

    }

    @Test
    void testListResourceAllocationRequestsThrowsBadRequest() {

        Set<Byte> approvalStatus = new HashSet<>();
        Set<Integer> projectIds = new HashSet<>();
        Set<Integer> departmentIds = new HashSet<>();
        Set<Integer> requestedBys = new HashSet<>();
        String searchKey = "test";
        Boolean isRequestList = null;
        Resource resource = new Resource(1);
        resource.setEmail("test@gmail.com");
        Pageable pageable = PageRequest.of(0, 20);

        when(resourceAllocationRequestRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(createMockPage());
        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte()))
                .thenReturn(Optional.of(resource));

        assertThrows(BadRequestException.class, () -> allocationServiceImpli.listResourceAllocationRequests(
                approvalStatus, departmentIds, projectIds, requestedBys, searchKey, isRequestList, pageable));

    }

    private Page<ResourceAllocationRequest> createMockPage() {

        return new PageImpl<>(Collections.emptyList());
    }

    @Test
    void testBuildSpecification() {
        // Mock data
        Set<Byte> approvalStatus = Collections.singleton((byte) 0);
        Set<Integer> projectIds = Collections.singleton(1);
        Set<Integer> departmentIds = Collections.singleton(1);
        Set<Integer> requestedBys = Collections.singleton(2);
        String searchKey = "test";
        Resource currentUser = new Resource(1);
        Role role = new Role("HR");
        currentUser.setRole(role);
        Boolean isRequestList = null;

        // Call the buildSpecification method
        Specification<ResourceAllocationRequest> specification = allocationServiceImpli.buildSpecification(
                approvalStatus, departmentIds, projectIds, requestedBys, searchKey, isRequestList, currentUser);

        // Verify that the returned specification is not null
        assertNotNull(specification);

        // Add more assertions based on your requirements
    }

    @Test
    void testDeleteSkillWiseAllocationRequestTestInvalidIdNull() {
        String id = null;
        when(messageSource.getMessage(eq("ID_REQUIRED"), eq(null), any()))
                .thenReturn("2072-Id required");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            allocationServiceImpli.deleteRequestResourceWithSkill(id);
        });

        assertEquals("2072-Id required", exception.getBody().getDetail());
    }

    @Test
    void testDeleteSkillWiseAllocationRequestTestInvalidUserNotFound() {
        String id = "1";
        int idInt = 1;
        when(parameterValidator.isNumber("id", id)).thenReturn(idInt);

        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("USER_NOT_FOUND"), eq(null), any()))
                .thenReturn("1051-User not found");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            allocationServiceImpli.deleteRequestResourceWithSkill(id);
        });

        assertEquals("1051-User not found", exception.getBody().getDetail());

    }

    @Test
    void testDeleteSkillWiseAllocationRequestByPMTest() {
        String id = "1";
        int idInt = 1;
        when(parameterValidator.isNumber("id", id)).thenReturn(idInt);

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);
        ro.setName(Resource.Roles.PM.getValue());

        resource.setRole(ro);

        ResourceSkillWiseAllocationRequest allocationRequest = new ResourceSkillWiseAllocationRequest();
        allocationRequest.setId(idInt);
        allocationRequest.setRequestedBy(resource);
        allocationRequest.setApprovalFlow(ResourceSkillWiseAllocationRequest.ApprovalFlowValues.PENDING.value);

        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(resource));
        when(skillWiseAllocationRequestRepository.findById(idInt)).thenReturn(Optional.of(allocationRequest));
        allocationServiceImpli.deleteRequestResourceWithSkill(id);
        verify(skillWiseAllocationRequestRepository, times(1)).save(allocationRequest);
    }

    @Test
    void testDeleteSkillWiseAllocationRequestByHODTest() {
        String id = "1";
        int idInt = 1;
        when(parameterValidator.isNumber("id", id)).thenReturn(idInt);

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);
        ro.setName(Resource.Roles.HOD.getValue());

        resource.setRole(ro);

        ResourceSkillWiseAllocationRequest allocationRequest = new ResourceSkillWiseAllocationRequest();
        allocationRequest.setId(idInt);
        allocationRequest.setRequestedBy(resource);
        allocationRequest.setApprovalFlow(ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);

        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(resource));
        when(skillWiseAllocationRequestRepository.findById(idInt)).thenReturn(Optional.of(allocationRequest));
        allocationServiceImpli.deleteRequestResourceWithSkill(id);
        verify(skillWiseAllocationRequestRepository, times(1)).save(allocationRequest);
    }

    @Test
    void testDeleteSkillWiseAllocationRequestByOtherThanHODAndPMTest() {
        String id = "1";
        int idInt = 1;
        when(parameterValidator.isNumber("id", id)).thenReturn(idInt);

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);
        ro.setName(Resource.Roles.RM.getValue());

        resource.setRole(ro);

        ResourceSkillWiseAllocationRequest allocationRequest = new ResourceSkillWiseAllocationRequest();
        allocationRequest.setId(idInt);
        allocationRequest.setRequestedBy(resource);
        allocationRequest.setApprovalFlow(ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);

        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(resource));
        when(skillWiseAllocationRequestRepository.findById(idInt)).thenReturn(Optional.of(allocationRequest));

        when(messageSource.getMessage(eq("ACCESS_DENIED"), eq(null), any()))
                .thenReturn("1010-Access restricted.");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            allocationServiceImpli.deleteRequestResourceWithSkill(id);
        });

        assertEquals("1010-Access restricted.", exception.getBody().getDetail());
    }

    @Test
    void testDeleteSkillWiseAllocationRequestInvalid() {
        String id = "1";
        int idInt = 1;
        when(parameterValidator.isNumber("id", id)).thenReturn(idInt);

        Resource resource = new Resource();
        resource.setId(1);
        Role ro = new Role();
        ro.setId(1);
        ro.setName(Resource.Roles.RM.getValue());

        resource.setRole(ro);

        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value))
                .thenReturn(Optional.of(resource));
        when(skillWiseAllocationRequestRepository.findById(idInt)).thenReturn(Optional.empty());

        when(messageSource.getMessage(eq("RESOURCE_SKILL_WISE_ALLOCATION_REQUEST_NOT_FOUND"), eq(null), any()))
                .thenReturn("2073-Resource skill wise allocation request not found");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            allocationServiceImpli.deleteRequestResourceWithSkill(id);
        });

        assertEquals("2073-Resource skill wise allocation request not found", exception.getBody().getDetail());
    }

    @Test
    void testGetAllocationListByProjectId() {
        int projectId = 1;
        int count = 0;

        String page = "0";
        String size = "10";
        int pageInt = 0;
        int pageSizeInt = 10;

        Department d = new Department(1, "depart", 1);

        Resource r = new Resource(1);
        r.setName("test");
        r.setDepartment(d);
        r.setEmployeeId(1234);
        Role role = new Role(1, "ADMIN");
        r.setRole(role);
        Project p = new Project();
        p.setProjectId(1);

        Allocation a = new Allocation(1);
        a.setEndDate(new Date());
        a.setStartDate(new Date());
        a.setResource(r);
        a.setProject(p);

        List<Allocation> allocations = Arrays.asList(a);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Allocation> allocationResponseDTOs = createNewMockPage(allocations, pageable);
        List<AllocationResponseDTO> ardtos = Arrays.asList(new AllocationResponseDTO());

        when(parameterValidator.isNumber("pageNumber", page)).thenReturn(pageInt);
        when(parameterValidator.isNumber("pageSize", size)).thenReturn(pageSizeInt);
        when(commonFunctions.calculateBusinessDays(new Date(), commonFunctions.convertDateToTimestamp(new Date())))
                .thenReturn(count);
        when(allocationRepository.findAllByProjectProjectIdAndStatusAndAllocationExpiryInOrderByUpdatedDateDesc(projectId, Allocation.StatusValues.ACTIVE.value, List.of(Allocation.AllocationExpiryValues.NOT_STARTED.value, Allocation.AllocationExpiryValues.ON_GOING.value), pageable)).thenReturn(allocationResponseDTOs);
        when(commonFunctions.convertDateToTimestamp(any()))
                .thenReturn(new Date());
        PagedResponseDTO<AllocationResponseDTO> reList = allocationServiceImpli.getAllocationListByProjectId(projectId,
                page, size, false);
        assertEquals(ardtos.size(), reList.getItems().size());

    }

    private Page<Allocation> createNewMockPage(List<Allocation> allocations, Pageable pageable) {

        return new PageImpl<>(allocations, pageable, 1);
    }

    @Test
    void testGetOneSkillWiseAllocationRequestByIdTestInvalidIdNull() {
        String id = null;
        when(messageSource.getMessage(eq("ID_REQUIRED"), eq(null), any()))
                .thenReturn("2072-Id required");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            allocationServiceImpli.getRequestResourceWithSkill(id);
        });

        assertEquals("2072-Id required", exception.getBody().getDetail());
    }

    @Test
    void testGetOneSkillWiseAllocationRequestInvalid() {
        String id = "1";
        int idInt = 1;
        when(parameterValidator.isNumber("id", id)).thenReturn(idInt);

        when(skillWiseAllocationRequestRepository.findById(idInt)).thenReturn(Optional.empty());

        when(messageSource.getMessage(eq("RESOURCE_SKILL_WISE_ALLOCATION_REQUEST_NOT_FOUND"), eq(null), any()))
                .thenReturn("2073-Resource skill wise allocation request not found");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            allocationServiceImpli.getRequestResourceWithSkill(id);
        });

        assertEquals("2073-Resource skill wise allocation request not found", exception.getBody().getDetail());
    }

    // @Test
    // void testGetOneSkillWiseAllocationRequestValid() {
    //
    // String id = "1";
    // int idInt = 1;
    //
    // Resource resource = new Resource();
    // resource.setId(1);
    //
    //// Project
    //
    // ResourceSkillWiseAllocationRequest allocationRequest = new
    // ResourceSkillWiseAllocationRequest();
    // allocationRequest.setId(idInt);
    // allocationRequest.setRequestedBy(resource);
    // allocationRequest.setApprovalFlow(ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);
    //
    // when(parameterValidator.isNumber("id", id)).thenReturn(idInt);
    //
    // when(skillWiseAllocationRequestRepository.findById(idInt)).thenReturn(Optional.of(allocationRequest));
    //
    // ResourceSkillWiseAllocationResponseListDTO allocationResponseListDTO =
    // allocationServiceImpli.getRequestResourceWithSkill(id);
    //
    // }
    @Test
    void handleIsRequestListTestHod() {
        List<Byte> approvalStatusList = new ArrayList<>(); // Use ArrayList to make it modifiable

        when(resourceRepository.findByEmailAndStatus(anyString(), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(createMockResource()));
        Resource resource = new Resource();
        resource.setId(1);
        resource.setEmail("test@example.com");
        Role role = new Role();
        role.setId(1);
        role.setName("HOD");
        resource.setRole(role);

        // Ensure that handleIsRequestList works with a modifiable list
        allocationServiceImpli.handleIsRequestList(approvalStatusList, resource);

        assertEquals(3, approvalStatusList.size());
    }

    @Test
    void handleIsRequestListTestHr() {
        List<Byte> approvalStatusList = new ArrayList<>(); // Use ArrayList to make it modifiable

        when(resourceRepository.findByEmailAndStatus(anyString(), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(createMockResource()));
        Resource resource = new Resource();
        resource.setId(1);
        resource.setEmail("test@example.com");
        Role role = new Role();
        role.setId(3);
        role.setName("HR");
        resource.setRole(role);

        // Ensure that handleIsRequestList works with a modifiable list
        allocationServiceImpli.handleIsRequestList(approvalStatusList, resource);

        assertEquals(3, approvalStatusList.size());
    }

    @Test
    void handleIsRequestListTestPROJECTMANAGER() {
        List<Byte> approvalStatusList = new ArrayList<>(); // Use ArrayList to make it modifiable

        when(resourceRepository.findByEmailAndStatus(anyString(), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(createMockResource()));
        Resource resource = new Resource();
        resource.setId(1);
        resource.setEmail("test@example.com");
        Role role = new Role();
        role.setId(5);
        role.setName("PROJECT MANAGER");
        resource.setRole(role);

        // Ensure that handleIsRequestList works with a modifiable list
        allocationServiceImpli.handleIsRequestList(approvalStatusList, resource);

        assertEquals(4, approvalStatusList.size());
    }

    @Test
    void handleIsNotRequestListTest() {
        List<Byte> approvalStatusList = new ArrayList<>(); // Use ArrayList to make it modifiable

        when(resourceRepository.findByEmailAndStatus(anyString(), eq(Resource.Status.ACTIVE.value)))
                .thenReturn(Optional.of(createMockResource()));
        Resource resource = new Resource();
        resource.setId(1);
        resource.setEmail("test@example.com");
        Role role = new Role();
        role.setId(3);
        role.setName("HR");
        resource.setRole(role);

        // Ensure that handleIsRequestList works with a modifiable list
        allocationServiceImpli.handleIsNotRequestList(approvalStatusList, resource);

        assertEquals(3, approvalStatusList.size());
    }


}
