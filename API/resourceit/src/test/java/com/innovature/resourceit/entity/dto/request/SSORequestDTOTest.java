/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.request;

import com.innovature.resourceit.entity.dto.requestdto.SSORequestDTO;
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
@ContextConfiguration(classes = SSORequestDTOTest.class)
public class SSORequestDTOTest {

    private String token;

    @BeforeEach
    void setUp() {
        token = "hsgdisbsnisas-sdnshdjsdjsnidjsgbafsadkdskdsm";

    }

    @Test
    void testSSORequestDTOEntity() {

        SSORequestDTO sSORequestDTO = new SSORequestDTO();
        sSORequestDTO.setToken(token);

        assertEquals(token, sSORequestDTO.getToken());
    }

    @Test
    void testConstructorWithAllArgument() {
        SSORequestDTO sSORequestDTO = new SSORequestDTO(token);
        assertEquals(token, sSORequestDTO.getToken());
    }
}
