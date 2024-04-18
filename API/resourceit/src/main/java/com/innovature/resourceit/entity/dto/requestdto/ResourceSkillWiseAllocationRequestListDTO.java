/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.requestdto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author abdul.fahad
 */
@Data
public class ResourceSkillWiseAllocationRequestListDTO {

    private List<String> departmentIds;

    private List<String> projectIds;

    private List<String> approvalStatus;
    
    private String pageNumber;

    private String pageSize;
    
    private List<String> managerIds;
}
