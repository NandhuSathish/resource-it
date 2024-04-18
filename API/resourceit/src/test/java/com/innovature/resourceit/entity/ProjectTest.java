package com.innovature.resourceit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Mock
    private Resource resourceMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Project project = new Project();
    }

    @Test
    void testDefaultConstructor() {
        Project project = new Project();
        assertNotNull(project);
        assertNull(project.getProjectId());
        assertNull(project.getProjectCode());
        assertNull(project.getName());
        assertNull(project.getProjectType());
        assertNull(project.getDescription());
        assertNull(project.getClientName());
        assertNull(project.getTeamSize());
        assertNull(project.getManDay());
        assertNull(project.getManager());
        assertNull(project.getStartDate());
        assertNull(project.getEndDate());
        assertNull(project.getSkill());
        assertNull(project.getEdited());
        assertNull(project.getStatus());
        assertNull(project.getProjectState());
        assertNull(project.getCreatedDate());
        assertNull(project.getUpdatedDate());
    }

    @Test
    void testConstructorWithAllArguments() {
        Date startDate = new Date();
        Date endDate = new Date();
        List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "Java")));

        Project project = new Project(1, "PROJ123", "Test Project", (byte) 0, "Sample project description", "Client A",
                5, 100, resourceMock, startDate, endDate, skills, (byte) 0, (byte) 1, (byte) 0, new Date(), new Date());

        assertEquals(1, project.getProjectId());
        assertEquals("PROJ123", project.getProjectCode());
        assertEquals("Test Project", project.getName());
        assertEquals((byte) 0, project.getProjectType());
        assertEquals("Sample project description", project.getDescription());
        assertEquals("Client A", project.getClientName());
        assertEquals(5, project.getTeamSize());
        assertEquals(100, project.getManDay());
        assertEquals(resourceMock, project.getManager());
        assertEquals(startDate, project.getStartDate());
        assertEquals(endDate, project.getEndDate());
        assertEquals(skills, project.getSkill());
        assertEquals((byte) 0, project.getEdited());
        assertEquals((byte) 1, project.getStatus());
        assertEquals((byte) 0, project.getProjectState());
    }

    @Test
    void testConstructorWithProjectId() {
        Project project = new Project(1);
        assertEquals(1, project.getProjectId());
    }

    @Test
    void testGettersAndSetters() {
        Project project = new Project();

        project.setProjectId(1);
        assertEquals(1, project.getProjectId());

        project.setProjectCode("PROJ123");
        assertEquals("PROJ123", project.getProjectCode());

        project.setName("Test Project");
        assertEquals("Test Project", project.getName());

        project.setProjectType((byte) 0);
        assertEquals((byte) 0, project.getProjectType());

        project.setDescription("Sample project description");
        assertEquals("Sample project description", project.getDescription());

        project.setClientName("Client A");
        assertEquals("Client A", project.getClientName());

        project.setTeamSize(5);
        assertEquals(5, project.getTeamSize());

        project.setManDay(100);
        assertEquals(100, project.getManDay());

        project.setManager(resourceMock);
        assertEquals(resourceMock, project.getManager());

        Date startDate = new Date();
        project.setStartDate(startDate);
        assertEquals(startDate, project.getStartDate());

        Date endDate = new Date();
        project.setEndDate(endDate);
        assertEquals(endDate, project.getEndDate());

        List<Skill> skills = new ArrayList<>(Arrays.asList(new Skill(1, "Java")));
        project.setSkill(skills);
        assertEquals(skills, project.getSkill());

        project.setEdited((byte) 0);
        assertEquals((byte) 0, project.getEdited());

        project.setStatus((byte) 1);
        assertEquals((byte) 1, project.getStatus());

        project.setProjectState((byte) 0);
        assertEquals((byte) 0, project.getProjectState());

        Date createdDate = new Date();
        project.setCreatedDate(createdDate);
        assertEquals(createdDate, project.getCreatedDate());

        Date updatedDate = new Date();
        project.setUpdatedDate(updatedDate);
        assertEquals(updatedDate, project.getUpdatedDate());
    }


    @Test
    void testProjectStateValues() {
        assertEquals((byte) 0, Project.projectStateValues.NOT_STARTED.value);
        assertEquals((byte) 1, Project.projectStateValues.IN_PROGRESS.value);
        assertEquals((byte) 2, Project.projectStateValues.COMPLETED.value);
    }

    @Test
    void testProjectTypeValues() {
        assertEquals((byte) 0, Project.projectTypeValues.INTERNAL.value);
        assertEquals((byte) 1, Project.projectTypeValues.BILLABLE.value);
    }

    @Test
    void testEditedValues() {
        assertEquals((byte) 0, Project.editedValues.NOT_EDITED.value);
        assertEquals((byte) 1, Project.editedValues.EDITED.value);
    }

    @Test
    void testStatusValues() {
        assertEquals((byte) 0, Project.statusValues.PENDING.value);
        assertEquals((byte) 1, Project.statusValues.ACTIVE.value);
        assertEquals((byte) 3, Project.statusValues.DELETED.value);
        assertEquals((byte) 2, Project.statusValues.HALFWAY.value);
    }

}
