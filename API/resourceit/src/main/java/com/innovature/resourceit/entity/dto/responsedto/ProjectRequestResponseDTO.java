package com.innovature.resourceit.entity.dto.responsedto;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.ProjectRequest;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Skill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class ProjectRequestResponseDTO {

    private Integer projectRequestId;
    private Project project;
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
    private Byte status;
    private Byte approvalStatus;
    private String createdDate;
    private String updatedDate;
    private List<String> editedFields;

    public ProjectRequestResponseDTO(ProjectRequest projectRequest) {
        initializeFields(projectRequest);
        checkProjectChanges(projectRequest);
    }

    private void initializeFields(ProjectRequest projectRequest) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.projectRequestId = projectRequest.getProjectRequestId();
        this.project = projectRequest.getProject();
        this.projectCode = projectRequest.getProjectCode();
        this.name = projectRequest.getName();
        this.projectType = projectRequest.getProjectType();
        this.description = projectRequest.getDescription();
        this.clientName = projectRequest.getClientName();
        this.teamSize = projectRequest.getTeamSize();
        this.manDay = projectRequest.getManDay();
        this.manager = projectRequest.getManager();
        this.startDate = formatDate(projectRequest.getStartDate(), dateFormat);
        this.endDate = formatDate(projectRequest.getEndDate(), dateFormat);
        this.skill = projectRequest.getSkill();
        this.status = projectRequest.getStatus();
        this.approvalStatus = projectRequest.getApprovalStatus();
        this.createdDate = formatDate(projectRequest.getCreatedDate(), dateFormat);
        this.updatedDate = formatDate(projectRequest.getUpdatedDate(), dateFormat);
    }

    private String formatDate(Date date, SimpleDateFormat dateFormat) {
        return date != null ? dateFormat.format(date) : "";
    }

    private void checkProjectChanges(ProjectRequest projectRequest) {
        List<String> changedFields = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        if (projectRequest.getProject() != null) {
            checkFieldChange("projectCode", this.getProjectCode(), projectRequest.getProject().getProjectCode(), changedFields);
            checkFieldChange("name", this.getName(), projectRequest.getProject().getName(), changedFields);
            checkFieldChange("projectType", this.getProjectType(), projectRequest.getProject().getProjectType(), changedFields);
            checkFieldChange("clientName", this.getClientName(), projectRequest.getProject().getClientName(), changedFields);
            checkFieldChange("manDay", this.getManDay(), projectRequest.getProject().getManDay(), changedFields);
            checkFieldChange("startDate", this.getStartDate(), formatDate(projectRequest.getProject().getStartDate(), dateFormat), changedFields);
            checkFieldChange("endDate", this.getEndDate(), formatDate(projectRequest.getProject().getEndDate(), dateFormat), changedFields);
        }

        this.editedFields = changedFields;
    }

    private void checkFieldChange(String fieldName, Object newValue, Object oldValue, List<String> changedFields) {
        if (!Objects.equals(oldValue, newValue)) {
            changedFields.add(fieldName);
        }
    }
}
