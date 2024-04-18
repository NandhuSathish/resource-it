/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.service;

import com.innovature.resourceit.entity.dto.responsedto.DashboardChartResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.DashboardCountResponseDTO;
import java.util.List;

/**
 *
 * @author abdul.fahad
 */
public interface DashboardService {
    
    DashboardCountResponseDTO fetchResourceAndProjectCount();
    
    DashboardChartResponseDTO fetchResourceSkillLabelAndCount(List<String> allocationStatus, String flag,List<Integer> skillIds);
}
