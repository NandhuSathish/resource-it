/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovature.resourceit.entity.dto.responsedto.ErrorResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 *
 * @author abdul.fahad
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint,
        Serializable {

    @Autowired
    private transient MessageSource messageSource;

    public JwtAuthenticationEntryPoint(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    @Override
    public void commence(HttpServletRequest request,HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        
        String[] list = messageSource.getMessage("UNAUTHORISED_URL", null, Locale.ENGLISH).split("-");
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(list[1], list[0]);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        new ObjectMapper().writeValue(out, errorResponse);
        out.flush();
    }
}
