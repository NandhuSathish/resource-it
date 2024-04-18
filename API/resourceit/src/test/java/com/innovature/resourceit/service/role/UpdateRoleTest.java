/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.role;

import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.dto.requestdto.RoleRequestDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.RoleRepository;
import com.innovature.resourceit.service.impli.RoleServiceImpli;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = UpdateRoleTest.class)
public class UpdateRoleTest {

    @Mock
    RoleRepository roleRepository;
    
    @Mock
    ResourceRepository resourceRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    RoleServiceImpli roleServiceImpli;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleServiceImpli = new RoleServiceImpli(roleRepository, resourceRepository, messageSource);
    }

    @Test
    public void updateRole() {
        Role role = new Role("ADMIN");
        role.setId(1);
        RoleRequestDTO roleRequestDTO = new RoleRequestDTO();
        roleRequestDTO.setRole("USER");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        roleServiceImpli.updateRole(1, roleRequestDTO);

        verify(roleRepository, times(1)).save(role);
    }

    @Test
    public void updateRoleIsRoleExist() {
        Role role = new Role("ADMIN");
        role.setId(1);
        RoleRequestDTO roleRequestDTO = new RoleRequestDTO();
        roleRequestDTO.setRole("USER");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role("USER")));
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            roleServiceImpli.updateRole(1, roleRequestDTO);
        });
        Assertions.assertNotNull(exception);
        

    }
}
