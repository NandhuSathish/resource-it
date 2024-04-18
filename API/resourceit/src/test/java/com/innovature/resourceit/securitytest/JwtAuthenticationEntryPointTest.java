package com.innovature.resourceit.securitytest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovature.resourceit.entity.dto.responsedto.ErrorResponseDTO;
import com.innovature.resourceit.security.JwtAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.PrintWriter;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.StringWriter;
import java.util.Locale;

import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.context.MessageSource;

class JwtAuthenticationEntryPointTest {

    @InjectMocks
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Mock
    MessageSource messageSource;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        authenticationEntryPoint = new JwtAuthenticationEntryPoint(messageSource);
    }

    @Test
    void testCommence() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = mock(AuthenticationException.class);

        when(messageSource.getMessage("UNAUTHORISED_URL", null, Locale.ENGLISH)).thenReturn("1003-Unauthorized url.");

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(out);

        authenticationEntryPoint.commence(request, response, authException);

        String responseContent = stringWriter.toString();
        ErrorResponseDTO errorResponse = new ObjectMapper().readValue(responseContent, ErrorResponseDTO.class);
        assertNotNull(errorResponse);
        assertEquals("Unauthorized url.", errorResponse.getErrorMessage());
        assertEquals("1003", errorResponse.getErrorCode());
    }
}