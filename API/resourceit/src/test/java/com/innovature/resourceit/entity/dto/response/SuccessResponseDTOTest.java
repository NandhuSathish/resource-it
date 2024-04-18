/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
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
@ContextConfiguration(classes = SuccessResponseDTOTest.class)
public class SuccessResponseDTOTest {
    private SuccessResponseDTO successResponseDTO;

    @BeforeEach
    void setUp() {
        successResponseDTO = new SuccessResponseDTO();
    }

    @Test
    void testConstructorWithSuccessCodeAndSuccessMessage() {
        SuccessResponseDTO response = new SuccessResponseDTO("200", "Success");
        assertEquals("200", response.getSuccessCode());
        assertEquals("Success", response.getSuccessMessage());
    }

    @Test
    void testGettersAndSetters() {
        successResponseDTO.setSuccessCode("201");
        assertEquals("201", successResponseDTO.getSuccessCode());

        successResponseDTO.setSuccessMessage("Another Success");
        assertEquals("Another Success", successResponseDTO.getSuccessMessage());
    }
}
