/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.role;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = UpdateRoleTest.class)
class DeleteRoleTest {

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
    void deleteRole() {
        int roleId = 1;
        Role role = new Role("ADMIN");
        role.setId(1);
        Optional<Resource> resource = Optional.of(new Resource());

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(resourceRepository.findByRoleId(roleId)).thenReturn(Optional.empty());

        roleServiceImpli.deleteRole(roleId);

        verify(roleRepository, times(1)).deleteById(roleId);
    }

    @Test
    void deleteRoleInvalidRole() {
        int roleId = 1;
        Role role = new Role("ADMIN");
        role.setId(1);
        Optional<Resource> resource = Optional.of(new Resource());

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        when(resourceRepository.findByRoleId(roleId)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> roleServiceImpli.deleteRole(roleId));

    }

    @Test
    void deleteRoleInValidIsResourcePresent() {
        int roleId = 1;
        Role role = new Role("ADMIN");
        role.setId(1);
        Optional<Resource> resource = Optional.of(new Resource());

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(resourceRepository.findByRoleId(roleId)).thenReturn(resource);
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            roleServiceImpli.deleteRole(roleId);
        });
        Assertions.assertNotNull(exception);

    }
}
