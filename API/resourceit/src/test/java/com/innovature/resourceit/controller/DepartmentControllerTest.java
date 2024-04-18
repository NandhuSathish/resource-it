package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.dto.requestdto.DepartmentRequestDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DepartmentControllerTest {

    @InjectMocks
    private DepartmentController departmentController;

    @Mock
    private MessageSource messageSource;

    @Mock
    private DepartmentService departmentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddValidDepartment() {
        // Create a valid DepartmentRequestDTO for testing
        DepartmentRequestDTO validDepartment = new DepartmentRequestDTO();
        validDepartment.setName("Test Department");
        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(validDepartment, "validDepartment");
        when(messageSource.getMessage("DEPARTMENT_ADDED", null, Locale.ENGLISH)).thenReturn("Department added successfully-12345");
        ResponseEntity<Object> responseEntity = departmentController.add(validDepartment);
        verify(departmentService).add(validDepartment);
        assertEquals( HttpStatus.CREATED,responseEntity.getStatusCode());

    }


    @Test
    void testUpdateValidDepartment() {
        DepartmentRequestDTO validDepartment = new DepartmentRequestDTO();
        validDepartment.setName("Updated Department");
        validDepartment.setDisplayOrder(10);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(messageSource.getMessage("DEPARTMENT_UPDATED", null, Locale.ENGLISH)).thenReturn("1011-Department updated");
        ResponseEntity<Object> responseEntity = departmentController.update(1, validDepartment);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void testFetchAll() {
        // Create a collection of Department objects for testing
        Department dept1 = new Department(1, "JAVA", 11);
        Department dept2 = new Department(1, "PYTHON", 11);
        Collection<Department> departments = new ArrayList<>();
        departments.add(dept1);
        departments.add(dept2);
        when(departmentService.fetchAll()).thenReturn(departments);
        when(messageSource.getMessage("DEPARTMENT_FETCHING_FAILED", null, Locale.ENGLISH))
                .thenReturn("Department fetching failed");
        ResponseEntity<Object> responseEntity = departmentController.fetchAll();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(departments, responseEntity.getBody());
    }

    @Test
    void testFetchAllWithException() {
       doThrow(new BadRequestException()).when(departmentService).fetchAll();
       assertThrows(BadRequestException.class,()->{
           departmentController.fetchAll();
       });
    }

    @Test
    void testGetDepartmentById() {
        Department dept1 = new Department(1, "JAVA", 11);
        when(departmentService.getDepartmentById(1)).thenReturn(dept1);
        ResponseEntity<Object> responseEntity = departmentController.getDepartmentById(1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(dept1, responseEntity.getBody());
    }


    @Test
    void testDeleteDepartment() {
        when(messageSource.getMessage("DEPARTMENT_DELETED", null, Locale.ENGLISH)).thenReturn("1120-Department deleted successfully");
        ResponseEntity<Object> responseEntity = departmentController.deleteDepartment(1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
