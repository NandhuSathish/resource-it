/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author abdul.fahad
 */
@Data
public class AllocationRequestResourceFilterResponseDTO {

    private int id;

    private String name;

    private Integer code;

    private String departmentName;

    private List<ResourceSkillResponseDTO> resourceSkillResponseDTOs;

    private int conflictDays;
    private Date joiningDate;
    private int experience;
}
