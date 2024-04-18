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
@ContextConfiguration(classes = UpdateDepartmentTest.class)
 class UpdateDepartmentTest {

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
     void testUpdateDepartment() {
        // Create a DepartmentRequestDTO for testing
        DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setName("Updated Department");
        departmentRequestDTO.setDisplayOrder(20);

        // Mock the department repository to return a department with the given deptId
        Department existingDepartment = new Department();
        existingDepartment.setDepartmentId(1); // Assuming deptId is 1 for this test
        existingDepartment.setName("Old Department");
        existingDepartment.setDisplayOrder(10);
        when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));

        // Mock departmentRepository to return an empty Optional when searching by name and display order
        when(departmentRepository.findByName("UPDATED DEPARTMENT")).thenReturn(Optional.empty());
        when(departmentRepository.findByDisplayOrder(20)).thenReturn(Optional.empty());

        // Mock the message source response for successful update and existing department
        when(messageSource.getMessage("DEPARTMENT_ALREADY_EXISTS", null, Locale.ENGLISH)).thenReturn("Department already exists");
        when(messageSource.getMessage("DISPLAY_ORDER_ALREADY_EXISTS", null, Locale.ENGLISH)).thenReturn("Display order already exists");

        // Call the update method
        departmentService.update(departmentRequestDTO, 1);

        // Verify that the departmentRepository save method is called once
        Mockito.verify(departmentRepository, Mockito.times(1)).save(Mockito.any(Department.class));
    }
     
      @Test
     void testUpdateDepartmentInValidDepartmentAlreadyExist() {
        DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setName("Department small");
        departmentRequestDTO.setDisplayOrder(20);
        
        int deptId= 1;

        Department existingDepartment = new Department();
        existingDepartment.setDepartmentId(1); 
        existingDepartment.setName("DEPARTMENT");
        existingDepartment.setDisplayOrder(10);
        
        Department existingDepartment1 = new Department();
        existingDepartment1.setDepartmentId(1); 
        existingDepartment1.setName("Department small");
        existingDepartment1.setDisplayOrder(10);
        
        when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.findByName("DEPARTMENT SMALL")).thenReturn(Optional.of(existingDepartment1));
        when(messageSource.getMessage(eq("DEPARTMENT_ALREADY_EXISTS"), eq(null), any()))
                .thenReturn("1107-Department name already exists ");
        
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            departmentService.update(departmentRequestDTO,deptId);
        });

        assertNotNull(exception);
        assertEquals("1107-Department name already exists ", exception.getBody().getDetail());
     }
     
      @Test
     void testUpdateDepartmentInValidDepartmentOrderExist() {
        DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
        departmentRequestDTO.setName("Department s");
        departmentRequestDTO.setDisplayOrder(20);
        
        int deptId= 1;

        Department existingDepartment = new Department();
        existingDepartment.setDepartmentId(1); 
        existingDepartment.setName("DEPARTMENT");
        existingDepartment.setDisplayOrder(10);
        
        Department existingDepartment1 = new Department();
        existingDepartment1.setDepartmentId(1); 
        existingDepartment1.setName("Department sm");
        existingDepartment1.setDisplayOrder(20);
        
        when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.findByName("DEPARTMENT S")).thenReturn(Optional.empty());
        when(departmentRepository.findByDisplayOrder(departmentRequestDTO.getDisplayOrder())).thenReturn(Optional.of(existingDepartment1));
        when(messageSource.getMessage(eq("DISPLAY_ORDER_ALREADY_EXISTS"), eq(null), any()))
                .thenReturn("1108-Display order already given");
        
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            departmentService.update(departmentRequestDTO,deptId);
        });

        assertNotNull(exception);
        assertEquals("1108-Display order already given", exception.getBody().getDetail());
     }
}
