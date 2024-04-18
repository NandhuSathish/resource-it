package com.innovature.resourceit.service.resource;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceAllocationRequest;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ResourceAllocationRequestRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.ResourceServiceImpli;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = DeleteResourceTest.class)
public class DeleteResourceTest {

    @InjectMocks
    ResourceServiceImpli service;

    @Mock
    ResourceRepository resourceRepository;
    @Mock
    AllocationRepository allocationRepository;
    @Mock
    ResourceAllocationRequestRepository resourceAllocationRequestRepository;
    @Mock
    MessageSource messageSource;
    @Mock
    CommonFunctions commonFunctions;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;


    private static final String RESOURCE_NOT_FOUND = "resource.not.found";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    public void testDeleteResource() {
        int resourceId = 123;
        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setStatus(Resource.Status.ACTIVE.value);
        Role role1 = new Role("RESOURCE");
        role1.setId(7);
        resource.setRole(role1);
        Resource resource1 = new Resource();
        resource.setId(1);
        resource.setStatus(Resource.Status.ACTIVE.value);
        Role role = new Role("ADMIN");
        resource1.setRole(role);
        Allocation allocation = new Allocation(1);
        allocation.setStartDate(new Date());
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource1));
        when(allocationRepository.findAllByResourceIdAndStatus(anyInt(), anyByte())).thenReturn(List.of(allocation));
        when(resourceRepository.findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value))).thenReturn(Optional.of(resource));
        when(resourceAllocationRequestRepository.findByResourceIdAndStatus(anyInt(), anyByte())).thenReturn(List.of(new ResourceAllocationRequest()));

        when(messageSource.getMessage(eq(RESOURCE_NOT_FOUND), eq(null), eq(Locale.ENGLISH))).thenReturn("Resource not found");

        service.deleteResource(resourceId);

        verify(resourceRepository, times(1)).findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value));

        assertEquals(Resource.Status.INACTIVE.value, resource.getStatus());

        verify(resourceRepository, times(1)).save(eq(resource));
    }

    @Test
    public void testDeleteResource_HR() {
        int resourceId = 123;
        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setStatus(Resource.Status.ACTIVE.value);
        Role role2 = new Role("RESOURCE");
        role2.setId(3);
        resource.setRole(role2);
        Resource resource1 = new Resource();
        resource1.setId(1);
        resource1.setStatus(Resource.Status.ACTIVE.value);
        Role role = new Role("HR");
        resource1.setRole(role); // Set role for resource1
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource1));

        when(resourceRepository.findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value))).thenReturn(Optional.of(resource));

        when(messageSource.getMessage(eq(RESOURCE_NOT_FOUND), eq(null), eq(Locale.ENGLISH))).thenReturn("Resource not found");

        service.deleteResource(resourceId);

        verify(resourceRepository, times(1)).findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value));

        assertEquals(Resource.Status.INACTIVE.value, resource.getStatus());

        verify(resourceRepository, times(1)).save(eq(resource));
    }

    @Test
    public void testDeleteResource_RM() {
        int resourceId = 123;
        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setStatus(Resource.Status.ACTIVE.value);
        Role role2 = new Role("RESOURCE");
        role2.setId(3);
        resource.setRole(role2);

        Resource resource1 = new Resource();
        resource1.setId(1);
        resource1.setStatus(Resource.Status.ACTIVE.value);
        Role role = new Role("RESOURCE MANAGER");
        role.setId(4); // Set id for role
        resource1.setRole(role); // Set role for resource1

        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource1));

        when(resourceRepository.findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value))).thenReturn(Optional.of(resource));

        when(messageSource.getMessage(eq(RESOURCE_NOT_FOUND), eq(null), eq(Locale.ENGLISH))).thenReturn("Resource not found");

        service.deleteResource(resourceId);

        verify(resourceRepository, times(1)).findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value));

        assertEquals(Resource.Status.INACTIVE.value, resource.getStatus());

        verify(resourceRepository, times(1)).save(eq(resource));
    }

    @Test
    public void testDeleteResourceResourceNotFound() {
        int resourceId = 123;
        when(resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(new Resource()));

        when(resourceRepository.findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value))).thenReturn(Optional.empty());

        when(messageSource.getMessage(eq(RESOURCE_NOT_FOUND), eq(null), eq(Locale.ENGLISH))).thenReturn("Resource not found");

        assertThrows(BadRequestException.class, () -> service.deleteResource(resourceId));

        verify(resourceRepository, times(1)).findByIdAndStatus(eq(resourceId), eq(Resource.Status.ACTIVE.value));
        verify(resourceRepository, never()).save(any());
    }
}
