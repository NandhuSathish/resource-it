package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.controller.ResourceControllerTest;
import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.responsedto.ProjectListResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = ProjectListResponseDTOTest.class)
class ProjectListResponseDTOTest {


    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() {
       dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    }


        @Test
        void testProjectListResponseDTOInitialization() throws ParseException {
            // Create a sample Project object
            Project project = new Project();
            project.setProjectId(1);
            project.setProjectCode("P001");
            project.setName("Test Project");
            project.setProjectType((byte) 1);
            project.setDescription("Sample project description");
            project.setClientName("Client A");
            project.setTeamSize(10);
            project.setManDay(100);
            project.setManager(new Resource());
            project.setStartDate(dateFormat.parse("01-01-2022"));
            project.setEndDate(dateFormat.parse("01-02-2022"));
            project.setSkill(new ArrayList<>());
            project.setEdited((byte) 1);
            project.setStatus((byte) 2);
            project.setProjectState((byte) 0);
            project.setCreatedDate(dateFormat.parse("01-01-2022"));
            project.setUpdatedDate(dateFormat.parse("02-01-2022"));

            ProjectListResponseDTO responseDTO = new ProjectListResponseDTO(project);

            assertEquals(1, responseDTO.getProjectId());
            assertEquals("P001", responseDTO.getProjectCode());
            assertEquals("Test Project", responseDTO.getName());
            assertEquals((byte) 1, responseDTO.getProjectType());
            assertEquals("Sample project description", responseDTO.getDescription());
            assertEquals("Client A", responseDTO.getClientName());
            assertEquals(10, responseDTO.getTeamSize());
            assertEquals(100, responseDTO.getManDay());
            assertNotNull(responseDTO.getManager());
            assertEquals("01-01-2022", responseDTO.getStartDate());
            assertEquals("01-02-2022", responseDTO.getEndDate());
            assertNotNull(responseDTO.getSkill());
            assertEquals((byte) 1, responseDTO.getEdited());
            assertEquals((byte) 2, responseDTO.getStatus());
            assertEquals((byte) 0, responseDTO.getProjectState());
            assertEquals("01-01-2022", responseDTO.getCreatedDate());
            assertEquals("02-01-2022", responseDTO.getUpdatedDate());
        }


    @Test
    void testProjectListResponseDTOInitializationWithDatesNull() throws ParseException {
        // Create a sample Project object
        Project project = new Project();
        project.setProjectId(1);
        project.setProjectCode("P001");
        project.setName("Test Project");
        project.setProjectType((byte) 1);
        project.setDescription("Sample project description");
        project.setClientName("Client A");
        project.setTeamSize(10);
        project.setManDay(100);
        project.setManager(new Resource());
        project.setStartDate(null);
        project.setEndDate(null);
        project.setSkill(new ArrayList<>());
        project.setEdited((byte) 1);
        project.setStatus((byte) 2);
        project.setProjectState((byte) 0);
        project.setCreatedDate(dateFormat.parse("01-01-2022"));
        project.setUpdatedDate(dateFormat.parse("02-01-2022"));

        ProjectListResponseDTO responseDTO = new ProjectListResponseDTO(project);

        assertEquals(1, responseDTO.getProjectId());
        assertEquals("P001", responseDTO.getProjectCode());
        assertEquals("Test Project", responseDTO.getName());
        assertEquals((byte) 1, responseDTO.getProjectType());
        assertEquals("Sample project description", responseDTO.getDescription());
        assertEquals("Client A", responseDTO.getClientName());
        assertEquals(10, responseDTO.getTeamSize());
        assertEquals(100, responseDTO.getManDay());
        assertNotNull(responseDTO.getManager());
        assertEquals(null, responseDTO.getStartDate());
        assertEquals(null, responseDTO.getEndDate());
        assertNotNull(responseDTO.getSkill());
        assertEquals((byte) 1, responseDTO.getEdited());
        assertEquals((byte) 2, responseDTO.getStatus());
        assertEquals((byte) 0, responseDTO.getProjectState());
        assertEquals("01-01-2022", responseDTO.getCreatedDate());
        assertEquals("02-01-2022", responseDTO.getUpdatedDate());
    }

    @Test
    void testGettersAndSetters() throws ParseException {
        ProjectListResponseDTO responseDTO = new ProjectListResponseDTO();

        responseDTO.setProjectId(1);
        responseDTO.setProjectCode("P001");
        responseDTO.setName("Test Project");
        responseDTO.setProjectType((byte) 1);
        responseDTO.setDescription("Sample project description");
        responseDTO.setClientName("Client A");
        responseDTO.setTeamSize(10);
        responseDTO.setManDay(100);
        responseDTO.setManager(new Resource());
        responseDTO.setStartDate(null);
        responseDTO.setEndDate(null);
        responseDTO.setSkill(new ArrayList<>());
        responseDTO.setEdited((byte) 1);
        responseDTO.setStatus((byte) 2);
        responseDTO.setProjectState((byte) 0);
        responseDTO.setCreatedDate("01-01-2022");
        responseDTO.setUpdatedDate("02-01-2022");
        assertEquals(1, responseDTO.getProjectId());
        assertEquals("P001", responseDTO.getProjectCode());
        assertEquals("Test Project", responseDTO.getName());
        assertEquals((byte) 1, responseDTO.getProjectType());
        assertEquals("Sample project description", responseDTO.getDescription());
        assertEquals("Client A", responseDTO.getClientName());
        assertEquals(10, responseDTO.getTeamSize());
        assertEquals(100, responseDTO.getManDay());
        assertNotNull(responseDTO.getManager());
        assertEquals(null, responseDTO.getStartDate());
        assertEquals(null, responseDTO.getEndDate());
        assertNotNull(responseDTO.getSkill());
        assertEquals((byte) 1, responseDTO.getEdited());
        assertEquals((byte) 2, responseDTO.getStatus());
        assertEquals((byte) 0, responseDTO.getProjectState());
        assertEquals("01-01-2022", responseDTO.getCreatedDate());
        assertEquals("02-01-2022", responseDTO.getUpdatedDate());
    }
}
