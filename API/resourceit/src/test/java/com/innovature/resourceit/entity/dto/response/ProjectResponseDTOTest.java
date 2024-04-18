package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.responsedto.ProjectResponseDTO;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectResponseDTOTest {

    @Test
    void testProjectResponseDTO() throws ParseException {
        // Arrange
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = dateFormat.parse("01-01-2023");
        Date endDate = dateFormat.parse("31-12-2023");
        Date createdDate = dateFormat.parse("01-01-2023");
        Date updatedDate = dateFormat.parse("02-01-2023");

        Project project = new Project();
        project.setProjectId(1);
        project.setProjectCode("PROJ001");
        project.setName("Test Project");
        project.setProjectType((byte) 1);
        project.setDescription("Project Description");
        project.setClientName("Client XYZ");
        project.setTeamSize(10);
        project.setManDay(100);
        project.setManager(new Resource());
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setSkill(new ArrayList<>());
        project.setEdited((byte) 0);
        project.setStatus((byte) 1);
        project.setProjectState((byte) 1);
        project.setCreatedDate(createdDate);
        project.setUpdatedDate(updatedDate);

        // Act
        ProjectResponseDTO responseDTO = new ProjectResponseDTO(project);

        // Assert
        assertEquals(project.getProjectId(), responseDTO.getProjectId());
        assertEquals(project.getProjectCode(), responseDTO.getProjectCode());
        assertEquals(project.getName(), responseDTO.getName());
        assertEquals(project.getProjectType(), responseDTO.getProjectType());
        assertEquals(project.getDescription(), responseDTO.getDescription());
        assertEquals(project.getClientName(), responseDTO.getClientName());
        assertEquals(project.getTeamSize(), responseDTO.getTeamSize());
        assertEquals(project.getManDay(), responseDTO.getManDay());
        assertEquals(project.getManager(), responseDTO.getManager());
        assertEquals(dateFormat.format(project.getStartDate()), responseDTO.getStartDate());
        assertEquals(dateFormat.format(project.getEndDate()), responseDTO.getEndDate());
        assertEquals(project.getSkill(), responseDTO.getSkill());
        assertEquals(project.getEdited(), responseDTO.getEdited());
        assertEquals(project.getStatus(), responseDTO.getStatus());
        assertEquals(project.getProjectState(), responseDTO.getProjectState());
        assertEquals(dateFormat.format(project.getCreatedDate()), responseDTO.getCreatedDate());
        assertEquals(dateFormat.format(project.getUpdatedDate()), responseDTO.getUpdatedDate());
    }
}
