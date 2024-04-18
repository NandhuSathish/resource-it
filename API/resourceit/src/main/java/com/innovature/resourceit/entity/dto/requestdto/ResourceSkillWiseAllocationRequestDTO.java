/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.requestdto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author abdul.fahad
 */
@Getter
@Setter
@Data
public class ResourceSkillWiseAllocationRequestDTO {

    private static final String PROJECT_ID_CANNOT_BE_NULL = "PROJECT_ID_CANNOT_BE_NULL";
    private static final String DEPARTMENT_REQUIRED = "DEPARTMENT_REQUIRED";
    private static final String START_DATE_REQUIRED = "START_DATE_REQUIRED";
    private static final String END_DATE_REQUIRED = "END_DATE_REQUIRED";
    private static final String EXPERIENCE_REQUIRED = "EXPERIENCE_REQUIRED";
    private static final String COUNT_REQUIRED = "COUNT_REQUIRED";
    private static final String SKILL_EXPERIENCE_REQUIRED = "SKILL_EXPERIENCE_REQUIRED";
    private static final String ALLOCATION_TYPE_CANNOT_BE_NULL = "ALLOCATION_TYPE_CANNOT_BE_NULL";

    @NotNull(message = PROJECT_ID_CANNOT_BE_NULL)
    @NotEmpty(message = PROJECT_ID_CANNOT_BE_NULL)
    private String projectId;

    @NotNull(message = DEPARTMENT_REQUIRED)
    @NotEmpty(message = DEPARTMENT_REQUIRED)
    private String departmentId;

    @NotNull(message = START_DATE_REQUIRED)
    @NotEmpty(message = START_DATE_REQUIRED)
    private String startDate;

    @NotNull(message = END_DATE_REQUIRED)
    @NotEmpty(message = END_DATE_REQUIRED)
    private String endDate;

    @NotNull(message = EXPERIENCE_REQUIRED)
    @NotEmpty(message = EXPERIENCE_REQUIRED)
    private String experience;
    @NotNull(message = COUNT_REQUIRED)
    @NotEmpty(message = COUNT_REQUIRED)
    private String count;

    @NotNull(message = SKILL_EXPERIENCE_REQUIRED)
    @NotEmpty(message = SKILL_EXPERIENCE_REQUIRED)
    private List<ResourceFilterSkillAndExperienceRequestDTO> skillExperienceRequestDTO;

}
