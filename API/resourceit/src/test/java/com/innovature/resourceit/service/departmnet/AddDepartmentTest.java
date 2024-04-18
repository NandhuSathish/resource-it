package com.innovature.resourceit.service.departmnet;

import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.dto.requestdto.DepartmentRequestDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
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

import java.util.Locale;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = AddDepartmentTest.class)
 class AddDepartmentTest {


    @InjectMocks
    private DepartmentServiceImpli departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    public void setUp() {
        Mockito.reset(departmentRepository, messageSource);
    }

    @Test
     void testAddDepartment() {
        DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setName("Test Department");
        departmentRequestDTO.setDisplayOrder(10);

        when(departmentRepository.findByName("TEST DEPARTMENT")).thenReturn(Optional.empty());
        when(departmentRepository.findByDisplayOrder(10)).thenReturn(Optional.empty());
        when(messageSource.getMessage("DEPARTMENT_ALREADY_EXISTS", null, Locale.ENGLISH)).thenReturn("Department already exists");
        when(messageSource.getMessage("DISPLAY_ORDER_ALREADY_EXISTS", null, Locale.ENGLISH)).thenReturn("Display order already exists");

        departmentService.add(departmentRequestDTO);

        // Verify that the departmentRepository save method is called once
        Mockito.verify(departmentRepository, Mockito.times(1)).save(Mockito.any(Department.class));
    }
     
     @Test
     void testAddDepartmentInValidDepartmentPresent() {
        DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setName("Test Department");
        departmentRequestDTO.setDisplayOrder(10);
        
        Department d = new Department(departmentRequestDTO);
        
        when(messageSource.getMessage(eq("DEPARTMENT_ALREADY_EXISTS"), eq(null), any()))
                .thenReturn("1107-Department name already exists");
        
        when(departmentRepository.findByName("TEST DEPARTMENT")).thenReturn(Optional.of(d));
        
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            departmentService.add(departmentRequestDTO);
        });

        assertNotNull(exception);
        assertEquals("1107-Department name already exists", exception.getBody().getDetail());
     }

@Test
     void testAddDepartmentInValidDisplayOrderPresent() {
        DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setName("Test Department");
        departmentRequestDTO.setDisplayOrder(10);
        
        Department d = new Department();
        d.setName("Software");
        d.setDisplayOrder(10);
        
        when(messageSource.getMessage(eq("DISPLAY_ORDER_ALREADY_EXISTS"), eq(null), any()))
                .thenReturn("1108-Display order already given");
        
        when(departmentRepository.findByDisplayOrder(10)).thenReturn(Optional.of(d));
        
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            departmentService.add(departmentRequestDTO);
        });

        assertNotNull(exception);
        assertEquals("1108-Display order already given", exception.getBody().getDetail());
     }
}
