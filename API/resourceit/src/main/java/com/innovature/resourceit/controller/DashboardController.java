/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.responsedto.DashboardChartResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.DashboardCountResponseDTO;
import com.innovature.resourceit.service.DashboardService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author abdul.fahad
 */
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    
    @Autowired
    DashboardService dashboardService;
    
    @GetMapping("/count")
    public ResponseEntity<DashboardCountResponseDTO> getDashboardCounts(){
        DashboardCountResponseDTO dashboardCountResponseDTO = dashboardService.fetchResourceAndProjectCount();
        return new ResponseEntity<>(dashboardCountResponseDTO,HttpStatus.OK);
    }
    
    @GetMapping("/chart")
    public ResponseEntity<DashboardChartResponseDTO> getDashboardChart(@RequestParam(name = "allocationStatus", required = false) List<String> allocationStatus, @RequestParam("flag") String flag,
                                                                       @RequestParam(name = "skillIds", required = false) List<Integer> skillIds){
        // flag 0 means resource
        // flag 1 means skill
        return new ResponseEntity<>(dashboardService.fetchResourceSkillLabelAndCount(allocationStatus, flag,skillIds),HttpStatus.OK);
    }
}
