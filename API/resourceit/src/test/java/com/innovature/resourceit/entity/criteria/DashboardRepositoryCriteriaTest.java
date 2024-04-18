/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.criteria;

import com.innovature.resourceit.entity.criteriaquery.impl.DashboardRepositoryCriteriaImpl;
import com.innovature.resourceit.entity.dto.responsedto.DashboardChartResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = DashboardRepositoryCriteriaTest.class)
public class DashboardRepositoryCriteriaTest {
    
    @Mock
    Query query;
    
    @Mock
    EntityManager entityManager;
    
    @InjectMocks
    DashboardRepositoryCriteriaImpl dashboardRepositoryCriteriaImpl;
    
    @Test
    public void findByDepartmentAllocationStatusInTest(){
        
        List<Integer> allocationStatusInt = Arrays.asList(0); 
        
        StringBuilder nativeQuery = new StringBuilder("SELECT  d.name as departmentName, count(r.id) as count FROM resource r , department d ");
        nativeQuery.append("where r.department_id = d.department_id and r.status = 1 and r.role_id <> 1 and r.allocation_status in :allocationStatus group by r.department_id order by d.display_order");
        
        List<Object> os = new ArrayList<>();
        Object[] o = Arrays.asList("Software",2).toArray();
        os.add(o);
        
        List<String> labels = new LinkedList<>();
        labels.add("Software");
        List<Integer> datas = new LinkedList<>();
        datas.add(2);
        DashboardChartResponseDTO chartResponseDTO = new DashboardChartResponseDTO();
        chartResponseDTO.setLabels(labels);
        chartResponseDTO.setDatas(datas);
        
        when(entityManager.createNativeQuery(nativeQuery.toString(), Object.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(os);
        
        DashboardChartResponseDTO dcrdto = dashboardRepositoryCriteriaImpl.findByDepartmentAllocationStatusIn(allocationStatusInt);
        
        Assertions.assertEquals(chartResponseDTO.getLabels().size(),dcrdto.getLabels().size());
    }
    


    @Test
    public void testFindByIdInSkillAllocationStatusIn() {
        // Mocking data
        List<Integer> skillIds = Arrays.asList(1, 2, 3);
        List<Integer> allocationStatusInt = Arrays.asList(4, 5, 6);

        List<Object[]> resultList = new LinkedList<>();
        resultList.add(new Object[]{"Benched", 10});
        resultList.add(new Object[]{"Internal", 20});
        resultList.add(new Object[]{"Billable", 2});

        // Mocking entityManager and query
        Query mockQuery = Mockito.mock(Query.class);
        Mockito.when(mockQuery.getResultList()).thenReturn(resultList);

        Mockito.when(entityManager.createNativeQuery(any(), eq(Object.class))).thenReturn(mockQuery);

        // Call the method to test
        DashboardChartResponseDTO result = dashboardRepositoryCriteriaImpl.findByIdInSkillAllocationStatusIn(skillIds, allocationStatusInt);

        // Assertions
        List<String> expectedLabels = Arrays.asList("Benched", "Internal", "Billable");
        List<Integer> expectedDatas = Arrays.asList(10, 20,2);

        assertEquals(expectedLabels, result.getLabels());
        assertEquals(expectedDatas, result.getDatas());
    }
}
