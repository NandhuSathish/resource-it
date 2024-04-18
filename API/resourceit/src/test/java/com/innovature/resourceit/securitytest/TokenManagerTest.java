package com.innovature.resourceit.securitytest;

import com.innovature.resourceit.entity.dto.responsedto.JwtResponseDTO;
import com.innovature.resourceit.security.TokenManager;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenManagerTest {

    @InjectMocks
    private TokenManager tokenManager;

    @Mock
    private UserDetails userDetails;

    String sampleUsername = "testUser";

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
        tokenManager.setAccessTokenSecretKey("ACCESS_KEY");
        tokenManager.setJwtExpirationInMs(10000);
        tokenManager.setRefreshExpirationDateInMs(25000);
        tokenManager.setRefreshTokenSecretKey("REFRESH_KEY");

    }

    @Test
    public void testGenerateJwtToken() {

        when(userDetails.getUsername()).thenReturn(sampleUsername);

        JwtResponseDTO jwtResponse = tokenManager.generateJwtToken(userDetails);

        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.getAccessToken());
        assertNotNull(jwtResponse.getRefreshToken());
    }

    @Test
    void testValidateJwtToken_ValidToken() {
        when(userDetails.getUsername()).thenReturn(sampleUsername);
        String token = generateValidToken(sampleUsername);
        Boolean isValid = tokenManager.validateJwtToken(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void testValidateJwtToken_UsernameMismatch() {
        when(userDetails.getUsername()).thenReturn("another_username");
        String token = generateValidToken(sampleUsername);
        Boolean isValid = tokenManager.validateJwtToken(token, userDetails);
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_ExpiredToken() {

        String expiredToken = generateExpiredToken(sampleUsername);
        Boolean isValid = tokenManager.validateJwtToken(expiredToken, userDetails);
        assertFalse(isValid);

    }

    @Test
    void testGetUsernameFromToken() {
        String token = generateValidToken(sampleUsername);
        String extractedUsername = tokenManager.getUsernameFromToken(token);
        assertEquals(sampleUsername, extractedUsername);
    }

    @Test
    void testGetUsernameFromRefreshToken() {
        String token = generateValidTokenRefresh(sampleUsername);
        String extractedUsername = tokenManager.getUsernameFromRefreshToken(token);
        assertEquals(sampleUsername, extractedUsername);
    }

    private String generateExpiredToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() + 1000))
                .signWith(SignatureAlgorithm.HS512, tokenManager.getAccessTokenSecretKey())
                .compact();
    }

    private String generateValidToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + tokenManager.getJwtExpirationInMs())).signWith(SignatureAlgorithm.HS512, tokenManager.getAccessTokenSecretKey()).compact();
    }

    private String generateValidTokenRefresh(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + tokenManager.getJwtExpirationInMs())).signWith(SignatureAlgorithm.HS512, tokenManager.getRefreshTokenSecretKey()).compact();
    }


}
