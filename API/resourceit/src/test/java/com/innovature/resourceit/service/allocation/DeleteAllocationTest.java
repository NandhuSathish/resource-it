package com.innovature.resourceit.service.allocation;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ResourceAllocationRequestRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.innovature.resourceit.util.CommonFunctions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = DeleteAllocationTest.class)
class DeleteAllocationTest {

    @Mock
    private AllocationRepository allocationRepository;

    @Mock
    private MessageSource messageSource;
    @Mock
    private CommonFunctions commonFunctions;
    @InjectMocks
    private AllocationServiceImpli allocationService;
    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;

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
    void testDeleteAllocationInvalid() {
        // Mock data
        Integer allocationId = 1;
        Allocation allocation = new Allocation();
        allocation.setId(allocationId);
        allocation.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation.setStartDate(new Date());
        Role role = new Role(2, "PROJECT MANAGER", new Date(), new Date());
        Resource resource = new Resource(1);
        resource.setRole(role);
        allocation.setResource(resource);

        when(resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        // Mock the repository
        when(allocationRepository.findByIdAndStatus(allocationId, Allocation.StatusValues.ACTIVE.value))
                .thenReturn(Optional.of(allocation));
        // Mock the message source
        when(messageSource.getMessage(any(), any(), any()))
                .thenReturn("Allocation not found")
                .thenReturn("CANNOT_DELETE_ALLOCATION");

        // Call the service method
        assertThrows(BadRequestException.class, () -> allocationService.deleteAllocation(allocationId));

        // Verify interactions
        verify(allocationRepository, times(1)).findByIdAndStatus(allocationId, Allocation.StatusValues.ACTIVE.value);
        verify(allocationRepository, times(0)).save(any());
    }

    @Test
    void testDeleteAllocation() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = sdf.parse("2024-12-06");

        // Mock data
        Integer allocationId = 1;
        Allocation allocation = new Allocation();
        allocation.setId(allocationId);
        allocation.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation.setStartDate(startDate);
        Role role = new Role(2, "PROJECT MANAGER", new Date(), new Date());
        Resource resource = new Resource(1);
        resource.setRole(role);
        resource.setJoiningDate(new Date());
        allocation.setResource(resource);
        allocation.setEndDate(new Date());
        when(resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        // Mock the repository
        when(allocationRepository.findByIdAndStatus(allocationId, Allocation.StatusValues.ACTIVE.value))
                .thenReturn(Optional.of(allocation));
        when(resourceRepository.findById(1)).thenReturn(Optional.of(resource));
        when(allocationRepository.findTopByResourceIdAndProjectProjectTypeNotAndAllocationExpiryAndStatusOrderByEndDateDesc(1, Project.projectTypeValues.SUPPORT.value, Allocation.AllocationExpiryValues.EXPIRED.value, Allocation.StatusValues.ACTIVE.value)).thenReturn(Optional.of(allocation));
        allocationService.deleteAllocation(allocationId);

        verify(allocationRepository, times(1)).save(allocation);
    }

    @Test
    void testDeleteAllocationWithNoPreviousAllocation() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = sdf.parse("2024-12-06");

        // Mock data
        Integer allocationId = 1;
        Allocation allocation = new Allocation();
        allocation.setId(allocationId);
        allocation.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation.setStartDate(startDate);
        Role role = new Role(2, "PROJECT MANAGER", new Date(), new Date());
        Resource resource = new Resource(1);
        resource.setRole(role);
        resource.setJoiningDate(new Date());
        allocation.setResource(resource);
        allocation.setEndDate(new Date());
        when(resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        // Mock the repository
        when(allocationRepository.findByIdAndStatus(allocationId, Allocation.StatusValues.ACTIVE.value))
                .thenReturn(Optional.of(allocation));
       when(resourceAllocationRequestRepository.findAllByAllocationId(anyInt())).thenReturn(new ArrayList<>());
        when(resourceRepository.findById(1)).thenReturn(Optional.of(resource));
        when(allocationRepository.findTopByResourceIdAndProjectProjectTypeNotAndAllocationExpiryAndStatusOrderByEndDateDesc(1, Project.projectTypeValues.SUPPORT.value, Allocation.AllocationExpiryValues.EXPIRED.value, Allocation.StatusValues.ACTIVE.value)).thenReturn(Optional.empty());
        allocationService.deleteAllocation(allocationId);

        verify(allocationRepository, times(1)).save(allocation);
    }

    @Test
    void testDeleteAllocationHOD() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = sdf.parse("2024-12-06");

        // Mock data
        Integer allocationId = 1;
        Allocation allocation = new Allocation();
        allocation.setId(allocationId);
        allocation.setStatus(Allocation.StatusValues.ACTIVE.value);
        allocation.setStartDate(startDate);
        Role role = new Role(2, "HOD", new Date(), new Date());
        Resource resource = new Resource(1);
        resource.setRole(role);
        allocation.setResource(resource);
        when(resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        // Mock the repository
        when(allocationRepository.findByIdAndStatus(allocationId, Allocation.StatusValues.ACTIVE.value))
                .thenReturn(Optional.of(allocation));
        when(resourceAllocationRequestRepository.findAllByAllocationId(anyInt())).thenReturn(new ArrayList<>());

        allocationService.deleteAllocation(allocationId);

        verify(allocationRepository, times(1)).save(allocation);
    }
}
