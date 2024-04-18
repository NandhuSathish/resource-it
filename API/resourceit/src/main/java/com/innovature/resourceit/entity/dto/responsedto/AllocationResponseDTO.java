/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

import com.innovature.resourceit.entity.Allocation;

import java.text.SimpleDateFormat;

import com.innovature.resourceit.entity.Resource;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abdul.fahad
 */
@Data
@NoArgsConstructor
public class AllocationResponseDTO {

    private int id;

    private String startDate;

    private String endDate;

    private int remainingWorkingDays;


    private String departmentName;
    private int projectId;
    private int resourceId;

    private String resourceName;

    private int employeeId;
    private Integer roleId;
    private Byte isEdited;

    private Byte isRemoved;
    private Resource requestedBy;

    public AllocationResponseDTO(Allocation allocation, int count) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.id = allocation.getId();
        this.startDate = dateFormat.format(allocation.getStartDate());
        this.endDate = dateFormat.format(allocation.getEndDate());
        this.remainingWorkingDays = count;
        this.departmentName = allocation.getResource().getDepartment().getName();
        this.resourceName = allocation.getResource().getName();
        this.resourceId = allocation.getResource().getId();
        this.projectId = allocation.getProject().getProjectId();
        this.employeeId = allocation.getResource().getEmployeeId();
        this.isEdited = allocation.getIsEdited();
        this.isRemoved = allocation.getIsRemoved();
        this.roleId = allocation.getResource().getRole().getId();
        this.requestedBy = allocation.getRequestedBy();
    }

}
