/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.requestdto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author abdul.fahad
 */
@Getter
@Setter
public class ResourceDownloadFilterRequestDTO {

    private String name;

    private String employeeId;

    private List<String> departmentIds;

    private List<String> projectIds;

    private List<String> roleIds;

    private String lowerExperience;

    private String highExperience;

    private List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperiences;


    private String status;

    private List<String> allocationStatus;

    private String sortKey;
    private Boolean sortOrder = false;

}
