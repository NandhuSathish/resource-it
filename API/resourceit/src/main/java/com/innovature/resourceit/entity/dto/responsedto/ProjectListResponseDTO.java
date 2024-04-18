package com.innovature.resourceit.entity.dto.responsedto;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Skill;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
public class ProjectListResponseDTO {


    private Integer projectId;
    private String projectCode;
    private String name;
    private Byte projectType;
    private String description;
    private String clientName;
    private Integer teamSize;
    private Integer manDay;
    private Resource manager;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String startDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String endDate;
    private Collection<Skill> skill;
    private Byte edited;
    private Byte status;
    private Byte projectState;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String createdDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String updatedDate;

    public ProjectListResponseDTO(Project project) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        this.projectId = project.getProjectId();
        this.projectCode = project.getProjectCode();
        this.name=project.getName();
        this.projectType = project.getProjectType();
        this.description =project.getDescription();
        this.clientName = project.getClientName();
        this.teamSize = project.getTeamSize();
        this.manDay = project.getManDay();
        this.manager = project.getManager();

        this.startDate = Objects.isNull(project.getStartDate()) ? null : dateFormat.format(project.getStartDate());
        this.endDate = Objects.isNull(project.getEndDate()) ? null : dateFormat.format(project.getEndDate());
        this.skill = project.getSkill();
        this.edited = project.getEdited();
        this.status = project.getStatus();
        this.projectState = project.getProjectState();
        this.createdDate = dateFormat.format(project.getCreatedDate());
        this.updatedDate = dateFormat.format(project.getUpdatedDate());

    }

    public ProjectListResponseDTO() {

    }
}
