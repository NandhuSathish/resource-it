/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller.rolecontroller;

import com.innovature.resourceit.controller.RoleController;
import com.innovature.resourceit.entity.dto.responsedto.RoleResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.service.RoleService;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
@ContextConfiguration(classes = GetRoleControllerTest.class)
class GetRoleControllerTest {

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
    void getRoleValid() {
        RoleResponseDTO role1 = new RoleResponseDTO(1, "ADMIN");
        RoleResponseDTO role2 = new RoleResponseDTO(2, "USER");
        List<RoleResponseDTO> roles = Arrays.asList(role1, role2);
        when(roleService.getRoles()).thenReturn(roles);

        // Call the method
        ResponseEntity<Object> result = roleController.getRoles();

        // Verify that the service method is called
        verify(roleService, times(1)).getRoles();

        // Assert the result
        assertNotNull(result);
        Collection<RoleResponseDTO> rrdtos = (Collection<RoleResponseDTO>) result.getBody();
        assertEquals(2, rrdtos.size());
    }

    @Test
    void getRoleInValid() {
        Mockito.doThrow(new BadRequestException()).when(roleService).getRoles();
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            roleController.getRoles();
        });
        Assertions.assertNotNull(exception);
    }

    @Test
     void testGetRoleById(){
        when(roleService.getRoleById(1)).thenReturn(new RoleResponseDTO());
        ResponseEntity<Object> responseEntity=roleController.getRoleById(1);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    }
}
