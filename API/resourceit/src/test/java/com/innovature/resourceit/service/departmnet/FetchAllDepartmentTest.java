package com.innovature.resourceit.service.departmnet;

import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.repository.DepartmentRepository;
import com.innovature.resourceit.service.impli.DepartmentServiceImpli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.when;


@SpringBootTest
@ContextConfiguration(classes = FetchAllDepartmentTest.class)
public class FetchAllDepartmentTest {

    @InjectMocks
    private DepartmentServiceImpli departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    public void setUp() {
        // Reset Mockito invocations and behaviors before each test
        Mockito.reset(departmentRepository, messageSource);
    }

    @Test
    public void testFetchAllDepartment() {
        Department department1 = new Department(1, "JAVA", 11);
        Department department2 = new Department(1, "PYTHON", 1);
        Collection<Department> departments = new ArrayList<>();
        departments.add(department1);
        departments.add(department2);
        when(departmentRepository.findAllByOrderByDisplayOrder()).thenReturn(departments);
        Collection<Department> result= departmentService.fetchAll();
        assertEquals(departments.size(),result.size());
    }

    @Test
    public void testFetchByIdDepartment() {
        Department department1 = new Department(1, "JAVA", 11);
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department1));
        Department result= departmentService.getDepartmentById(1);
        assertEquals(department1,result);
    }
}
