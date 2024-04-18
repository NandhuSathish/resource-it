package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.responsedto.ProjectListByManagerResponseDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectListByManagerResponseDTOTest {

    @Test
    void testProjectListByManagerResponseDTO() {
        Integer projectId = 1;
        String projectName = "Test Project";
        String projectCode ="123";

        ProjectListByManagerResponseDTO responseDTO = new ProjectListByManagerResponseDTO(projectId, projectName,projectCode);

        assertEquals(projectId, responseDTO.getProjectId());
        assertEquals(projectName, responseDTO.getProjectName());
        assertEquals(projectCode, responseDTO.getProjectCode());
    }

    @Test
    void testProjectListByManagerResponseDTOWithDefaultConstructor() {

        ProjectListByManagerResponseDTO responseDTO = new ProjectListByManagerResponseDTO();

        assertEquals(null, responseDTO.getProjectId());
        assertEquals(null, responseDTO.getProjectName());
        assertEquals(null, responseDTO.getProjectCode());
    }

    @Test
    void testSetterAndGetterMethods() {
        Integer projectId = 1;
        String projectName = "Test Project";
        String projectCode ="123";

        ProjectListByManagerResponseDTO responseDTO = new ProjectListByManagerResponseDTO();

        responseDTO.setProjectId(projectId);
        responseDTO.setProjectName(projectName);
        responseDTO.setProjectCode(projectCode);

        assertEquals(projectId, responseDTO.getProjectId());
        assertEquals(projectName, responseDTO.getProjectName());
        assertEquals(projectCode, responseDTO.getProjectCode());
    }
}
