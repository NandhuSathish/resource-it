/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

import com.innovature.resourceit.entity.ResourceSkillWiseAllocationRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abdul.fahad
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceSkillWiseAllocationResponseListDTO {

    private Integer id;

    private Integer projectId;

    private String projectCode;

    private String projectName;

    private Integer departmentId;

    private int experience;

    private int count;

    private String startDate;

    private String endDate;

    private int approvalFlow;

    private String departmentName;

    private List<SkillExperienceResponseDTO> skillExperienceResponseDTOs;

    private String managerName;

    public ResourceSkillWiseAllocationResponseListDTO(ResourceSkillWiseAllocationRequest resourceSkillWiseAllocationRequest) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.id = resourceSkillWiseAllocationRequest.getId();
        this.projectId = resourceSkillWiseAllocationRequest.getProject().getProjectId();
        this.projectCode = resourceSkillWiseAllocationRequest.getProject().getProjectCode();
        this.projectName = resourceSkillWiseAllocationRequest.getProject().getName();
        this.departmentId = resourceSkillWiseAllocationRequest.getDepartment().getDepartmentId();
        this.experience = resourceSkillWiseAllocationRequest.getExperience();
        this.count = resourceSkillWiseAllocationRequest.getResourceCount();
        this.startDate = formatDate(resourceSkillWiseAllocationRequest.getStartDate(), dateFormat);
        this.endDate = formatDate(resourceSkillWiseAllocationRequest.getEndDate(), dateFormat);
        this.approvalFlow = resourceSkillWiseAllocationRequest.getApprovalFlow();
        this.departmentName = resourceSkillWiseAllocationRequest.getDepartment().getName();
        this.skillExperienceResponseDTOs = resourceSkillWiseAllocationRequest.getSkillExperiences().stream().map(SkillExperienceResponseDTO::new).toList();
        this.managerName = resourceSkillWiseAllocationRequest.getRequestedBy().getName();
    }

    private String formatDate(Date date, SimpleDateFormat dateFormat) {
        return date != null ? dateFormat.format(date) : "";
    }

}
