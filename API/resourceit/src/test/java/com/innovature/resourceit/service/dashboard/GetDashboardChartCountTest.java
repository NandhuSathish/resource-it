/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.dashboard;

import com.innovature.resourceit.entity.criteriaquery.DashboardRepositoryCriteria;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.responsedto.DashboardChartResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.service.impli.DashboardServiceImpl;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = GetDashboardChartCountTest.class)
public class GetDashboardChartCountTest {
    
    @InjectMocks
    DashboardServiceImpl dashboardServiceImpl;
    
    @Mock
    ParameterValidator parameterValidator;

    @Mock
    MessageSource messageSource;

    @Mock
    DashboardRepositoryCriteria dashboardRepositoryCriteria;
    
    @Test
    public void testFetchDepartmentSkillLabelAndCount(){
        
        List<String> allocationStatus = Arrays.asList("0"); 
        String flag = "0";
        
        List<Integer> allocationStatusInt = Arrays.asList(0); 
        int flagInt = 0;
        
        List<String> labels = new LinkedList<>();
        labels.add("Software");
        labels.add("HR");
        List<Integer> datas = new LinkedList<>();
        datas.add(1);
        datas.add(2);
        DashboardChartResponseDTO chartResponseDTO = new DashboardChartResponseDTO();
        chartResponseDTO.setLabels(labels);
        chartResponseDTO.setDatas(datas);
        
        when(parameterValidator.isNumber("flag", flag)).thenReturn(flagInt);
        when(parameterValidator.isNumbersNum("allocationStatus", allocationStatus)).thenReturn(allocationStatusInt);
        when(dashboardRepositoryCriteria.findByDepartmentAllocationStatusIn(allocationStatusInt)).thenReturn(chartResponseDTO);
        List<Integer> skillIds = List.of(1);
        DashboardChartResponseDTO dcrdto = dashboardServiceImpl.fetchResourceSkillLabelAndCount(allocationStatus, flag,skillIds);
        
        Assertions.assertEquals(chartResponseDTO.getLabels().size(),dcrdto.getLabels().size());
    }
    
    @Test
    public void testFetchSkillLabelAndCount(){
        
        List<String> allocationStatus = Arrays.asList("0"); 
        String flag = "1";
        
        List<Integer> allocationStatusInt = Arrays.asList(0); 
        int flagInt = 1;
        
        List<String> labels = new LinkedList<>();
        labels.add("Java");
        labels.add("Angular");
        List<Integer> datas = new LinkedList<>();
        datas.add(1);
        datas.add(2);
        DashboardChartResponseDTO chartResponseDTO = new DashboardChartResponseDTO();
        chartResponseDTO.setLabels(labels);
        chartResponseDTO.setDatas(datas);
        
        when(parameterValidator.isNumbersNum("allocationStatus", allocationStatus)).thenReturn(allocationStatusInt);
        when(parameterValidator.isNumber("flag", flag)).thenReturn(flagInt);
        List<Integer> skillIds = List.of(1);
        when(dashboardRepositoryCriteria.findByIdInSkillAllocationStatusIn(skillIds,allocationStatusInt)).thenReturn(chartResponseDTO);
        
        DashboardChartResponseDTO dcrdto = dashboardServiceImpl.fetchResourceSkillLabelAndCount(allocationStatus, flag,skillIds);
        
        Assertions.assertEquals(chartResponseDTO.getLabels().size(),dcrdto.getLabels().size());
    }
    
    @Test
    public void testFetchSkillLabelAndCountInvalidFlag(){
        
        List<String> allocationStatus = Arrays.asList("0"); 
        String flag = "1";
        
        List<Integer> allocationStatusInt = Arrays.asList(0); 
        int flagInt = 2;
        
        List<String> labels = new LinkedList<>();
        labels.add("Java");
        labels.add("Angular");
        List<Integer> datas = new LinkedList<>();
        datas.add(1);
        datas.add(2);
        DashboardChartResponseDTO chartResponseDTO = new DashboardChartResponseDTO();
        chartResponseDTO.setLabels(labels);
        chartResponseDTO.setDatas(datas);
        
        when(parameterValidator.isNumbersNum("allocationStatus", allocationStatus)).thenReturn(allocationStatusInt);
        when(parameterValidator.isNumber("flag", flag)).thenReturn(flagInt);
        
         when(messageSource.getMessage(eq("INVALID_FLAG"), eq(null), any()))
                .thenReturn("2069-Invalid type for resource and skill");
        List<Integer> skillIds = List.of(1);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            dashboardServiceImpl.fetchResourceSkillLabelAndCount(allocationStatus, flag,skillIds);
        });

        assertNotNull(exception);
        assertEquals("2069-Invalid type for resource and skill", exception.getBody().getDetail());
    }
    
    @Test
    public void testFetchSkillLabelAndCountInvalidAllocationStatus(){
        
        List<String> allocationStatus = Arrays.asList("4"); 
        String flag = "0";
        
        List<Integer> allocationStatusInt = Arrays.asList(4); 
        int flagInt = 0;
        
        List<String> labels = new LinkedList<>();
        labels.add("Java");
        labels.add("Angular");
        List<Integer> datas = new LinkedList<>();
        datas.add(1);
        datas.add(2);
        DashboardChartResponseDTO chartResponseDTO = new DashboardChartResponseDTO();
        chartResponseDTO.setLabels(labels);
        chartResponseDTO.setDatas(datas);
        
        when(parameterValidator.isNumbersNum("allocationStatus", allocationStatus)).thenReturn(allocationStatusInt);
        when(parameterValidator.isNumber("flag", flag)).thenReturn(flagInt);
        
         when(messageSource.getMessage(eq("INVALID_ALLOCATION_STATUS"), eq(null), any()))
                .thenReturn("2071-Allocation status must be either 0 or 1 or 2.");
        List<Integer> skillIds = List.of(1);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            dashboardServiceImpl.fetchResourceSkillLabelAndCount(allocationStatus, flag,skillIds);
        });

        assertNotNull(exception);
        assertEquals("2071-Allocation status must be either 0 or 1 or 2.", exception.getBody().getDetail());
    }
    
}
