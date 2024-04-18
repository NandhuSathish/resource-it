package com.innovature.resourceit.entity.dto.responsedto;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Skill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class ProjectResponseDTO {
    private Integer projectId;
    private String projectCode;
    private String name;
    private Byte projectType;
    private String description;
    private String clientName;
    private Integer teamSize;
    private Integer manDay;
    private Resource manager;
    private String startDate;
    private String endDate;
    private Collection<Skill> skill;
    private Byte edited;
    private Byte status;
    private Byte projectState;
    private String createdDate;
    private String updatedDate;

    public ProjectResponseDTO(Project project) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.projectId = project.getProjectId();
        this.projectCode = project.getProjectCode();
        this.name = project.getName();
        this.projectType = project.getProjectType();
        this.description = project.getDescription();
        this.clientName = project.getClientName();
        this.teamSize = project.getTeamSize();
        this.manDay = project.getManDay();
        this.manager = project.getManager();
        this.startDate = project.getStartDate() != null ? dateFormat.format(project.getStartDate()) : "";
        this.endDate = project.getEndDate() != null ? dateFormat.format(project.getEndDate()) : "";
        this.skill = project.getSkill();
        this.edited = project.getEdited();
        this.status = project.getStatus();
        this.projectState = project.getProjectState();
        this.createdDate = project.getCreatedDate() != null ? dateFormat.format(project.getCreatedDate()) : "";
        this.updatedDate = project.getUpdatedDate() != null ? dateFormat.format(project.getUpdatedDate()) : "";

    }
}
