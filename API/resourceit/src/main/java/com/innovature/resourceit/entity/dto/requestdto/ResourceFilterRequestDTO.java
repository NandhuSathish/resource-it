/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author abdul.fahad
 */
@Getter
@Setter
@NoArgsConstructor
public class ResourceFilterRequestDTO {

    private String name;

    private String employeeId;

    private List<String> departmentIds;

    private List<String> projectIds;

    private List<String> roleIds;


    private String lowerExperience;

    private String highExperience;

    private String status;

    private List<String> allocationStatus;

    private String pageNumber;

    private String pageSize;

    private Boolean sortOrder = false;

    private String sortKey;

    private List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperiences;
}
