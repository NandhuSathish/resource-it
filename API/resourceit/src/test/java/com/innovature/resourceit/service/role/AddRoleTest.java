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
import org.mockito.Mockito;
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
@ContextConfiguration(classes = AddRoleTest.class)
class AddRoleTest {

    @Mock
    RoleRepository roleRepository;
    ;

    @Mock
    ResourceRepository resourceRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    RoleServiceImpli roleServiceImpli;

    RoleRequestDTO roleRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleServiceImpli = new RoleServiceImpli(roleRepository, resourceRepository, messageSource);
        roleRequestDTO = new RoleRequestDTO();
        roleRequestDTO.setRole("ADMIN");
    }

    @Test
    void addRoleTest() {
        Optional<Role> role = Optional.empty();
        when(roleRepository.findByName(roleRequestDTO.getRole().toUpperCase())).thenReturn(role);
        roleServiceImpli.addRole(roleRequestDTO);
        verify(roleRepository, times(1)).save(new Role(roleRequestDTO.getRole().toUpperCase()));
    }

    @Test
    void addRoleTestInvalid() {
        Optional<Role> role = Optional.of(new Role("ADMIN"));
        when(roleRepository.findByName(roleRequestDTO.getRole().toUpperCase())).thenReturn(role);
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            roleServiceImpli.addRole(roleRequestDTO);
        });
        Assertions.assertNotNull(exception);
    }
}
