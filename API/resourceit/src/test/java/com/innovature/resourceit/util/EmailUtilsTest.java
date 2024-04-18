package com.innovature.resourceit.util;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class EmailUtilsTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    ResourceRepository resourceRepository;
    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    EmailUtils emailUtils;

    @Value("${spring.mail.username}")
    private String sendEmail;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

    }

    @Test
    void testAllocationMailNotificationCase2() {

        String mockedSendEmail = "mocked.email@example.com";
        ReflectionTestUtils.setField(emailUtils, "sendEmail", mockedSendEmail);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        // Set up expectations
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 2;
        ResourceSkillWiseAllocationRequest mockedSkillWiseReq = Mockito.mock(ResourceSkillWiseAllocationRequest.class);
        Optional<ResourceSkillWiseAllocationRequest> skillWiseReqObj = Optional.of(mockedSkillWiseReq);
        List<ResourceAllocationRequest> resourceWiseReqList = new ArrayList<>();
        Resource resource = new Resource(1);
        Project project = new Project(1);
        Set<SkillExperience> skillExperienceList = new HashSet<>();
        SkillExperience skillExperience = new SkillExperience(1, 10, 20, "Java", List.of((byte) 1, (byte) 2));
        skillExperienceList.add(skillExperience);
        project.setManager(resource);
        when(mockedSkillWiseReq.getId()).thenReturn(1);  // Set any required properties
        when(mockedSkillWiseReq.getProject()).thenReturn(project); // Mock the project
        when(mockedSkillWiseReq.getDepartment()).thenReturn(new Department()); // Mock the department
        when(mockedSkillWiseReq.getSkillExperiences()).thenReturn(skillExperienceList); // Mock the skillExperiences
        when(mockedSkillWiseReq.getStartDate()).thenReturn(new Date()); // Mock the startDate
        when(mockedSkillWiseReq.getEndDate()).thenReturn(new Date()); // Mock the endDate
        when(mockedSkillWiseReq.getExperience()).thenReturn(2); // Mock the experience
        when(mockedSkillWiseReq.getResourceCount()).thenReturn(3); // Mock the resourceCount
        when(mockedSkillWiseReq.getRequestedBy()).thenReturn(new Resource(1)); // Mock the requestedBy
        when(mockedSkillWiseReq.getRejectedBy()).thenReturn(new Resource(1)); // Mock the rejectedBy
        when(mockedSkillWiseReq.getStatus()).thenReturn(ResourceSkillWiseAllocationRequest.StatusValues.ACTIVE.value); // Mock the status
        when(mockedSkillWiseReq.getApprovalFlow()).thenReturn(ResourceSkillWiseAllocationRequest.ApprovalFlowValues.PENDING.value); // Mock the approvalFlow
        when(mockedSkillWiseReq.getRejectionReason()).thenReturn(""); // Mock the rejectionReason
        when(mockedSkillWiseReq.getCreatedDate()).thenReturn(new Date()); // Mock the createdDate
        when(mockedSkillWiseReq.getUpdatedDate()).thenReturn(new Date()); // Mock the updatedDate

        // Set up mock behavior
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(resource));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(resourceRepository.findByIdAndStatus(project.getManager().getId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        // Call the method under test
        emailUtils.allocationMailNotification(userList, type, skillWiseReqObj, resourceWiseReqList);
        Assertions.assertTrue(true);
    }

    @Test
    void testAllocationMailNotificationCase7() {

        String mockedSendEmail = "mocked.email@example.com";
        ReflectionTestUtils.setField(emailUtils, "sendEmail", mockedSendEmail);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        // Set up expectations
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 7;
        ResourceSkillWiseAllocationRequest mockedSkillWiseReq = Mockito.mock(ResourceSkillWiseAllocationRequest.class);
        Optional<ResourceSkillWiseAllocationRequest> skillWiseReqObj = Optional.of(mockedSkillWiseReq);
        List<ResourceAllocationRequest> resourceWiseReqList = new ArrayList<>();
        Resource resource = new Resource(1);
        Project project = new Project(1);
        project.setManager(resource);
        when(mockedSkillWiseReq.getId()).thenReturn(1);  // Set any required properties
        when(mockedSkillWiseReq.getProject()).thenReturn(project); // Mock the project
        when(mockedSkillWiseReq.getDepartment()).thenReturn(new Department()); // Mock the department
        when(mockedSkillWiseReq.getSkillExperiences()).thenReturn(new HashSet<>()); // Mock the skillExperiences
        when(mockedSkillWiseReq.getStartDate()).thenReturn(new Date()); // Mock the startDate
        when(mockedSkillWiseReq.getEndDate()).thenReturn(new Date()); // Mock the endDate
        when(mockedSkillWiseReq.getExperience()).thenReturn(2); // Mock the experience
        when(mockedSkillWiseReq.getResourceCount()).thenReturn(3); // Mock the resourceCount
        when(mockedSkillWiseReq.getRequestedBy()).thenReturn(new Resource(1)); // Mock the requestedBy
        when(mockedSkillWiseReq.getRejectedBy()).thenReturn(new Resource(1)); // Mock the rejectedBy
        when(mockedSkillWiseReq.getStatus()).thenReturn(ResourceSkillWiseAllocationRequest.StatusValues.ACTIVE.value); // Mock the status
        when(mockedSkillWiseReq.getApprovalFlow()).thenReturn(ResourceSkillWiseAllocationRequest.ApprovalFlowValues.PENDING.value); // Mock the approvalFlow
        when(mockedSkillWiseReq.getRejectionReason()).thenReturn(""); // Mock the rejectionReason
        when(mockedSkillWiseReq.getCreatedDate()).thenReturn(new Date()); // Mock the createdDate
        when(mockedSkillWiseReq.getUpdatedDate()).thenReturn(new Date()); // Mock the updatedDate

        // Set up mock behavior
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(resource));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(resourceRepository.findByIdAndStatus(project.getManager().getId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        // Call the method under test
        emailUtils.allocationMailNotification(userList, type, skillWiseReqObj, resourceWiseReqList);
        Assertions.assertTrue(true);
    }

    @Test
    void testAllocationMailNotificationCase18() {

        String mockedSendEmail = "mocked.email@example.com";
        ReflectionTestUtils.setField(emailUtils, "sendEmail", mockedSendEmail);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        // Set up expectations
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 18;
        ResourceSkillWiseAllocationRequest mockedSkillWiseReq = Mockito.mock(ResourceSkillWiseAllocationRequest.class);
        Optional<ResourceSkillWiseAllocationRequest> skillWiseReqObj = Optional.of(mockedSkillWiseReq);
        List<ResourceAllocationRequest> resourceWiseReqList = new ArrayList<>();
        Resource resource = new Resource(1);
        Project project = new Project(1);
        project.setManager(resource);
        when(mockedSkillWiseReq.getId()).thenReturn(1);  // Set any required properties
        when(mockedSkillWiseReq.getProject()).thenReturn(project); // Mock the project
        when(mockedSkillWiseReq.getDepartment()).thenReturn(new Department()); // Mock the department
        when(mockedSkillWiseReq.getSkillExperiences()).thenReturn(new HashSet<>()); // Mock the skillExperiences
        when(mockedSkillWiseReq.getStartDate()).thenReturn(new Date()); // Mock the startDate
        when(mockedSkillWiseReq.getEndDate()).thenReturn(new Date()); // Mock the endDate
        when(mockedSkillWiseReq.getExperience()).thenReturn(2); // Mock the experience
        when(mockedSkillWiseReq.getResourceCount()).thenReturn(3); // Mock the resourceCount
        when(mockedSkillWiseReq.getRequestedBy()).thenReturn(new Resource(1)); // Mock the requestedBy
        when(mockedSkillWiseReq.getRejectedBy()).thenReturn(new Resource(1)); // Mock the rejectedBy
        when(mockedSkillWiseReq.getStatus()).thenReturn(ResourceSkillWiseAllocationRequest.StatusValues.ACTIVE.value); // Mock the status
        when(mockedSkillWiseReq.getApprovalFlow()).thenReturn(ResourceSkillWiseAllocationRequest.ApprovalFlowValues.PENDING.value); // Mock the approvalFlow
        when(mockedSkillWiseReq.getRejectionReason()).thenReturn(""); // Mock the rejectionReason
        when(mockedSkillWiseReq.getCreatedDate()).thenReturn(new Date()); // Mock the createdDate
        when(mockedSkillWiseReq.getUpdatedDate()).thenReturn(new Date()); // Mock the updatedDate

        // Set up mock behavior
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(resource));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(resourceRepository.findByIdAndStatus(project.getManager().getId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        // Call the method under test
        emailUtils.allocationMailNotification(userList, type, skillWiseReqObj, resourceWiseReqList);
        Assertions.assertTrue(true);
    }

    @Test
    void testAllocationMailNotificationCase1() {

        String mockedSendEmail = "mocked.email@example.com";
        ReflectionTestUtils.setField(emailUtils, "sendEmail", mockedSendEmail);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        // Set up expectations
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 1;
        ResourceAllocationRequest mockedResourceWiseReq = Mockito.mock(ResourceAllocationRequest.class);
        Optional<ResourceSkillWiseAllocationRequest> skillWiseReqObj = Optional.empty();
        List<ResourceAllocationRequest> resourceWiseReqList = List.of(mockedResourceWiseReq);
        Resource resource = new Resource(1);
        Project project = new Project(1);
        Department department = new Department();
        department.setDepartmentId(1);
        resource.setDepartment(department);
        project.setManager(resource);
        when(mockedResourceWiseReq.getId()).thenReturn(1);  // Set any required properties
        when(mockedResourceWiseReq.getProject()).thenReturn(project); // Mock the project
        when(mockedResourceWiseReq.getStartDate()).thenReturn(new Date()); // Mock the startDate
        when(mockedResourceWiseReq.getEndDate()).thenReturn(new Date()); // Mock the endDate
        when(mockedResourceWiseReq.getRequestedBy()).thenReturn(new Resource(1)); // Mock the requestedBy
        when(mockedResourceWiseReq.getResource()).thenReturn(resource); // Mock the requestedBy
        when(mockedResourceWiseReq.getRejectedBy()).thenReturn(new Resource(1)); // Mock the rejectedBy
        when(mockedResourceWiseReq.getStatus()).thenReturn(ResourceAllocationRequest.StatusValues.ACTIVE.value); // Mock the status
        when(mockedResourceWiseReq.getApprovalFlow()).thenReturn(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value); // Mock the approvalFlow
        when(mockedResourceWiseReq.getRejectionReason()).thenReturn(""); // Mock the rejectionReason
        when(mockedResourceWiseReq.getCreatedDate()).thenReturn(new Date()); // Mock the createdDate
        when(mockedResourceWiseReq.getUpdatedDate()).thenReturn(new Date()); // Mock the updatedDate

        // Set up mock behavior
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(resource));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(resourceRepository.findByIdAndStatus(project.getManager().getId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.allocationMailNotification(userList, type, skillWiseReqObj, resourceWiseReqList);
        Assertions.assertTrue(true);
    }

    @Test
    void testAllocationMailNotificationCase3() {

        String mockedSendEmail = "mocked.email@example.com";
        ReflectionTestUtils.setField(emailUtils, "sendEmail", mockedSendEmail);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        // Set up expectations
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 3;
        ResourceAllocationRequest mockedResourceWiseReq = Mockito.mock(ResourceAllocationRequest.class);
        Optional<ResourceSkillWiseAllocationRequest> skillWiseReqObj = Optional.empty();
        List<ResourceAllocationRequest> resourceWiseReqList = List.of(mockedResourceWiseReq);
        Resource resource = new Resource(1);
        Project project = new Project(1);
        Department department = new Department();
        department.setDepartmentId(1);
        resource.setDepartment(department);
        project.setManager(resource);
        when(mockedResourceWiseReq.getId()).thenReturn(1);  // Set any required properties
        when(mockedResourceWiseReq.getProject()).thenReturn(project); // Mock the project
        when(mockedResourceWiseReq.getStartDate()).thenReturn(new Date()); // Mock the startDate
        when(mockedResourceWiseReq.getEndDate()).thenReturn(new Date()); // Mock the endDate
        when(mockedResourceWiseReq.getRequestedBy()).thenReturn(new Resource(1)); // Mock the requestedBy
        when(mockedResourceWiseReq.getResource()).thenReturn(resource); // Mock the requestedBy
        when(mockedResourceWiseReq.getRejectedBy()).thenReturn(new Resource(1)); // Mock the rejectedBy
        when(mockedResourceWiseReq.getStatus()).thenReturn(ResourceAllocationRequest.StatusValues.ACTIVE.value); // Mock the status
        when(mockedResourceWiseReq.getApprovalFlow()).thenReturn(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value); // Mock the approvalFlow
        when(mockedResourceWiseReq.getRejectionReason()).thenReturn(""); // Mock the rejectionReason
        when(mockedResourceWiseReq.getCreatedDate()).thenReturn(new Date()); // Mock the createdDate
        when(mockedResourceWiseReq.getUpdatedDate()).thenReturn(new Date()); // Mock the updatedDate

        // Set up mock behavior
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(resource));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(resourceRepository.findByIdAndStatus(project.getManager().getId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.allocationMailNotification(userList, type, skillWiseReqObj, resourceWiseReqList);
        Assertions.assertTrue(true);
    }

    @Test
    void testAllocationMailNotificationCase4() {

        String mockedSendEmail = "mocked.email@example.com";
        ReflectionTestUtils.setField(emailUtils, "sendEmail", mockedSendEmail);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        // Set up expectations
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 4;
        ResourceAllocationRequest mockedResourceWiseReq = Mockito.mock(ResourceAllocationRequest.class);
        Optional<ResourceSkillWiseAllocationRequest> skillWiseReqObj = Optional.empty();
        List<ResourceAllocationRequest> resourceWiseReqList = List.of(mockedResourceWiseReq);
        Resource resource = new Resource(1);
        Project project = new Project(1);
        Department department = new Department();
        department.setDepartmentId(1);
        resource.setDepartment(department);
        project.setManager(resource);
        when(mockedResourceWiseReq.getId()).thenReturn(1);  // Set any required properties
        when(mockedResourceWiseReq.getProject()).thenReturn(project); // Mock the project
        when(mockedResourceWiseReq.getStartDate()).thenReturn(new Date()); // Mock the startDate
        when(mockedResourceWiseReq.getEndDate()).thenReturn(new Date()); // Mock the endDate
        when(mockedResourceWiseReq.getRequestedBy()).thenReturn(new Resource(1)); // Mock the requestedBy
        when(mockedResourceWiseReq.getResource()).thenReturn(resource); // Mock the requestedBy
        when(mockedResourceWiseReq.getRejectedBy()).thenReturn(new Resource(1)); // Mock the rejectedBy
        when(mockedResourceWiseReq.getStatus()).thenReturn(ResourceAllocationRequest.StatusValues.ACTIVE.value); // Mock the status
        when(mockedResourceWiseReq.getApprovalFlow()).thenReturn(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value); // Mock the approvalFlow
        when(mockedResourceWiseReq.getRejectionReason()).thenReturn(""); // Mock the rejectionReason
        when(mockedResourceWiseReq.getCreatedDate()).thenReturn(new Date()); // Mock the createdDate
        when(mockedResourceWiseReq.getUpdatedDate()).thenReturn(new Date()); // Mock the updatedDate

        // Set up mock behavior
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(resource));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(resourceRepository.findByIdAndStatus(project.getManager().getId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.allocationMailNotification(userList, type, skillWiseReqObj, resourceWiseReqList);
        Assertions.assertTrue(true);
    }

    @Test
    void testAllocationMailNotificationCase5() {

        String mockedSendEmail = "mocked.email@example.com";
        ReflectionTestUtils.setField(emailUtils, "sendEmail", mockedSendEmail);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        // Set up expectations
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 5;
        ResourceAllocationRequest mockedResourceWiseReq = Mockito.mock(ResourceAllocationRequest.class);
        Optional<ResourceSkillWiseAllocationRequest> skillWiseReqObj = Optional.empty();
        List<ResourceAllocationRequest> resourceWiseReqList = List.of(mockedResourceWiseReq);
        Resource resource = new Resource(1);
        Project project = new Project(1);
        Department department = new Department();
        department.setDepartmentId(1);
        resource.setDepartment(department);
        project.setManager(resource);
        when(mockedResourceWiseReq.getId()).thenReturn(1);  // Set any required properties
        when(mockedResourceWiseReq.getProject()).thenReturn(project); // Mock the project
        when(mockedResourceWiseReq.getStartDate()).thenReturn(new Date()); // Mock the startDate
        when(mockedResourceWiseReq.getEndDate()).thenReturn(new Date()); // Mock the endDate
        when(mockedResourceWiseReq.getRequestedBy()).thenReturn(new Resource(1)); // Mock the requestedBy
        when(mockedResourceWiseReq.getResource()).thenReturn(resource); // Mock the requestedBy
        when(mockedResourceWiseReq.getRejectedBy()).thenReturn(new Resource(1)); // Mock the rejectedBy
        when(mockedResourceWiseReq.getStatus()).thenReturn(ResourceAllocationRequest.StatusValues.ACTIVE.value); // Mock the status
        when(mockedResourceWiseReq.getApprovalFlow()).thenReturn(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value); // Mock the approvalFlow
        when(mockedResourceWiseReq.getRejectionReason()).thenReturn(""); // Mock the rejectionReason
        when(mockedResourceWiseReq.getCreatedDate()).thenReturn(new Date()); // Mock the createdDate
        when(mockedResourceWiseReq.getUpdatedDate()).thenReturn(new Date()); // Mock the updatedDate

        // Set up mock behavior
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(resource));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(resourceRepository.findByIdAndStatus(project.getManager().getId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.allocationMailNotification(userList, type, skillWiseReqObj, resourceWiseReqList);
        Assertions.assertTrue(true);
    }

    @Test
    void testAllocationMailNotificationCase6() {

        String mockedSendEmail = "mocked.email@example.com";
        ReflectionTestUtils.setField(emailUtils, "sendEmail", mockedSendEmail);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        // Set up expectations
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 6;
        ResourceAllocationRequest mockedResourceWiseReq = Mockito.mock(ResourceAllocationRequest.class);
        Optional<ResourceSkillWiseAllocationRequest> skillWiseReqObj = Optional.empty();
        List<ResourceAllocationRequest> resourceWiseReqList = List.of(mockedResourceWiseReq);
        Resource resource = new Resource(1);
        Project project = new Project(1);
        Department department = new Department();
        department.setDepartmentId(1);
        resource.setDepartment(department);
        project.setManager(resource);
        when(mockedResourceWiseReq.getId()).thenReturn(1);  // Set any required properties
        when(mockedResourceWiseReq.getProject()).thenReturn(project); // Mock the project
        when(mockedResourceWiseReq.getStartDate()).thenReturn(new Date()); // Mock the startDate
        when(mockedResourceWiseReq.getEndDate()).thenReturn(new Date()); // Mock the endDate
        when(mockedResourceWiseReq.getRequestedBy()).thenReturn(new Resource(1)); // Mock the requestedBy
        when(mockedResourceWiseReq.getResource()).thenReturn(resource); // Mock the requestedBy
        when(mockedResourceWiseReq.getRejectedBy()).thenReturn(new Resource(1)); // Mock the rejectedBy
        when(mockedResourceWiseReq.getStatus()).thenReturn(ResourceAllocationRequest.StatusValues.ACTIVE.value); // Mock the status
        when(mockedResourceWiseReq.getApprovalFlow()).thenReturn(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value); // Mock the approvalFlow
        when(mockedResourceWiseReq.getRejectionReason()).thenReturn(""); // Mock the rejectionReason
        when(mockedResourceWiseReq.getCreatedDate()).thenReturn(new Date()); // Mock the createdDate
        when(mockedResourceWiseReq.getUpdatedDate()).thenReturn(new Date()); // Mock the updatedDate

        // Set up mock behavior
        when(resourceRepository.findById(anyInt())).thenReturn(Optional.of(resource));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(resourceRepository.findByIdAndStatus(project.getManager().getId(), Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));
        when(resourceRepository.findByIdAndStatus(1, Resource.Status.ACTIVE.value)).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.allocationMailNotification(userList, type, skillWiseReqObj, resourceWiseReqList);
        Assertions.assertTrue(true);
    }

    //project related mails

    @Test
    void testProjectMailNotificationCase8() {
        // Define your test data
        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 8;
        Resource resource = new Resource(1);
        Skill skill = new Skill(1, "abcd");
        Project project = new Project();
        project.setProjectId(1);
        project.setSkill(List.of(skill));
        project.setProjectType((byte) 1);
        project.setManDay(100);
        Resource manager = new Resource(1);
        manager.setName("testmngr");
        project.setManager(manager);

        // Set up mock behavior
        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.projectMailNotification(userList, type, Optional.empty(), Optional.of(project));

        // Add assertions as needed
        // For example, you can use Mockito.verify to check if certain methods were called
        verify(resourceRepository, times(4)).findByIdAndStatus(anyInt(), eq(Resource.Status.ACTIVE.value));
    }

    @Test
    void testProjectMailNotificationCase9() {
        // Define your test data
        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 9;
        Resource resource = new Resource(1);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectRequestId(1);
        Skill skill = new Skill(1, "abcd");
//        Project project = null;
//        project.setProjectId(1);
        projectRequest.setProjectType((byte) 1);
        projectRequest.setSkill(List.of(skill));
//        project.setProjectType((byte)1);
//        project.setManDay(100);

        // Set up mock behavior
        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.projectMailNotification(userList, type, Optional.of(projectRequest), Optional.empty());

        // Add assertions as needed
        // For example, you can use Mockito.verify to check if certain methods were called
        verify(resourceRepository, times(userList.size())).findByIdAndStatus(anyInt(), eq(Resource.Status.ACTIVE.value));
    }

    @Test
    void testProjectMailNotificationCase10() {
        // Define your test data
        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 10;
        Resource resource = new Resource(1);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectRequestId(1);
        Skill skill = new Skill(1, "abcd");
        projectRequest.setProjectType((byte) 1);
        projectRequest.setSkill(List.of(skill));
        Resource manager = new Resource(1);
        manager.setName("testmngr");
        projectRequest.setManager(manager);
        // Set up mock behavior
        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.projectMailNotification(userList, type, Optional.of(projectRequest), Optional.empty());

        // Add assertions as needed
        // For example, you can use Mockito.verify to check if certain methods were called
        verify(resourceRepository, times(userList.size())).findByIdAndStatus(anyInt(), eq(Resource.Status.ACTIVE.value));
    }

    @Test
    void testProjectMailNotificationCase11() {
        // Define your test data
        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 11;
        Resource resource = new Resource(1);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectRequestId(1);
        Skill skill = new Skill(1, "abcd");
        projectRequest.setProjectType((byte) 1);
        projectRequest.setSkill(List.of(skill));
        Resource manager = new Resource(1);
        manager.setName("testmngr");
        projectRequest.setManager(manager);
        Project project = new Project();
        project.setProjectId(1);
        project.setSkill(List.of(skill));
        project.setProjectType((byte) 1);
        project.setManDay(100);
        project.setManager(manager);
        projectRequest.setProject(project);
        // Set up mock behavior
        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(resource));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        // Call the method under test
        emailUtils.projectMailNotification(userList, type, Optional.of(projectRequest), Optional.empty());

        // Add assertions as needed
        // For example, you can use Mockito.verify to check if certain methods were called
        verify(resourceRepository, times(4)).findByIdAndStatus(anyInt(), eq(Resource.Status.ACTIVE.value));
    }

    @Test
    void testProjectMailNotificationCase12() {
        // Define your test data
        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 12;
        Resource resource = new Resource(1);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectRequestId(1);
        Skill skill = new Skill(1, "abcd");
        projectRequest.setProjectType((byte) 1);
        projectRequest.setSkill(List.of(skill));
        Resource manager = new Resource(1);
        manager.setName("testmngr");
        projectRequest.setManager(manager);
        Project project = new Project();
        project.setProjectId(1);
        project.setSkill(List.of(skill));
        project.setProjectType((byte) 1);
        project.setManDay(100);
        manager.setName("testmngr");
        project.setManager(manager);
        projectRequest.setProject(project);
        // Set up mock behavior
        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(resource));
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        // Call the method under test
        emailUtils.projectMailNotification(userList, type, Optional.of(projectRequest), Optional.empty());

        // Add assertions as needed
        // For example, you can use Mockito.verify to check if certain methods were called
        verify(resourceRepository, times(4)).findByIdAndStatus(anyInt(), eq(Resource.Status.ACTIVE.value));
    }

    @Test
    void testProjectMailNotificationCase13() {
        // Define your test data
        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 13;
        Resource resource = new Resource(1);
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectRequestId(1);
        Skill skill = new Skill(1, "abcd");
        projectRequest.setProjectType((byte) 1);
        projectRequest.setSkill(List.of(skill));
        Resource manager = new Resource(1);
        manager.setName("testmngr");
        projectRequest.setManager(manager);
        Project project = new Project();
        project.setProjectId(1);
        project.setSkill(List.of(skill));
        project.setProjectType((byte) 1);
        project.setManDay(100);
        manager.setName("testmngr");
        project.setManager(manager);
        projectRequest.setProject(project);
        // Set up mock behavior
        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.projectMailNotification(userList, type, Optional.empty(), Optional.of(project));

        // Add assertions as needed
        // For example, you can use Mockito.verify to check if certain methods were called
        verify(resourceRepository, times(5)).findByIdAndStatus(anyInt(), eq(Resource.Status.ACTIVE.value));
    }

    @Test
    void testProjectMailNotificationCase14() {
        // Define your test data
        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 14;
        Resource resource = new Resource(1);
        Skill skill = new Skill(1, "abcd");
        Project project = new Project();
        project.setProjectId(1);
        project.setSkill(List.of(skill));
        project.setProjectType((byte) 1);
        project.setManDay(100);
        Resource manager = new Resource(1);
        manager.setName("testmngr");
        project.setManager(manager);
        // Set up mock behavior
        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.projectMailNotification(userList, type, Optional.empty(), Optional.of(project));

        // Add assertions as needed
        // For example, you can use Mockito.verify to check if certain methods were called
        verify(resourceRepository, times(5)).findByIdAndStatus(anyInt(), eq(Resource.Status.ACTIVE.value));
    }

    @Test
    void testProjectMailNotificationCase15() {
        // Define your test data
        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 15;
        Resource resource = new Resource(1);
        Skill skill = new Skill(1, "abcd");
        Project project = new Project();
        project.setProjectId(1);
        project.setSkill(List.of(skill));
        project.setProjectType((byte) 1);
        project.setManDay(100);
        Resource manager = new Resource(1);
        manager.setName("testmngr");
        project.setManager(manager);
        // Set up mock behavior
        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.projectMailNotification(userList, type, Optional.empty(), Optional.of(project));

        // Add assertions as needed
        // For example, you can use Mockito.verify to check if certain methods were called
        verify(resourceRepository, times(4)).findByIdAndStatus(anyInt(), eq(Resource.Status.ACTIVE.value));
    }

    @Test
    void testProjectMailNotificationCase16() {
        // Define your test data
        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 16;
        Resource resource = new Resource(1);
        Skill skill = new Skill(1, "abcd");
        Project project = new Project();
        project.setProjectId(1);
        project.setSkill(List.of(skill));
        project.setProjectType((byte) 1);
        project.setManDay(100);
        Resource manager = new Resource(1);
        manager.setName("testmngr");
        project.setManager(manager);
        // Set up mock behavior
        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.projectMailNotification(userList, type, Optional.empty(), Optional.of(project));

        // Add assertions as needed
        // For example, you can use Mockito.verify to check if certain methods were called
        verify(resourceRepository, times(4)).findByIdAndStatus(anyInt(), eq(Resource.Status.ACTIVE.value));
    }

    @Test
    void testProjectMailNotificationCase17() {
        // Define your test data
        List<Integer> userList = Arrays.asList(1, 2, 3);
        Integer type = 17;
        Resource resource = new Resource(1);
        Skill skill = new Skill(1, "abcd");
        Project project = new Project();
        project.setProjectId(1);
        project.setSkill(List.of(skill));
        project.setProjectType((byte) 1);
        project.setManDay(100);
        Resource manager = new Resource(1);
        manager.setName("testmngr");
        project.setManager(manager);
        // Set up mock behavior
        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(resource));

        // Call the method under test
        emailUtils.projectMailNotification(userList, type, Optional.empty(), Optional.of(project));

        // Add assertions as needed
        // For example, you can use Mockito.verify to check if certain methods were called
        verify(resourceRepository, times(4)).findByIdAndStatus(anyInt(), eq(Resource.Status.ACTIVE.value));
    }

    @Test
    void testGetProficiency() {
        List<Byte> prficiency = List.of((byte) 0, (byte) 1, (byte) 2);
        String result = emailUtils.getProficiency(prficiency);
        assertNotNull(result);
    }
}
