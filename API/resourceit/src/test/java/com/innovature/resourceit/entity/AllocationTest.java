package com.innovature.resourceit.entity;

import com.innovature.resourceit.entity.dto.requestdto.ResourceAllocationRequestDTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = AllocationTest.class)
class AllocationTest {

    @Test
    void testAllocationEntity() {
        int allocationId = 1;
        Project project = new Project();
        Resource resource = new Resource();
        Date startDate = new Date();
        Date endDate = new Date();
        byte status = 1;
        byte allocationType = 2;
        Date currentDate = new Date();

        Allocation allocation = new Allocation();
        allocation.setId(allocationId);
        allocation.setProject(project);
        allocation.setResource(resource);
        allocation.setStartDate(startDate);
        allocation.setEndDate(endDate);
        allocation.setStatus(status);
        allocation.setCreatedDate(currentDate);
        allocation.setUpdatedDate(currentDate);

        assertEquals(allocationId, allocation.getId());
        assertEquals(project, allocation.getProject());
        assertEquals(resource, allocation.getResource());
        assertEquals(startDate, allocation.getStartDate());
        assertEquals(endDate, allocation.getEndDate());
        assertEquals(status, allocation.getStatus());
        assertEquals(currentDate, allocation.getCreatedDate());
        assertEquals(currentDate, allocation.getUpdatedDate());
    }

    @Test
    void testConstructorWithAllArguments() {
        int allocationId = 1;
        Project project = new Project();
        Resource resource = new Resource();
        Resource requestedBy = new Resource();
        Date startDate = new Date();
        Date endDate = new Date();
        byte status = 1;
        Date currentDate = new Date();
        byte isEdited = 0;
        byte isRemoved = 0;

        Allocation allocation = new Allocation(allocationId, project, resource, startDate, endDate, status, currentDate, currentDate, status, isEdited, isRemoved, requestedBy);

        assertEquals(allocationId, allocation.getId());
        assertEquals(project, allocation.getProject());
        assertEquals(resource, allocation.getResource());
        assertEquals(startDate, allocation.getStartDate());
        assertEquals(endDate, allocation.getEndDate());
        assertEquals(status, allocation.getStatus());
        assertEquals(currentDate, allocation.getCreatedDate());
        assertEquals(currentDate, allocation.getUpdatedDate());
    }

    @Test
    void testSetterAndGetterForId() {
        Allocation allocation = new Allocation();
        int allocationId = 1;
        allocation.setId(allocationId);
        assertEquals(allocationId, allocation.getId());
    }

    @Test
    void testSetterAndGetterForProject() {
        Allocation allocation = new Allocation();
        Project project = new Project();
        allocation.setProject(project);
        assertEquals(project, allocation.getProject());
    }

    @Test
    void testSetterAndGetterForResource() {
        Allocation allocation = new Allocation();
        Resource resource = new Resource();
        allocation.setResource(resource);
        assertEquals(resource, allocation.getResource());
    }

    @Test
    void testSetterAndGetterForStartDate() {
        Allocation allocation = new Allocation();
        Date startDate = new Date();
        allocation.setStartDate(startDate);
        assertEquals(startDate, allocation.getStartDate());
    }

    @Test
    void testSetterAndGetterForEndDate() {
        Allocation allocation = new Allocation();
        Date endDate = new Date();
        allocation.setEndDate(endDate);
        assertEquals(endDate, allocation.getEndDate());
    }

    @Test
    void testSetterAndGetterForStatus() {
        Allocation allocation = new Allocation();
        byte status = 1;
        allocation.setStatus(status);
        assertEquals(status, allocation.getStatus());
    }


    @Test
    void testSetterAndGetterForCreatedDate() {
        Allocation allocation = new Allocation();
        Date currentDate = new Date();
        allocation.setCreatedDate(currentDate);
        assertEquals(currentDate, allocation.getCreatedDate());
    }

    @Test
    void testSetterAndGetterForUpdatedDate() {
        Allocation allocation = new Allocation();
        Date currentDate = new Date();
        allocation.setUpdatedDate(currentDate);
        assertEquals(currentDate, allocation.getUpdatedDate());
    }

    @Test
    void testToStringMethod() {
        int allocationId = 1;
        Project project = new Project();
        Resource resource = new Resource();
        Resource requestedBy = new Resource();
        Date dt = new Date();
        Date startDate = dt;
        Date endDate = dt;
        byte status = 1;
        byte isEdited = 0;
        byte isRemoved = 0;
        Allocation allocation = new Allocation(allocationId, project, resource, startDate, endDate, status, dt, dt, status, isEdited, isRemoved, requestedBy);
        assertEquals(allocation.toString(), allocation.toString());
    }

    @Test
    void testStatusEnum() {
        Allocation.StatusValues deleteStatus = Allocation.StatusValues.DELETED;
        Allocation.StatusValues activeStatus = Allocation.StatusValues.ACTIVE;

        // Test equality
        assertEquals(Allocation.StatusValues.DELETED, deleteStatus);
        assertEquals(Allocation.StatusValues.ACTIVE, activeStatus);
        assertNotEquals(Allocation.StatusValues.DELETED, activeStatus);

        // Test value retrieval
        assertEquals((byte) 0, deleteStatus.value);
        assertEquals((byte) 1, activeStatus.value);
    }

    @Test
    void testAllocationContructorUsingResourceAllocationRequestDTO() {
        Project project = new Project();
        project.setProjectId(1);
        Resource resource = new Resource();
        Resource requestedBy = new Resource();
        resource.setId(1);

        ResourceAllocationRequestDTO allocationRequestDTO = new ResourceAllocationRequestDTO();
        allocationRequestDTO.setEndDate(new Date());
        allocationRequestDTO.setProjectId(1);
        allocationRequestDTO.setResourceId(1);
        allocationRequestDTO.setStartDate(new Date());

        Allocation allocation = new Allocation(allocationRequestDTO, requestedBy);

        assertEquals(project.getProjectId(), allocation.getProject().getProjectId());
        assertEquals(resource.getId(), allocation.getResource().getId());
    }

}
