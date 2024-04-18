package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.ProjectRequest;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Skill;
import com.innovature.resourceit.entity.dto.responsedto.ProjectRequestResponseDTO;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectRequestResponseDTOTest {

    @Test
    void testConstructorAndSetter() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Create sample data
        ProjectRequest projectRequest = createSampleProjectRequest();

        // Create ProjectRequestResponseDTO instance using the constructor
        ProjectRequestResponseDTO responseDTO = new ProjectRequestResponseDTO(projectRequest);

        // Verify values using getters
        verifyValues(responseDTO);

        // Create a new ProjectRequestResponseDTO instance
        ProjectRequestResponseDTO newResponseDTO = new ProjectRequestResponseDTO();

        // Set values using setter
        newResponseDTO.setProjectRequestId(1);
        newResponseDTO.setProject(createSampleProject());
        newResponseDTO.setProjectCode("PRJ-001");
        newResponseDTO.setName("Sample Project");
        newResponseDTO.setProjectType((byte) 1);
        newResponseDTO.setDescription("Sample Description");
        newResponseDTO.setClientName("Sample Client");
        newResponseDTO.setTeamSize(5);
        newResponseDTO.setManDay(10);
        newResponseDTO.setManager(createSampleResource());
        newResponseDTO.setStartDate("01-01-2023");
        newResponseDTO.setEndDate("05-01-2023");
        newResponseDTO.setSkill(Collections.singletonList(createSampleSkill()));
        newResponseDTO.setStatus((byte) 0);
        newResponseDTO.setApprovalStatus((byte) 1);
        newResponseDTO.setCreatedDate("01-01-2022");
        newResponseDTO.setUpdatedDate("02-01-2022");

        // Verify values using getters
        verifyValues(newResponseDTO);
    }

    private ProjectRequest createSampleProjectRequest() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectRequestId(1);
        projectRequest.setProject(createSampleProject());
        projectRequest.setProjectCode("PRJ-001");
        projectRequest.setName("Sample Project");
        projectRequest.setProjectType((byte) 1);
        projectRequest.setDescription("Sample Description");
        projectRequest.setClientName("Sample Client");
        projectRequest.setTeamSize(5);
        projectRequest.setManDay(10);
        projectRequest.setManager(createSampleResource());
        projectRequest.setStartDate(dateFormat.parse("01-01-2023"));
        projectRequest.setEndDate(dateFormat.parse("05-01-2023"));
        projectRequest.setSkill(Collections.singletonList(createSampleSkill()));
        projectRequest.setStatus((byte) 0);
        projectRequest.setApprovalStatus((byte) 1);
        projectRequest.setCreatedDate(dateFormat.parse("01-01-2022"));
        projectRequest.setUpdatedDate(dateFormat.parse("02-01-2022"));

        return projectRequest;
    }

    private Project createSampleProject() {
        Project project = new Project();
        project.setProjectId(1);
        project.setName("Sample Project");
        return project;
    }

    private Resource createSampleResource() {
        Resource resource = new Resource();
        resource.setId(1);
        resource.setName("Sample Manager");
        return resource;
    }

    private Skill createSampleSkill() {
        Skill skill = new Skill();
        skill.setId(1);
        skill.setName("Java");
        return skill;
    }

    private void verifyValues(ProjectRequestResponseDTO responseDTO) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        assertEquals(1, responseDTO.getProjectRequestId());
        assertEquals(createSampleProject().getProjectId(), responseDTO.getProject().getProjectId());
        assertEquals("PRJ-001", responseDTO.getProjectCode());
        assertEquals("Sample Project", responseDTO.getName());
        assertEquals((byte) 1, responseDTO.getProjectType());
        assertEquals("Sample Description", responseDTO.getDescription());
        assertEquals("Sample Client", responseDTO.getClientName());
        assertEquals(5, responseDTO.getTeamSize());
        assertEquals(10, responseDTO.getManDay());
        assertEquals(createSampleResource().getId(), responseDTO.getManager().getId());
        assertEquals("01-01-2023", responseDTO.getStartDate());
        assertEquals("05-01-2023", responseDTO.getEndDate());
        assertEquals(Collections.singletonList(createSampleSkill()).size(), responseDTO.getSkill().size());
        assertEquals((byte) 0, responseDTO.getStatus());
        assertEquals((byte) 1, responseDTO.getApprovalStatus());
        assertEquals("01-01-2022", responseDTO.getCreatedDate());
        assertEquals("02-01-2022", responseDTO.getUpdatedDate());
    }
}
