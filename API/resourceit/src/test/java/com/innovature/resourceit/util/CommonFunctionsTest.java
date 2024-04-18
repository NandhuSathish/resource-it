package com.innovature.resourceit.util;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.AllocationConflictsByResourceResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedGroup;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Null;
import org.springframework.context.MessageSource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommonFunctionsTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;
    @Mock
    private ResourceSkillWiseAllocationRequestRepository resourceSkillWiseAllocationRequestRepository;
    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private HolidayCalendarRepository holidayCalendarRepository;

    @InjectMocks
    private CommonFunctions commonFunctions;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckProjectChanges() {
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setProjectId(1);

        when(projectRepository.findByProjectIdAndStatusNot(anyInt(), anyByte()))
                .thenReturn(Optional.of(new Project()));

        boolean result = commonFunctions.checkProjectChanges(dto);

        assertTrue(result);

        verify(projectRepository, times(1)).findByProjectIdAndStatusNot(anyInt(), anyByte());
    }

    @Test
    void testCheckProjectChanges_ProjectId_Null() {
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setProjectId(null);

        when(projectRepository.findByProjectIdAndStatusNot(anyInt(), anyByte()))
                .thenReturn(Optional.of(new Project()));

        boolean result = commonFunctions.checkProjectChanges(dto);

        assertTrue(result);
    }

    @Test
    void testCheckProjectChanges_BadRequest() {
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setProjectId(1);

        when(projectRepository.findByProjectIdAndStatusNot(anyInt(), anyByte()))
                .thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> commonFunctions.checkProjectChanges(dto));
    }

    @Test
    void checkFieldChange() {
        String filedName = "name";
        String oldValue = "Hello";
        String newValue = "Hello There";
        List<String> changedFields = new ArrayList<>();
        commonFunctions.checkFieldChange(filedName, oldValue, newValue, changedFields);
    }

    @Test
    void testCalculateDaysBetween() {
        Date startDate = Date.from(LocalDate.of(2024, 2, 8).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2024, 2, 9).atStartOfDay(ZoneId.systemDefault()).toInstant());

        int daysBetween = commonFunctions.calculateDaysBetween(startDate, endDate);
        assertEquals(2, daysBetween);
    }

    @Test
    void testCheckRequestConflicts() {
        Date startDate = new Date();
        Date endDate = new Date();
        Integer resourceId = 1;
        Integer projectId = 1;

        when(resourceAllocationRequestRepository
                .findByResourceIdAndApprovalFlowNotInAndStartDateBeforeAndEndDateAfterAndProjectProjectIdAndStatus(
                        anyInt(), anyList(), any(), any(), anyInt(), anyByte()))
                .thenReturn(Collections.emptyList());
        when(resourceAllocationRequestRepository
                .findByResourceIdAndApprovalFlowNotInAndStartDateAndProjectProjectIdAndStatus(
                        anyInt(), anyList(), any(), anyInt(), anyByte()))
                .thenReturn(Collections.emptyList());
        when(resourceAllocationRequestRepository
                .findByResourceIdAndApprovalFlowNotInAndEndDateAndProjectProjectIdAndStatus(
                        anyInt(), anyList(), any(), anyInt(), anyByte()))
                .thenReturn(Collections.emptyList());

        boolean result = commonFunctions.checkRequestConflicts(startDate, endDate, resourceId, projectId);

        assertTrue(result);

        verify(resourceAllocationRequestRepository, times(1))
                .findByResourceIdAndApprovalFlowNotInAndStartDateBeforeAndEndDateAfterAndProjectProjectIdAndStatus(
                        anyInt(), anyList(), any(), any(), anyInt(), anyByte());
        verify(resourceAllocationRequestRepository, times(1))
                .findByResourceIdAndApprovalFlowNotInAndStartDateAndProjectProjectIdAndStatus(
                        anyInt(), anyList(), any(), anyInt(), anyByte());
        verify(resourceAllocationRequestRepository, times(1))
                .findByResourceIdAndApprovalFlowNotInAndEndDateAndProjectProjectIdAndStatus(
                        anyInt(), anyList(), any(), anyInt(), anyByte());
    }

    @Test
    void testCheckAllocationConflictsForMultipleResources() {
        List<Integer> resourceIdList = Arrays.asList(1, 2, 3);
        Date startDate = new Date();
        Date endDate = new Date();

        when(allocationRepository
                .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(
                        anyInt(), any(), any(), anyByte()))
                .thenReturn(Collections.emptyList());

        List<AllocationConflictsByResourceResponseDTO> result = commonFunctions
                .checkAllocationConflicts(resourceIdList, startDate, endDate, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(allocationRepository, times(resourceIdList.size()))
                .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(
                        anyInt(), any(), any(), anyByte());
    }

    @Test
    void testIsWeekend() {
        LocalDate weekendDate = LocalDate.of(2023, 1, 28); // A Saturday

        boolean result = commonFunctions.isWeekend(weekendDate);

        assertTrue(result);
    }

    @Test
    void testIsHoliday() {
        LocalDate holidayDate = LocalDate.of(2023, 12, 25); // Christmas Day

        when(holidayCalendarRepository.findByYear(anyInt(), any())).thenReturn(Optional.empty());

        boolean result = commonFunctions.isHoliday(holidayDate);

        assertFalse(result);

        verify(holidayCalendarRepository, times(1)).findByYear(anyInt(), any());
    }

    @Test
    void testConvertToLocalDate() {
        Date testDate = new Date();

        LocalDate result = commonFunctions.convertToLocalDate(testDate);

        assertNotNull(result);
    }

    @Test
    void testCalculateBusinessDays() {
        Date startDate = Date.from(LocalDate.of(2024, 2, 8).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2024, 2, 9).atStartOfDay(ZoneId.systemDefault()).toInstant());

        int businessDays = commonFunctions.calculateBusinessDays(startDate, endDate);
        assertEquals(2, businessDays);
    }

    @Test
    void testCalculateBusinessDayswithWeekend() {
        Date startDate = Date.from(LocalDate.of(2023, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 4).atStartOfDay(ZoneId.systemDefault()).toInstant());

        int businessDays = commonFunctions.calculateBusinessDays(startDate, endDate);
        assertEquals(3, businessDays);
    }

    @Test
    void testConvertDateToTimestamp() {
        Date validDate = new Date(2023, 11, 1);

        Date resultDate = commonFunctions.convertDateToTimestamp(validDate);
        assertEquals(validDate, resultDate);
    }

    @Test
    void testCalculateConflictDays() {
        Date proposedStartDate = new Date();
        Date proposedEndDate = new Date();
        Integer resourceId = 1;
        // Mock the repository responses

        when(allocationRepository
                .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(
                        anyInt(), any(Date.class), any(Date.class), anyByte()))
                .thenReturn(Collections.emptyList());

        when(allocationRepository.findAllByResourceIdAndEndDateAndStatus(
                anyInt(), any(Date.class), anyByte()))
                .thenReturn(Collections.emptyList());

        when(allocationRepository.findAllByResourceIdAndStartDateAndStatus(
                anyInt(), any(Date.class), anyByte()))
                .thenReturn(Collections.emptyList());
        int conflictDays = commonFunctions.calculateConflictDays(resourceId, null, proposedStartDate,
                proposedEndDate);

        assertEquals(0, conflictDays);

    }

    @Test
    void testCalculateConflictDaysWithAllocation() {
        Date proposedStartDate = new Date();
        Date proposedEndDate = new Date();
        Integer resourceId = 1;
        // Mock the repository responses

        when(allocationRepository
                .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(
                        anyInt(), any(Date.class), any(Date.class), anyByte()))
                .thenReturn(Collections.emptyList());

        when(allocationRepository.findAllByResourceIdAndEndDateAndStatus(
                anyInt(), any(Date.class), anyByte()))
                .thenReturn(Collections.emptyList());

        when(allocationRepository.findAllByResourceIdAndStartDateAndStatus(
                anyInt(), any(Date.class), anyByte()))
                .thenReturn(Collections.emptyList());
        int conflictDays = commonFunctions.calculateConflictDays(resourceId, new Allocation(1),
                proposedStartDate, proposedEndDate);

        assertEquals(0, conflictDays);

    }

    @Test
    void testRemoveAllocationRequests() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer projectId = 1;
        Date newStartDate = dateFormat.parse("2023-01-01 15:30:00");
        Date newEndDate = dateFormat.parse("2023-01-31 15:30:00");
        ResourceAllocationRequest allocation1 = new ResourceAllocationRequest();
        allocation1.setId(1);
        allocation1.setStartDate(new Date());
        allocation1.setEndDate(new Date());
        ResourceAllocationRequest allocation2 = new ResourceAllocationRequest();
        allocation2.setId(2);
        allocation2.setStartDate(dateFormat.parse("2023-01-01 15:30:00"));
        allocation2.setEndDate(new Date());
        ResourceSkillWiseAllocationRequest allocation3 = new ResourceSkillWiseAllocationRequest();
        allocation3.setId(1);
        allocation3.setStartDate(new Date());
        allocation3.setEndDate(new Date());
        ResourceSkillWiseAllocationRequest allocation4 = new ResourceSkillWiseAllocationRequest();
        allocation4.setId(2);
        allocation4.setStartDate(dateFormat.parse("2023-01-01 15:30:00"));
        allocation4.setEndDate(new Date());
        List<ResourceAllocationRequest> resourceAllocationRequests = Arrays.asList(
                allocation1,
                allocation2);
        when(resourceAllocationRequestRepository.findAllByProjectProjectIdAndStatusAndApprovalFlowNotIn(
                anyInt(), anyByte(), anyList()))
                .thenReturn(resourceAllocationRequests);

        List<ResourceSkillWiseAllocationRequest> skillAllocationRequests = Arrays.asList(
                allocation3,
                allocation4);
        when(resourceSkillWiseAllocationRequestRepository.findAllByProjectProjectIdAndStatusAndApprovalFlowNot(
                anyInt(), anyByte(), anyByte()))
                .thenReturn(skillAllocationRequests);

        commonFunctions.removeAllocationRequests(projectId, newStartDate, newEndDate);

        verify(resourceAllocationRequestRepository, times(1)).saveAll(anyList());
        verify(resourceSkillWiseAllocationRequestRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testRemoveAllocationRequestsWithAllocation() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer projectId = 1;
        Date newStartDate = dateFormat.parse("2023-01-01 15:30:00");
        Date newEndDate = dateFormat.parse("2023-01-31 15:30:00");
        ResourceAllocationRequest allocation1 = new ResourceAllocationRequest();
        allocation1.setId(1);
        allocation1.setStartDate(new Date());
        allocation1.setEndDate(new Date());
        allocation1.setAllocation(new Allocation(1));
        ResourceAllocationRequest allocation2 = new ResourceAllocationRequest();
        allocation2.setId(2);
        allocation2.setStartDate(dateFormat.parse("2023-01-01 15:30:00"));
        allocation2.setEndDate(new Date());
        allocation2.setAllocation(new Allocation(2));

        ResourceSkillWiseAllocationRequest allocation3 = new ResourceSkillWiseAllocationRequest();
        allocation3.setId(1);
        allocation3.setStartDate(new Date());
        allocation3.setEndDate(new Date());
        ResourceSkillWiseAllocationRequest allocation4 = new ResourceSkillWiseAllocationRequest();
        allocation4.setId(2);
        allocation4.setStartDate(dateFormat.parse("2023-01-01 15:30:00"));
        allocation4.setEndDate(new Date());
        List<ResourceAllocationRequest> resourceAllocationRequests = Arrays.asList(
                allocation1,
                allocation2);
        when(resourceAllocationRequestRepository.findAllByProjectProjectIdAndStatusAndApprovalFlowNotIn(
                anyInt(), anyByte(), anyList()))
                .thenReturn(resourceAllocationRequests);

        List<ResourceSkillWiseAllocationRequest> skillAllocationRequests = Arrays.asList(
                allocation3,
                allocation4);
        when(resourceSkillWiseAllocationRequestRepository.findAllByProjectProjectIdAndStatusAndApprovalFlowNot(
                anyInt(), anyByte(), anyByte()))
                .thenReturn(skillAllocationRequests);

        commonFunctions.removeAllocationRequests(projectId, newStartDate, newEndDate);

        verify(resourceAllocationRequestRepository, times(1)).saveAll(anyList());
        verify(resourceSkillWiseAllocationRequestRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testAdjustAllocationDates() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer projectId = 1;
        Date newStartDate = dateFormat.parse("2023-01-01 15:30:00");
        Date newEndDate = dateFormat.parse("2023-01-31 15:30:00");
        Allocation allocation1 = new Allocation(1);
        allocation1.setStartDate(new Date());
        allocation1.setEndDate(new Date());
        Allocation allocation2 = new Allocation(1);
        allocation2.setStartDate(dateFormat.parse("2023-01-01 15:30:00"));
        allocation2.setEndDate(new Date());
        List<Allocation> allocations = Arrays.asList(
                allocation1,
                allocation2 // Add relevant fields for another valid allocation
        );
        when(allocationRepository.findByProjectProjectIdAndStatus(anyInt(), anyByte()))
                .thenReturn(allocations);

        commonFunctions.adjustAllocationDates(projectId, newStartDate, newEndDate);

        verify(allocationRepository, times(2)).save(any(Allocation.class));
    }

    @Test
    void testDeleteWrongAllocations() {
        List<Allocation> allocationList = new ArrayList<>();
        Allocation allocation1 = new Allocation();
        allocation1.setStartDate(new Date());
        allocation1.setEndDate(new Date());
        Allocation allocation2 = new Allocation();
        allocation2.setStartDate(new Date());
        allocation2.setEndDate(new Date());
        allocationList.add(allocation1);
        allocationList.add(allocation2);

        when(allocationRepository.findAll()).thenReturn(allocationList);

        commonFunctions.deleteWrongAllocations();

        verify(allocationRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testUpdateResourceAllocationStatus() {
        List<Resource> resources = new ArrayList<>();
        Project project = new Project(1);
        Resource resource1 = new Resource();
        resource1.setId(1);
        Resource resource2 = new Resource();
        resource2.setId(2);
        resources.add(resource1);
        resources.add(resource2);

        List<Allocation> allocationList1 = new ArrayList<>();
        Allocation allocation11 = new Allocation();
        allocation11.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation11.setProject(project);
        allocationList1.add(allocation11);

        List<Allocation> allocationList2 = new ArrayList<>();
        Allocation allocation21 = new Allocation();
        allocation21.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation21.setProject(project);
        allocationList2.add(allocation21);

        when(resourceRepository.findAll()).thenReturn(resources);
        when(allocationRepository.findByResourceIdAndAllocationExpiryInAndStatusAndProjectProjectTypeNot(
                eq(1), anyList(), eq(Allocation.StatusValues.ACTIVE.value),
                eq(Project.projectTypeValues.SUPPORT.value)))
                .thenReturn(allocationList1);
        when(allocationRepository.findByResourceIdAndAllocationExpiryInAndStatusAndProjectProjectTypeNot(
                eq(2), anyList(), eq(Allocation.StatusValues.ACTIVE.value),
                eq(Project.projectTypeValues.SUPPORT.value)))
                .thenReturn(allocationList2);

        commonFunctions.updateResourceAllocationStatus();

        verify(resourceRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testHandleAllocationStatus() {
        Allocation allocation1 = new Allocation();
        allocation1.setProject(new Project());
        allocation1.getProject().setProjectId(1);
        allocation1.getProject().setStatus(Project.statusValues.ACTIVE.value);
        allocation1.getProject().setProjectType(Project.projectTypeValues.INTERNAL.value);

        Allocation allocation2 = new Allocation();
        allocation2.setProject(new Project());
        allocation2.getProject().setProjectId(2);
        allocation2.getProject().setStatus(Project.statusValues.ACTIVE.value);
        allocation2.getProject().setProjectType(Project.projectTypeValues.BILLABLE.value);

        List<Allocation> allocationList = new ArrayList<>();
        allocationList.add(allocation1);
        allocationList.add(allocation2);

        Resource resource = new Resource();

        when(projectRepository.findByProjectIdAndStatus(eq(1), eq(Project.statusValues.ACTIVE.value)))
                .thenReturn(Optional.of(new Project()));
        when(projectRepository.findByProjectIdAndStatus(eq(2), eq(Project.statusValues.ACTIVE.value)))
                .thenReturn(Optional.of(new Project()));

        commonFunctions.handleAllocationStatus(allocationList, resource);

        verify(projectRepository, times(2)).findByProjectIdAndStatus(anyInt(), anyByte());
        // assertEquals(Resource.AllocationStatus.EXTERNAL.value,
        // resource.getAllocationStatus());
    }

    @Test
    void testHandleAllocationStatusWithInternalProject() {
        Allocation allocation = new Allocation();
        allocation.setProject(new Project());
        allocation.getProject().setProjectId(1);
        allocation.getProject().setStatus(Project.statusValues.ACTIVE.value);
        allocation.getProject().setProjectType(Project.projectTypeValues.INTERNAL.value);

        List<Allocation> allocationList = new ArrayList<>();
        allocationList.add(allocation);

        Resource resource = new Resource();

        when(projectRepository.findByProjectIdAndStatus(eq(1), eq(Project.statusValues.ACTIVE.value)))
                .thenReturn(Optional.of(new Project()));

        commonFunctions.handleAllocationStatus(allocationList, resource);

        verify(projectRepository, times(1)).findByProjectIdAndStatus(anyInt(), anyByte());
        // assertEquals(Resource.AllocationStatus.INTERNAL.value,
        // resource.getAllocationStatus());
    }

    @Test
    void testHandleAllocationStatusWithNoProjects() {
        List<Allocation> allocationList = new ArrayList<>();
        Resource resource = new Resource();

        commonFunctions.handleAllocationStatus(allocationList, resource);

        verify(projectRepository, never()).findByProjectIdAndStatus(anyInt(), anyByte());
        assertEquals(Resource.AllocationStatus.BENCH.value, resource.getAllocationStatus());
    }

    @Test
    void deleteResourceTest() {
        Resource resource = new Resource(1);
        Allocation allocation = new Allocation();
        allocation.setStartDate(new Date());
        allocation.setEndDate(new Date());
        List<Allocation> allocationList = new ArrayList<>();
        allocationList.add(allocation);
        when(allocationRepository.findAllByResourceIdAndStatus(anyInt(), anyByte())).thenReturn(allocationList);
        ResourceAllocationRequest resourceAllocationRequest = new ResourceAllocationRequest();
        resourceAllocationRequest.setId(1);
        List<ResourceAllocationRequest> resourceAllocationRequestList = new ArrayList<>();
        resourceAllocationRequestList.add(resourceAllocationRequest);
        when(resourceAllocationRequestRepository.findByResourceIdAndStatus(anyInt(), anyByte()))
                .thenReturn(resourceAllocationRequestList);
        commonFunctions.resourceDelete(resource);
        assertEquals(resource.getStatus(), Resource.Status.INACTIVE.value);
    }

    @Test
    void deleteResourceTestAllocationStartDateIsAfterCurrentDate() {
        Resource resource = new Resource(1);
        Allocation allocation = new Allocation();
        allocation.setStartDate(Date.from(LocalDate.of(2024, 2, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation.setEndDate(Date.from(LocalDate.of(2024, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        List<Allocation> allocationList = new ArrayList<>();
        allocation.setStatus(Allocation.StatusValues.DELETED.value);
        allocationList.add(allocation);
        when(allocationRepository.findAllByResourceIdAndStatus(anyInt(), anyByte())).thenReturn(allocationList);
        ResourceAllocationRequest resourceAllocationRequest = new ResourceAllocationRequest();
        resourceAllocationRequest.setId(1);
        List<ResourceAllocationRequest> resourceAllocationRequestList = new ArrayList<>();
        resourceAllocationRequestList.add(resourceAllocationRequest);
        when(resourceAllocationRequestRepository.findByResourceIdAndStatus(anyInt(), anyByte()))
                .thenReturn(resourceAllocationRequestList);
        commonFunctions.resourceDelete(resource);
        assertEquals(allocation.getIsRemoved(), Allocation.IsRemoved.YES.value);
        assertEquals(allocation.getAllocationExpiry(), Allocation.AllocationExpiryValues.EXPIRED.value);


    }

    @Test
    void testUpdateAllocationStatusExpiryAndTeamSizeResourceIsPresent() {
        // Mocking repository responses
        Project project1 = new Project();
        project1.setProjectId(1);
        project1.setStartDate(
                Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project1.setEndDate(
                Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project1.setProjectType(Project.projectTypeValues.INTERNAL.value);

        when(projectRepository.findAllByStatus(Project.statusValues.ACTIVE.value))
                .thenReturn(List.of(project1));

        Resource resource1 = new Resource();
        resource1.setId(1);
        resource1.setStatus(Resource.Status.ACTIVE.value);

        Allocation allocation1 = new Allocation(1);
        allocation1.setStartDate(
                Date.from(LocalDate.of(2022, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation1.setEndDate(
                Date.from(LocalDate.of(2022, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation1.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation1.setAllocationExpiry(Allocation.AllocationExpiryValues.ON_GOING.value);
        allocation1.setResource(resource1);
        allocation1.setProject(project1);
        when(resourceRepository.findById(anyInt()))
                .thenReturn(Optional.of(resource1));
        when(allocationRepository.findByProjectProjectIdAndAllocationExpiryInAndStatus(
                1,
                Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value,
                        Allocation.AllocationExpiryValues.ON_GOING.value),
                Allocation.StatusValues.ACTIVE.value))
                .thenReturn(List.of(allocation1));

        // Additional setup as needed for your code

        // Mock other necessary repository responses and expected behavior

        // Execute the method
        commonFunctions.updateAllocationStatusExpiryAndTeamSize();

        // Verify the interactions
        verify(allocationRepository, times(1)).saveAllAndFlush(anyList());
        verify(resourceRepository, times(1)).saveAllAndFlush(anyList());
        verify(projectRepository, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    void testUpdateAllocationStatusExpiryAndTeamSizeResourceIsPresentAndProjectTypeIsSupport() {
        // Mocking repository responses
        Project project1 = new Project();
        project1.setProjectId(1);
        project1.setStartDate(
                Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project1.setEndDate(
                Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project1.setProjectType(Project.projectTypeValues.SUPPORT.value);

        when(projectRepository.findAllByStatus(Project.statusValues.ACTIVE.value))
                .thenReturn(List.of(project1));

        Resource resource1 = new Resource();
        resource1.setId(1);
        resource1.setStatus(Resource.Status.ACTIVE.value);

        Allocation allocation1 = new Allocation(1);
        allocation1.setStartDate(
                Date.from(LocalDate.of(2022, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation1.setEndDate(
                Date.from(LocalDate.of(2022, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation1.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation1.setAllocationExpiry(Allocation.AllocationExpiryValues.ON_GOING.value);
        allocation1.setResource(resource1);
        allocation1.setProject(project1);
        when(resourceRepository.findById(anyInt()))
                .thenReturn(Optional.of(resource1));
        when(allocationRepository.findByProjectProjectIdAndAllocationExpiryInAndStatus(
                1,
                Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value,
                        Allocation.AllocationExpiryValues.ON_GOING.value),
                Allocation.StatusValues.ACTIVE.value))
                .thenReturn(List.of(allocation1));

        // Additional setup as needed for your code

        // Mock other necessary repository responses and expected behavior

        // Execute the method
        commonFunctions.updateAllocationStatusExpiryAndTeamSize();

        // Verify the interactions
        verify(allocationRepository, times(1)).saveAllAndFlush(anyList());
        verify(resourceRepository, times(1)).saveAllAndFlush(anyList());
        verify(projectRepository, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    void testUpdateAllocationStatusExpiryAndTeamSizeResourceIsPresentAndAllocationStartDateIsAfterCurrentDate() {
        // Mocking repository responses
        Project project1 = new Project();
        project1.setProjectId(1);
        project1.setStartDate(
                Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project1.setEndDate(
                Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project1.setProjectType(Project.projectTypeValues.INTERNAL.value);
        when(projectRepository.findAllByStatus(Project.statusValues.ACTIVE.value))
                .thenReturn(List.of(project1));

        Resource resource1 = new Resource();
        resource1.setId(1);
        resource1.setStatus(Resource.Status.ACTIVE.value);
        resource1.setAllocationStatus(Resource.AllocationStatus.BENCH.value);
        Allocation allocation1 = new Allocation(1);
        allocation1.setStartDate(
                Date.from(LocalDate.of(2024, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation1.setEndDate(
                Date.from(LocalDate.of(2024, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation1.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation1.setAllocationExpiry(Allocation.AllocationExpiryValues.ON_GOING.value);
        allocation1.setResource(resource1);
        allocation1.setProject(project1);
        when(resourceRepository.findById(anyInt()))
                .thenReturn(Optional.of(resource1));
        when(allocationRepository.findByProjectProjectIdAndAllocationExpiryInAndStatus(
                1,
                Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value,
                        Allocation.AllocationExpiryValues.ON_GOING.value),
                Allocation.StatusValues.ACTIVE.value))
                .thenReturn(List.of(allocation1));

        // Additional setup as needed for your code

        // Mock other necessary repository responses and expected behavior

        // Execute the method
        commonFunctions.updateAllocationStatusExpiryAndTeamSize();

        // Verify the interactions
        verify(allocationRepository, times(1)).saveAllAndFlush(anyList());
        verify(resourceRepository, times(1)).saveAllAndFlush(anyList());
        verify(projectRepository, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    void testUpdateAllocationStatusExpiryAndTeamSizeResourceIsPresentAndAllocationOngoing() {
        // Mocking repository responses
        Project project1 = new Project();
        project1.setProjectId(1);
        project1.setStartDate(
                Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project1.setEndDate(
                Date.from(LocalDate.of(2024, 5, 30).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project1.setProjectType(Project.projectTypeValues.BILLABLE.value);

        when(projectRepository.findAllByStatus(Project.statusValues.ACTIVE.value))
                .thenReturn(List.of(project1));

        Resource resource1 = new Resource();
        resource1.setId(1);
        resource1.setStatus(Resource.Status.ACTIVE.value);
        resource1.setAllocationStatus(Resource.AllocationStatus.BENCH.value);

        Allocation allocation1 = new Allocation(1);
        allocation1.setStartDate(
                Date.from(LocalDate.of(2024, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation1.setEndDate(
                Date.from(LocalDate.of(2024, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation1.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation1.setAllocationExpiry(Allocation.AllocationExpiryValues.ON_GOING.value);
        allocation1.setResource(resource1);
        allocation1.setProject(project1);
        when(resourceRepository.findById(anyInt()))
                .thenReturn(Optional.of(resource1));
        when(allocationRepository.findByProjectProjectIdAndAllocationExpiryInAndStatus(
                1,
                Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value,
                        Allocation.AllocationExpiryValues.ON_GOING.value),
                Allocation.StatusValues.ACTIVE.value))
                .thenReturn(List.of(allocation1));

        // Additional setup as needed for your code

        // Mock other necessary repository responses and expected behavior

        // Execute the method
        commonFunctions.updateAllocationStatusExpiryAndTeamSize();

        // Verify the interactions
        verify(allocationRepository, times(1)).saveAllAndFlush(anyList());
        verify(resourceRepository, times(1)).saveAllAndFlush(anyList());
        verify(projectRepository, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    void testUpdateAllocationStatusExpiryAndTeamSize() {
        // Mocking repository responses
        Project project1 = new Project();
        project1.setProjectId(1);
        project1.setStartDate(
                Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project1.setEndDate(
                Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(projectRepository.findAllByStatus(Project.statusValues.ACTIVE.value))
                .thenReturn(List.of(project1));

        Resource resource1 = new Resource();
        resource1.setId(1);
        resource1.setStatus(Resource.Status.ACTIVE.value);

        Allocation allocation1 = new Allocation(1);
        allocation1.setStartDate(
                Date.from(LocalDate.of(2022, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation1.setEndDate(
                Date.from(LocalDate.of(2022, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        allocation1.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation1.setAllocationExpiry(Allocation.AllocationExpiryValues.ON_GOING.value);
        allocation1.setResource(resource1);

        when(allocationRepository.findByProjectProjectIdAndAllocationExpiryInAndStatus(
                1,
                Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value,
                        Allocation.AllocationExpiryValues.ON_GOING.value),
                Allocation.StatusValues.ACTIVE.value))
                .thenReturn(List.of(allocation1));

        // Additional setup as needed for your code

        // Mock other necessary repository responses and expected behavior

        // Execute the method
        commonFunctions.updateAllocationStatusExpiryAndTeamSize();

        // Verify the interactions
        verify(allocationRepository, times(1)).saveAllAndFlush(anyList());
        verify(resourceRepository, times(1)).saveAllAndFlush(anyList());
        verify(projectRepository, times(1)).saveAllAndFlush(anyList());
    }


    @Test
    void testHandleAllocationStartDateAfterCurrentDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Allocation allocation = new Allocation(1);
        allocation.setStartDate(dateFormat.parse("2023-01-02 15:30:00"));
        allocation.setEndDate(dateFormat.parse("2023-01-10 15:30:00"));

        Resource resource = new Resource();
        resource.setId(1);
        resource.setAllocationStatus(Resource.AllocationStatus.BENCH.value);
        allocation.setResource(resource);


        // Mock the repository responses as needed

        commonFunctions.handleAllocationStartDateAfterCurrentDate(allocation, resource);

        // Verify the changes made to allocation and resource
        assertEquals(Allocation.AllocationExpiryValues.NOT_STARTED.value, allocation.getAllocationExpiry());
        // Verify other changes as needed
    }

    @Test
    void testCheckProjectState() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Project project = new Project();
        project.setStartDate(dateFormat.parse("2023-01-02 15:30:00"));
        project.setEndDate(dateFormat.parse("2023-01-10 15:30:00"));

        Project result = commonFunctions.checkProjectState(project);

        assertEquals(Project.projectStateValues.COMPLETED.value, result.getProjectState());
    }

    @Test
    void testGetStartDate() {
        Allocation allocation = new Allocation(1);
        allocation.setStartDate(Date.from(LocalDate.of(2022, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Date proposedStartDate = Date.from(LocalDate.of(2022, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate localDate = LocalDate.of(2023, 1, 1);
        Date result = commonFunctions.getStartDate(allocation, localDate, proposedStartDate);
        assertNotNull(result);
    }

    @Test
    void testGetEndDate() {
        Allocation allocation = new Allocation(1);
        allocation.setEndDate(Date.from(LocalDate.of(2022, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Date proposedEndDate = Date.from(LocalDate.of(2022, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate localDate = LocalDate.of(2023, 1, 1);
        Date result = commonFunctions.getEndDate(allocation, localDate, proposedEndDate);
        assertNotNull(result);
    }

    @Test
    void testScheduleUpdateAllocationStatusExpiryAndTeamSize() {
        commonFunctions.scheduleUpdateAllocationStatusExpiryAndTeamSize();
        assertTrue(true);
    }

    @Test
    void testRelievedUserCheck() {
        Allocation allocation = new Allocation(1);
        Resource resource = new Resource(1);
        resource.setStatus(Resource.Status.INACTIVE.value);
        commonFunctions.relievedUserCheck(allocation, resource);
        assertEquals(allocation.getIsRemoved(), Allocation.IsRemoved.YES.value);
    }

    @Test
    void testSetLastWorkedProjectDateCase2() {
        Allocation allocation = new Allocation(1);
        Resource resource = new Resource(1);
        resource.setLastWorkedProjectDate(new Date());
        List<Resource> resourceList = new ArrayList<>();
        commonFunctions.setLastWorkedProjectDate(resource, allocation, LocalDate.of(2024, 2, 20), resourceList);
        assertNotEquals(resourceList, new ArrayList<Resource>());
    }

    @Test
    void testSetLastWorkedProjectDateCase1() {
        Allocation allocation = new Allocation(1);
        Resource resource = new Resource(1);
        resource.setLastWorkedProjectDate(new Date());
        List<Resource> resourceList = new ArrayList<>();
        commonFunctions.setLastWorkedProjectDate(resource, allocation, LocalDate.now(), resourceList);
        assertNotEquals(resourceList, new ArrayList<Resource>());
    }

}
