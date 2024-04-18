/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller.rolecontroller;

import com.innovature.resourceit.controller.RoleController;
import com.innovature.resourceit.service.RoleService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BindingResult;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = DeleteRoleControllerTest.class)
 class DeleteRoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    @Mock
    private MessageSource messageSource;

    BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        roleController = new RoleController(roleService, messageSource);
    }

    @Test
     void deleteRoleValid() {
        int roleId = 1;
        String msg = "1102-Role added successfully";
//        when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(messageSource.getMessage(eq("ROLE_DELETED"), eq(null), any()))
                .thenReturn(msg);
        ResponseEntity<Object> responseEntity = roleController.deleteRole(roleId);

        // Verify that the service method is called
        verify(roleService, times(1)).deleteRole(roleId);

        // Perform assertions on the responseEntity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
}
