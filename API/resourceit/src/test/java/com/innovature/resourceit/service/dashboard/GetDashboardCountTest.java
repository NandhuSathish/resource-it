/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.dashboard;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.responsedto.DashboardCountResponseDTO;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.impli.DashboardServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = GetDashboardCountTest.class)
public class GetDashboardCountTest {
    
    @Mock
    ResourceRepository resourceRepository;

    @Mock
    ProjectRepository projectRepository;
    
    @InjectMocks
    DashboardServiceImpl dashboardServiceImpl;
    
    @Test
    public void testGetDashboardCount(){
        DashboardCountResponseDTO dashboardCountResponseDTO = new DashboardCountResponseDTO();
        long internalProjectCount = 1;
        long billableProjectCount = 2;
        long benchResource = 2;
        long externalResource = 2;
        long internalResource = 3;
        when(projectRepository.countByProjectTypeAndProjectStateAndStatus(Project.projectTypeValues.INTERNAL.value, Project.projectStateValues.IN_PROGRESS.value, Project.statusValues.ACTIVE.value)).thenReturn(internalProjectCount);
        when(projectRepository.countByProjectTypeAndProjectStateAndStatus(Project.projectTypeValues.BILLABLE.value, Project.projectStateValues.IN_PROGRESS.value, Project.statusValues.ACTIVE.value)).thenReturn(billableProjectCount);
        when(resourceRepository.countByAllocationStatusAndStatusAndNotRoleId(Resource.AllocationStatus.BENCH.value, Resource.Status.ACTIVE.value,1)).thenReturn(benchResource);
        when(resourceRepository.countByAllocationStatusAndStatusAndNotRoleId(Resource.AllocationStatus.EXTERNAL.value, Resource.Status.ACTIVE.value,1)).thenReturn(externalResource);
        when(resourceRepository.countByAllocationStatusAndStatusAndNotRoleId(Resource.AllocationStatus.INTERNAL.value, Resource.Status.ACTIVE.value,1)).thenReturn(internalResource);
        dashboardCountResponseDTO.setBenchResourceCount(2);
        dashboardCountResponseDTO.setBillableResourceCount(2);
        dashboardCountResponseDTO.setInternalResourceCount(3);
        dashboardCountResponseDTO.setTotalResourceCount(7);
        
        dashboardCountResponseDTO.setInternalProjectCount(1);
        dashboardCountResponseDTO.setBillableProjectCount(2);
        dashboardCountResponseDTO.setTotalProjectCount(3);
        
        DashboardCountResponseDTO resultDTO = dashboardServiceImpl.fetchResourceAndProjectCount();
        
        assertEquals(dashboardCountResponseDTO.getBenchResourceCount(), resultDTO.getBenchResourceCount());
        
    }
}
