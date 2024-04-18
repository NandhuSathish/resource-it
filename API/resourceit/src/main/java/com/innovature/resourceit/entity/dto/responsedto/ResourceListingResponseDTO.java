/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author abdul.fahad
 */
@Getter
@Setter
public class ResourceListingResponseDTO {

    private int id;

    private String name;

    private Integer departmentId;

    private String departmentName;

    private String projectName;

    private String skillNameAndExperience;

    private List<ResourceSkillResponseDTO> resourceSkillResponseDTOs;

    private String totalExperience;

    private String joiningDate;

    private String email;

    private String allocationStatus;

    private Integer role;

    private String roleName;

    private String status;

    private int employeeId;

    private int aging;
}
