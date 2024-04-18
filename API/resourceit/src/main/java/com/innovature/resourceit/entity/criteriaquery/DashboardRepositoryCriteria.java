/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.entity.criteriaquery;

import com.innovature.resourceit.entity.dto.responsedto.DashboardChartResponseDTO;
import java.util.List;

/**
 *
 * @author abdul.fahad
 */
public interface DashboardRepositoryCriteria {
    
    DashboardChartResponseDTO findByDepartmentAllocationStatusIn(List<Integer> allocationStatusInt);
    
    DashboardChartResponseDTO findByIdInSkillAllocationStatusIn(List<Integer>skillIds,List<Integer> allocationStatusInt);
            
}
