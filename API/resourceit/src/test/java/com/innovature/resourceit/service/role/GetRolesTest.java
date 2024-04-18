/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.role;

import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.dto.responsedto.RoleResponseDTO;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.RoleRepository;
import com.innovature.resourceit.service.impli.RoleServiceImpli;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = GetRolesTest.class)
public class GetRolesTest {

    @Mock
    RoleRepository roleRepository;
    ;

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
    public void getRoles() {
        Role role1 = new Role("ROLE_ADMIN");
        Role role2 = new Role("ROLE_USER");
        role1.setId(1);
        role2.setId(2);
        List<Role> roles = Arrays.asList(role1, role2);

        // Mock the behavior of the repository
        when(roleRepository.findAllByIdNotOrderById(anyInt())).thenReturn(roles);

        // Perform the test
        Collection<RoleResponseDTO> roleResponseDTOs = roleServiceImpli.getRoles();

        // Assert the results
        assertEquals(2, roleResponseDTOs.size());
    }
}
