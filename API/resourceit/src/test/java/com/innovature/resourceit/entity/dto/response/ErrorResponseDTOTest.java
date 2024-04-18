/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.responsedto.ErrorResponseDTO;
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
@ContextConfiguration(classes = ErrorResponseDTOTest.class)
class ErrorResponseDTOTest {

    private String errorCode;
    private String errorMessage;

    @BeforeEach
    void setUp() {
        errorCode = "1001";
        errorMessage = "Error Message";

    }

    @Test
    void testErrorResponseDTOEntity() {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setErrorCode(errorCode);
        errorResponseDTO.setErrorMessage(errorMessage);

        assertEquals(errorCode, errorResponseDTO.getErrorCode());
        assertEquals(errorMessage, errorResponseDTO.getErrorMessage());
    }

    @Test
    void testConstructorWithAllArgument() {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(errorMessage,errorCode);
        assertEquals(errorCode, errorResponseDTO.getErrorCode());
        assertEquals(errorMessage, errorResponseDTO.getErrorMessage());
    }

}
