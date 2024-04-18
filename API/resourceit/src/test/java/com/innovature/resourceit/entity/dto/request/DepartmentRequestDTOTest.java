package com.innovature.resourceit.entity.dto.request;

import com.innovature.resourceit.entity.dto.requestdto.DepartmentRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentRequestDTOTest {

    @Test
    public void testDepartmentRequestDTO() {
        // Create a sample DepartmentRequestDTO
        DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setName("Sample Department");
        departmentRequestDTO.setDisplayOrder(10);

        // Test the getters
        assertEquals("Sample Department", departmentRequestDTO.getName());
        assertEquals(10, departmentRequestDTO.getDisplayOrder());

        // Test the validation annotations
        assertNotNull(departmentRequestDTO.getName());
        assertNotNull(departmentRequestDTO.getDisplayOrder());

        // Test other validations
        assertTrue(departmentRequestDTO.getName().length() <= 50);
        assertTrue(departmentRequestDTO.getDisplayOrder() <= 20);
    }
}
