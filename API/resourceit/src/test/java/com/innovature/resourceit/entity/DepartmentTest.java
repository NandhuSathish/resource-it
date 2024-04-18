package com.innovature.resourceit.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import com.innovature.resourceit.entity.dto.requestdto.DepartmentRequestDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = DepartmentTest.class)
 class DepartmentTest {

    @Test
    void testNoArgsConstructor() {
        Department department = new Department();
        assertNull(department.getDepartmentId());
        assertNull(department.getName());
        assertNull(department.getDisplayOrder());
    }

    @Test
    void testAllArgsConstructor() {
        Department department = new Department(1, "IT", 1);
        assertEquals(1, department.getDepartmentId());
        assertEquals("IT", department.getName());
        assertEquals(1, department.getDisplayOrder());
    }

    @Test
    void testAllArgumentConstructorWithDTO() {
        DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setName("HR");
        departmentRequestDTO.setDisplayOrder(2);

        Department department = new Department(departmentRequestDTO);
        assertNull(department.getDepartmentId()); // DepartmentId is generated and should be null here.
        assertEquals("HR", department.getName());
        assertEquals(2, department.getDisplayOrder());
    }

    @Test
    void testSetterAndGetterForDepartmentId() {
        Department department = new Department();
        department.setDepartmentId(1);
        assertEquals(1, department.getDepartmentId());
    }

    @Test
    void testSetterAndGetterForName() {
        Department department = new Department();
        department.setName("Finance");
        assertEquals("Finance", department.getName());
    }

    @Test
    void testSetterAndGetterForDisplayOrder() {
        Department department = new Department();
        department.setDisplayOrder(3);
        assertEquals(3, department.getDisplayOrder());
    }
}
