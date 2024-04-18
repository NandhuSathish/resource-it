package com.innovature.resourceit.entity;

import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectRequestTest {

    @Test
    void testArgumentConstructor() {
        List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "Java")));
        Project project = new Project(1);
        Resource manager = new Resource(2);
        Resource requestedBy = new Resource(1);

        Date dt = new Date();
        ProjectRequest projectRequest = new ProjectRequest(1, project, "Test Project", "PROJ123", "Description", "Client A",
                ProjectRequest.projectTypeValues.BILLABLE.value, 5, 100, requestedBy, manager, new Date(), new Date(),
                skills, ProjectRequest.approvalStatusValues.APPROVED.value, ProjectRequest.statusValues.ACTIVE.value,
                dt, dt);

        assertEquals(1, projectRequest.getProjectRequestId());
        assertEquals(project, projectRequest.getProject());
        assertEquals("PROJ123", projectRequest.getProjectCode());
        assertEquals("Test Project", projectRequest.getName());
        assertEquals("Description", projectRequest.getDescription());
        assertEquals("Client A", projectRequest.getClientName());
        assertEquals(ProjectRequest.projectTypeValues.BILLABLE.value, projectRequest.getProjectType());
        assertEquals(5, projectRequest.getTeamSize());
        assertEquals(100, projectRequest.getManDay());
        assertEquals(manager, projectRequest.getManager());
        assertEquals(dt.getTime(), projectRequest.getStartDate().getTime(), 100);
        assertEquals(dt.getTime(), projectRequest.getEndDate().getTime(), 100);
        assertEquals(skills, projectRequest.getSkill());
        assertEquals(ProjectRequest.approvalStatusValues.APPROVED.value, projectRequest.getApprovalStatus());
        assertEquals(ProjectRequest.statusValues.ACTIVE.value, projectRequest.getStatus());
        assertNotNull(projectRequest.getCreatedDate());
        assertNotNull(projectRequest.getUpdatedDate());
    }

    @Test
    void testConstructorWithDTOAndSkillsType1() {
        Resource requestedBy = new Resource(1);
        List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "Java")));
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setProjectId(null);
        dto.setProjectCode("PROJ123");
        dto.setName("Test Project");
        dto.setDescription("Description");
        dto.setClientName("Client A");
        dto.setProjectType(ProjectRequest.projectTypeValues.INTERNAL.value);
        dto.setManDay(100);
        dto.setManagerId(null);
        Date dt = new Date();
        dto.setStartDate(dt);
        dto.setEndDate(dt);
        dto.setSkillIds(new ArrayList<>());
        dto.setApprovalStatus(null);

        ProjectRequest projectRequest = new ProjectRequest(dto, skills, requestedBy);

        assertNull(projectRequest.getProjectRequestId());
        assertNull(projectRequest.getProject()); // This should be null as you set it to null in the constructor
        assertEquals("PROJ123", projectRequest.getProjectCode());
        assertEquals("Test Project", projectRequest.getName());
        assertEquals("Description", projectRequest.getDescription());
        assertEquals("Client A", projectRequest.getClientName());
        assertEquals(ProjectRequest.projectTypeValues.INTERNAL.value, projectRequest.getProjectType());
        assertEquals(0, projectRequest.getTeamSize());
        assertEquals(100, projectRequest.getManDay());
        assertNull(projectRequest.getManager()); // This should be null as you set it to null in the constructor
        assertEquals(dt, projectRequest.getStartDate());
        assertEquals(dt, projectRequest.getEndDate());
        assertEquals(skills, projectRequest.getSkill()); // Comparing with a list containing a sample skill
        assertEquals(ProjectRequest.approvalStatusValues.PENDING.value, projectRequest.getApprovalStatus());
        assertEquals(ProjectRequest.statusValues.ACTIVE.value, projectRequest.getStatus());
        assertNotNull(projectRequest.getCreatedDate());
        assertNotNull(projectRequest.getUpdatedDate());
    }

    @Test
    void testConstructorWithDTOAndSkillsType2() {
        List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "Java")));
        Resource requestedBy = new Resource(1);
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setProjectId(1);
        dto.setProjectCode("PROJ123");
        dto.setName("Test Project");
        dto.setDescription("Description");
        dto.setClientName("Client A");
        dto.setProjectType(ProjectRequest.projectTypeValues.BILLABLE.value);
        dto.setManDay(100);
        dto.setManagerId(2);
        Date dt = new Date();
        dto.setStartDate(dt);
        dto.setEndDate(dt);
        dto.setSkillIds(new ArrayList<>());
        dto.setApprovalStatus(ProjectRequest.approvalStatusValues.PENDING.value);

        ProjectRequest projectRequest = new ProjectRequest(dto, skills, requestedBy);

        assertNull(projectRequest.getProjectRequestId());
        assertEquals(1, projectRequest.getProject().getProjectId());
        assertEquals("PROJ123", projectRequest.getProjectCode());
        assertEquals("Test Project", projectRequest.getName());
        assertEquals("Description", projectRequest.getDescription());
        assertEquals("Client A", projectRequest.getClientName());
        assertEquals(ProjectRequest.projectTypeValues.BILLABLE.value, projectRequest.getProjectType());
        assertEquals(0, projectRequest.getTeamSize());
        assertEquals(100, projectRequest.getManDay());
        assertEquals(2, projectRequest.getManager().getId());
        assertEquals(dt, projectRequest.getStartDate());
        assertEquals(dt, projectRequest.getEndDate());
        assertEquals(skills, projectRequest.getSkill()); // Comparing with a list containing a sample skill
        assertEquals(ProjectRequest.approvalStatusValues.PENDING.value, projectRequest.getApprovalStatus());
        assertEquals(ProjectRequest.statusValues.ACTIVE.value, projectRequest.getStatus());
        assertNotNull(projectRequest.getCreatedDate());
        assertNotNull(projectRequest.getUpdatedDate());
    }


    @Test
    void testSetters() {
        ProjectRequest projectRequest = new ProjectRequest();

        Project project = new Project(1);
        projectRequest.setProjectRequestId(1);
        projectRequest.setProject(project);
        projectRequest.setProjectCode("PROJ123");
        projectRequest.setName("Test Project");
        projectRequest.setDescription("Description");
        projectRequest.setClientName("Client A");
        projectRequest.setProjectType(ProjectRequest.projectTypeValues.BILLABLE.value);
        projectRequest.setTeamSize(5);
        projectRequest.setManDay(100);
        Resource manager = new Resource(2);
        projectRequest.setManager(manager);
        Date startDate = new Date();
        projectRequest.setStartDate(startDate);
        Date endDate = new Date();
        projectRequest.setEndDate(endDate);
        List<Skill> skills = new ArrayList<>();
        projectRequest.setSkill(skills);
        projectRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.APPROVED.value);
        projectRequest.setStatus(ProjectRequest.statusValues.ACTIVE.value);
        Date createdDate = new Date();
        projectRequest.setCreatedDate(createdDate);
        Date updatedDate = new Date();
        projectRequest.setUpdatedDate(updatedDate);

        assertEquals(1, projectRequest.getProjectRequestId());
        assertEquals(project, projectRequest.getProject());
        assertEquals("PROJ123", projectRequest.getProjectCode());
        assertEquals("Test Project", projectRequest.getName());
        assertEquals("Description", projectRequest.getDescription());
        assertEquals("Client A", projectRequest.getClientName());
        assertEquals(ProjectRequest.projectTypeValues.BILLABLE.value, projectRequest.getProjectType());
        assertEquals(5, projectRequest.getTeamSize());
        assertEquals(100, projectRequest.getManDay());
        assertEquals(manager, projectRequest.getManager());
        assertEquals(startDate, projectRequest.getStartDate());
        assertEquals(endDate, projectRequest.getEndDate());
        assertEquals(skills, projectRequest.getSkill());
        assertEquals(ProjectRequest.approvalStatusValues.APPROVED.value, projectRequest.getApprovalStatus());
        assertEquals(ProjectRequest.statusValues.ACTIVE.value, projectRequest.getStatus());
        assertEquals(createdDate, projectRequest.getCreatedDate());
        assertEquals(updatedDate, projectRequest.getUpdatedDate());
    }

    @Test
    void testEnums() {
        assertEquals((byte) 0, ProjectRequest.approvalStatusValues.PENDING.value);
        assertEquals((byte) 1, ProjectRequest.approvalStatusValues.APPROVED.value);
        assertEquals((byte) 2, ProjectRequest.approvalStatusValues.REJECTED.value);
        assertEquals((byte) 0, ProjectRequest.projectTypeValues.INTERNAL.value);
        assertEquals((byte) 1, ProjectRequest.projectTypeValues.BILLABLE.value);
        assertEquals((byte) 1, ProjectRequest.statusValues.ACTIVE.value);
        assertEquals((byte) 0, ProjectRequest.statusValues.DELETED.value);
    }
}
