package com.innovature.resourceit.service.batch;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.impli.BatchServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SendRemainderTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    ResourceRepository resourceRepository;

    @Mock
    AllocationRepository allocationRepository;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    CommonFunctions commonFunctions;

    @InjectMocks
    BatchServiceImpli batchServiceImpli;

    @Value("${spring.mail.username}")
    private String sendEmail;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendRemainder() throws ParseException {

        String mockedSendEmail = "mocked.email@example.com";
        ReflectionTestUtils.setField(batchServiceImpli, "sendEmail", mockedSendEmail);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        List<Integer> managerIds = Arrays.asList(3);
        Resource manager = new Resource(3);
        manager.setEmail("manager@gmail.com");

        Project p = new Project();
        p.setProjectId(1);
        p.setName("testProject");
        List<Project> projects = new ArrayList<>();
        projects.add(p);

        Allocation allocation = new Allocation();
        Department department = new Department();
        department.setName("testDepartment");
        Resource resource = new Resource(1);
        resource.setName("testResource");
        resource.setEmployeeId(1234);
        resource.setDepartment(department);
        allocation.setResource(resource);
        allocation.setStartDate(simpleDateFormat.parse("01-12-2023"));
        allocation.setEndDate(simpleDateFormat.parse("15-12-2023"));
        List<Allocation> allocations = new ArrayList<>();
        allocations.add(allocation);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        // Set up expectations
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(projectRepository.findAllManagerByProject(Project.statusValues.ACTIVE.value)).thenReturn(managerIds);
        when(resourceRepository.findByIdAndStatus(3, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(manager));
        when(projectRepository.findByManagerIdAndStatusAndProjectStateNotAndEdited(3, Project.statusValues.ACTIVE.value, Project.projectStateValues.COMPLETED.value, Project.editedValues.NOT_EDITED.value)).thenReturn(projects);
        when(allocationRepository.findAllAllocationFallsBetween(p.getProjectId(), Allocation.StatusValues.ACTIVE.value)).thenReturn(allocations);
        when(resourceRepository.findByIdAndStatus(allocation.getResource().getId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        when(commonFunctions.convertDateToTimestamp(any())).thenReturn(new Date());
        batchServiceImpli.sendReminder();
    }

    @Test
    public void testCheckingAllocation() {
        // Create sample data for testing
        Resource resource = new Resource(1);
        Department department = new Department();
        department.setName("something");
        department.setDepartmentId(1);
        resource.setDepartment(department);
        Resource resource1 = new Resource(2);
        Allocation allocation = new Allocation(1);
        allocation.setResource(resource);
        Project project = new Project(1);
        project.setManager(resource1);
        List<Project> projectList = new ArrayList<>();
        projectList.add(project);
        // Set the current date
        LocalDate localCurrentDate = LocalDate.now();
        Date currentDate = new Date();
        when(allocationRepository.findAllAllocationFallsBetween(anyInt(), anyByte())).thenReturn(List.of(allocation));
        when(resourceRepository.findByIdAndStatus(allocation.getResource().getId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        when(commonFunctions.convertDateToTimestamp(any())).thenReturn(new Date());
        String result = batchServiceImpli.checkingAllocation(projectList, localCurrentDate, currentDate);

        // Perform assertions based on the expected results
        assertNotNull(result);
    }

    @Test
    public void testCheckingAllocationBadrequestTest() {
        // Create sample data for testing
        List<Project> projectList = new ArrayList<>();
        // Add projects to the list

        // Set the current date
        LocalDate localCurrentDate = LocalDate.now();
        Date currentDate = new Date();

        assertThrows(BadRequestException.class, () -> batchServiceImpli.checkingAllocation(projectList, localCurrentDate, currentDate));


    }
}
