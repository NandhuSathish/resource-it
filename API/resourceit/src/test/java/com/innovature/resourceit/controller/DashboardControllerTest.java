/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.responsedto.DashboardChartResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.DashboardCountResponseDTO;
import com.innovature.resourceit.service.DashboardService;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = DashboardControllerTest.class)
public class DashboardControllerTest {

    @Mock
    DashboardService dashboardService;

    @InjectMocks
    DashboardController dashboardController;

    @Test
    void testGetDashboardCounts() {

        DashboardCountResponseDTO dashboardCountResponseDTO = new DashboardCountResponseDTO();
        dashboardCountResponseDTO.setBenchResourceCount(2);
        dashboardCountResponseDTO.setBillableResourceCount(2);
        dashboardCountResponseDTO.setInternalResourceCount(3);
        dashboardCountResponseDTO.setTotalResourceCount(7);

        dashboardCountResponseDTO.setInternalProjectCount(1);
        dashboardCountResponseDTO.setBillableProjectCount(2);
        dashboardCountResponseDTO.setTotalProjectCount(3);

        when(dashboardService.fetchResourceAndProjectCount()).thenReturn(dashboardCountResponseDTO);

        ResponseEntity<DashboardCountResponseDTO> result = dashboardController.getDashboardCounts();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dashboardCountResponseDTO.getBenchResourceCount(), result.getBody().getBenchResourceCount());
    }

    @Test
    void testGetDashboardChart() {
        List<String> labels = new LinkedList<>();
        labels.add("Software");
        labels.add("HR");
        List<Integer> datas = new LinkedList<>();
        datas.add(1);
        datas.add(2);
        DashboardChartResponseDTO chartResponseDTO = new DashboardChartResponseDTO();
        chartResponseDTO.setLabels(labels);
        chartResponseDTO.setDatas(datas);

        List<String> allocationStatus = Arrays.asList("0");
        String flag = "0";
        List<Integer> skillIds = List.of(1);
        when(dashboardService.fetchResourceSkillLabelAndCount(allocationStatus, flag,skillIds)).thenReturn(chartResponseDTO);

        ResponseEntity<DashboardChartResponseDTO> result = dashboardController.getDashboardChart(allocationStatus, flag,skillIds);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(chartResponseDTO.getDatas().size(), result.getBody().getDatas().size());
        assertEquals(chartResponseDTO.getLabels().size(), result.getBody().getLabels().size());
    }
}
