/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.response;

import com.innovature.resourceit.entity.dto.responsedto.JwtResponseDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = JwtResponseDTOTest.class)
class JwtResponseDTOTest {

    private String email;
    private String accessToken;
    private String refreshToken;
    private long expiredTime;
    private String userName;
    private Integer role;
    private String pictureUrl;


    @BeforeEach
    void setUp() {
        email = "test@gmail.com";
        accessToken = "Wehbdwjhnk-idufsdk";
        refreshToken = "PKOJbhsfdsvb-rtyb343";
        expiredTime = 626327637L;
        userName = "testName";
        role = 1;
        pictureUrl = "testPictureUrl";

    }

    @Test
    void testJwtResponseDTOEntity() {

        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setEmail(email);
        jwtResponseDTO.setAccessToken(accessToken);
        jwtResponseDTO.setRefreshToken(refreshToken);
        jwtResponseDTO.setExpiredTime(expiredTime);
        jwtResponseDTO.setUserName(userName);
        jwtResponseDTO.setRole(role);
        jwtResponseDTO.setPictureUrl(pictureUrl);

        assertEquals(email, jwtResponseDTO.getEmail());
        assertEquals(accessToken, jwtResponseDTO.getAccessToken());
        assertEquals(refreshToken, jwtResponseDTO.getRefreshToken());
        assertEquals(expiredTime, jwtResponseDTO.getExpiredTime());
        assertEquals(userName, jwtResponseDTO.getUserName());
        assertEquals(role, jwtResponseDTO.getRole());
        assertEquals(pictureUrl, jwtResponseDTO.getPictureUrl());

    }

    @Test
    void testConstructorWithSomeArgument() {
        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO(accessToken, refreshToken);
        assertEquals(accessToken, jwtResponseDTO.getAccessToken());
        assertEquals(refreshToken, jwtResponseDTO.getRefreshToken());
    }

//    @Test
//    void testConstructorWithAllArgument() {
//        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO(email, accessToken, refreshToken);
//        assertEquals(email, jwtResponseDTO.getEmail());
//        assertEquals(accessToken, jwtResponseDTO.getAccessToken());
//        assertEquals(refreshToken, jwtResponseDTO.getRefreshToken());
//    }
}
