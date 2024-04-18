/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.allocation;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = ApprovalFlowTest.class)
public class ApprovalFlowTest {

    @InjectMocks
    private AllocationServiceImpli allocationServiceImpli;

    @Test
    void testAllocationFlowNullIsRequestListTrueAndHOD() {

        boolean isRequestList = true;

        Role r = new Role();
        r.setId(1);
        r.setName("HOD");

        Resource re = new Resource(1);
        re.setRole(r);

        List<Integer> lists = allocationServiceImpli.allocationFlowNull(isRequestList, re);
        Assertions.assertEquals(3, lists.size());
    }

    @Test
    void testAllocationFlowNullIsRequestListTrueAndHR() {

        boolean isRequestList = true;

        Role r = new Role();
        r.setId(1);
        r.setName("HR");

        Resource re = new Resource(1);
        re.setRole(r);

        List<Integer> lists = allocationServiceImpli.allocationFlowNull(isRequestList, re);
        Assertions.assertEquals(3, lists.size());
    }

    @Test
    void testAllocationFlowNullIsRequestListTrueAndPROJECTMANAGER() {

        boolean isRequestList = true;

        Role r = new Role();
        r.setId(1);
        r.setName("PROJECT MANAGER");

        Resource re = new Resource(1);
        re.setRole(r);

        List<Integer> lists = allocationServiceImpli.allocationFlowNull(isRequestList, re);
        Assertions.assertEquals(4, lists.size());
    }
    @Test
    void testAllocationFlowNullIsRequestListFalseAndHOD() {

        boolean isRequestList = false;

        Role r = new Role();
        r.setId(1);
        r.setName("HOD");

        Resource re = new Resource(1);
        re.setRole(r);

        List<Integer> lists = allocationServiceImpli.allocationFlowNull(isRequestList, re);
        Assertions.assertEquals(4, lists.size());
    }
    
    @Test
    void testAllocationFlowNullIsRequestListFalseAndHR() {

        boolean isRequestList = false;

        Role r = new Role();
        r.setId(1);
        r.setName("HR");

        Resource re = new Resource(1);
        re.setRole(r);

        List<Integer> lists = allocationServiceImpli.allocationFlowNull(isRequestList, re);
        Assertions.assertEquals(3, lists.size());
    }
}
