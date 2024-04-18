/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.request;

import com.innovature.resourceit.entity.dto.requestdto.JwtRequestDTO;
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
@ContextConfiguration(classes = JwtRequestDTOTest.class)
class JwtRequestDTOTest {

    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        email = "test@gmail.com";
        password = "Test@123";

    }

    @Test
    void testJwtRequestEntity() {

        JwtRequestDTO jwtRequestDTO = new JwtRequestDTO();
        jwtRequestDTO.setEmail(email);
        jwtRequestDTO.setPassword(password);

        assertEquals(email, jwtRequestDTO.getEmail());
        assertEquals(password, jwtRequestDTO.getPassword());
    }

    @Test
    void testConstructorWithAllArgument() {
        JwtRequestDTO jwtRequestDTO = new JwtRequestDTO(email, password);
        assertEquals(email, jwtRequestDTO.getEmail());
        assertEquals(password, jwtRequestDTO.getPassword());
    }

}
