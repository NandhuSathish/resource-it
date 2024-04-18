package com.innovature.resourceit.service.batch;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.impli.BatchServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.Mockito.when;

public class UpdateAllocationStatusExpiryAndTeamSizeTest {

    @Mock
    AllocationRepository allocationRepository;

    @Mock
    ProjectRepository projectRepository;
    @Mock
    ResourceRepository resourceRepository;
    @Mock
    CommonFunctions commonFunctions;

    @InjectMocks
    BatchServiceImpli batchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateAllocationStatusExpiryAndTeamSize() throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        List<Project> projects = new ArrayList<>();
        Project p = new Project();
        p.setProjectId(1);
        p.setStartDate(new Date());
        p.setEndDate(new Date());

        projects.add(p);

        List<Allocation> allocations = new ArrayList<>();
        Allocation a = new Allocation(1);
        a.setResource(new Resource(1));
        a.setEndDate(simpleDateFormat.parse("06-12-2023"));
        allocations.add(a);

        int count = 0;

        when(projectRepository.findAllByStatus(anyByte())).thenReturn(projects);
        when(commonFunctions.convertDateToTimestamp(any())).thenReturn(new Date());
        when(allocationRepository.findByProjectProjectIdAndAllocationExpiryInAndStatus
                (p.getProjectId(), Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value, Allocation.AllocationExpiryValues.ON_GOING.value), Allocation.StatusValues.ACTIVE.value)).thenReturn(allocations);
        when(allocationRepository.countDistinctResourceIdByProjectProjectIdAndAllocationExpiryInAndStatus
                (p.getProjectId(), Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value, Allocation.AllocationExpiryValues.ON_GOING.value), Allocation.StatusValues.ACTIVE.value)).thenReturn(count);
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(new Resource()));

        batchService.updateAllocationStatusExpiryAndTeamSize();
    }


    @Test
    public void testUpdateAllocationStatusExpiryAndTeamSizeAndProjStatusToOngoing() throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        List<Project> projects = new ArrayList<>();
        Project p = new Project();
        p.setProjectId(1);
        p.setStartDate(new Date());
        p.setEndDate(new Date());

        projects.add(p);

        List<Allocation> allocations = new ArrayList<>();
        Allocation a = new Allocation(1);
        a.setResource(new Resource(1));
        a.setStartDate(simpleDateFormat.parse("06-12-2020"));
        a.setEndDate(simpleDateFormat.parse("06-12-2040"));
        allocations.add(a);

        int count = 0;

        when(projectRepository.findAllByStatus(anyByte())).thenReturn(projects);
        when(allocationRepository.findByProjectProjectIdAndAllocationExpiryInAndStatus
                (p.getProjectId(), Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value, Allocation.AllocationExpiryValues.ON_GOING.value), Allocation.StatusValues.ACTIVE.value)).thenReturn(allocations);
        when(allocationRepository.countDistinctResourceIdByProjectProjectIdAndAllocationExpiryInAndStatus
                (p.getProjectId(), Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value, Allocation.AllocationExpiryValues.ON_GOING.value), Allocation.StatusValues.ACTIVE.value)).thenReturn(count);
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(new Resource()));
        when(commonFunctions.convertDateToTimestamp(any())).thenReturn(new Date());

        batchService.updateAllocationStatusExpiryAndTeamSize();
    }

    @Test
    public void testDateFilter() {
        LocalDate localStartDate = LocalDate.now().plusDays(1);
        LocalDate localCurrentDate = LocalDate.now();
        Date allocationStartDate = new Date();
        Date allocationEndDate = new Date();
        Date currentDate = new Date();

        int expectedDaysLeft = 1; // Assuming calculateWorkDayModified returns 0 days

        int actualDaysLeft = batchService.dateFilter(localStartDate, localCurrentDate, allocationStartDate, allocationEndDate, currentDate);

        assertEquals(expectedDaysLeft, actualDaysLeft);
    }

    @Test
    public void testDateFormatter() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateToFormat = simpleDateFormat.parse("01/01/2023");

        String expectedFormattedDate = "01/01/2023";

        String actualFormattedDate = batchService.dateFormater(dateToFormat);

        assertEquals(expectedFormattedDate, actualFormattedDate);
    }

    @Test
    public void testCalculateWorkDayModified() {
        Date startDate = new Date();
        Date endDate = new Date();

        when(commonFunctions.isWeekend(any())).thenReturn(false);
        when(commonFunctions.isHoliday(any())).thenReturn(false);

        int expectedNumberOfDays = 0; // Assuming startDate and endDate are the same day and not a weekend or holiday

        int actualNumberOfDays = batchService.calculateWorkDayModified(startDate, endDate);

        assertEquals(expectedNumberOfDays, actualNumberOfDays);
    }
}
