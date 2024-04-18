package com.innovature.resourceit.service.departmnet;


import com.innovature.resourceit.entity.Department;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.DepartmentRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.impli.DepartmentServiceImpli;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = DeleteDepartmentTest.class)
 class DeleteDepartmentTest {

    @InjectMocks
    private DepartmentServiceImpli departmentServiceImpl;


    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private MessageSource messageSource;

    @Test
     void testDeleteDepartment() {
        // Mock the department repository to return a department with the given deptId
        Department existingDepartment = new Department();
        existingDepartment.setDepartmentId(1); // Assuming deptId is 1 for this test

        when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));
        when(resourceRepository.findByDepartmentDepartmentId(1)).thenReturn(Optional.empty());

        // Mock the message source response for department not found
        when(messageSource.getMessage("DEPARTMENT_NOT_FOUND", null, Locale.ENGLISH)).thenReturn("1133-Department not found");

        // Call the deleteDepartment method
        departmentServiceImpl.deleteDepartment(1);

        // Verify that the departmentRepository deleteById method is called once
        Mockito.verify(departmentRepository, Mockito.times(1)).deleteById(1);
    }

   @Test
   void testDeleteDepartmentThrowError() {
      // Mock the department repository to return a department with the given deptId
      when(departmentRepository.findById(1)).thenReturn(Optional.empty());

      // Call the deleteDepartment method and verify that a BadRequestException is thrown
      assertThrows(BadRequestException.class, () -> {
         departmentServiceImpl.deleteDepartment(1);
      });

      // Verify that the departmentRepository deleteById method is not called since the department is not found
      verify(departmentRepository, never()).deleteById(anyInt());
      // Verify that the departmentRepository deleteById method is called once
   }

   @Test
   void testDeleteDepartmentThrowErrorWhenCannotDelete() {
      // Mock the department repository to return a department with the given deptId
      Department existingDepartment = new Department();
      existingDepartment.setDepartmentId(1); // Assuming deptId is 1 for this test

      when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));
      when(resourceRepository.findByDepartmentDepartmentId(1)).thenReturn(Optional.of(new Resource()));

      // Mock the message source response for DEPARTMENT_CANNOT_DELETE
      when(messageSource.getMessage(eq("DEPARTMENT_CANNOT_DELETE"), isNull(), eq(Locale.ENGLISH)))
              .thenReturn("Department cannot be deleted message");

      // Call the deleteDepartment method and verify that a BadRequestException is thrown
      BadRequestException exception = assertThrows(BadRequestException.class, () -> {
         departmentServiceImpl.deleteDepartment(1);
      });

      // Verify that the exception message is as expected
       assertEquals(HttpStatus.BAD_REQUEST.value(), extractStatusCode(exception.getMessage()));

      // Verify that the departmentRepository deleteById method is not called since the department cannot be deleted
      verify(departmentRepository, never()).deleteById(anyInt());
   }

    private int extractStatusCode(String exceptionMessage) {
        // Assuming exceptionMessage is in the format "400 BAD_REQUEST"
        String[] parts = exceptionMessage.split("\\s");
        return Integer.parseInt(parts[0]);
    }


}
