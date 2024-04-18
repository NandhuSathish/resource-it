/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.dto.responsedto.RoleResponseDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = RoleResponseDTOTest.class)
public class RoleResponseDTOTest {

    private Integer id;

    private String role;

    @BeforeEach
    void setUp() {
        id = 1;
        role = "ADMIN";

    }

    @Test
    void testRoleResponseDTOEntity() {

        RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
        roleResponseDTO.setId(id);
        roleResponseDTO.setRole(role);

        assertEquals(id, roleResponseDTO.getId());
        assertEquals(role, roleResponseDTO.getRole());
    }

    @Test
    void testConstructorWithAllArgument() {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(id, role);
        assertEquals(id, roleResponseDTO.getId());
        assertEquals(role, roleResponseDTO.getRole());
    }

    @Test
    void testConstructorWithRoleArgument() {
        Role r = new Role(role);
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(r);
        assertEquals(role, roleResponseDTO.getRole());
    }

}
