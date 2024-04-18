/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller.rolecontroller;

import com.innovature.resourceit.controller.RoleController;
import com.innovature.resourceit.entity.dto.requestdto.RoleRequestDTO;
import com.innovature.resourceit.service.RoleService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
@ContextConfiguration(classes = UpdateRoleControllerTest.class)
class UpdateRoleControllerTest {

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
    void updateRoleValid() {
        int roleId = 1;
        RoleRequestDTO roleRequestDTO = new RoleRequestDTO();
        roleRequestDTO.setRole("ADMIN");
        String msg = "1102-Role added successfully";
        bindingResult = createBindingResultWithoutErrors();
        Mockito.when(messageSource.getMessage(eq("ROLE_UPDATED"), eq(null), any()))
                .thenReturn(msg);
        ResponseEntity<Object> responseEntity = roleController.updateRole(roleId, roleRequestDTO);

        // Verify that the service method is called
        verify(roleService, times(1)).updateRole(roleId, roleRequestDTO);

        // Perform assertions on the responseEntity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    private BindingResult createBindingResultWithoutErrors() {
        RoleRequestDTO validRoleDTO = new RoleRequestDTO();
        return new org.springframework.validation.BeanPropertyBindingResult(validRoleDTO, "roleDTO");
    }
}
