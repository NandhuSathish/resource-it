/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovature.resourceit.entity.dto.responsedto.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author abdul.fahad
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private TokenManager tokenManager;

    private static final String CONTENT_TYPE = "application/json";

    public JwtFilter(JwtUserDetailsService userDetailsService, TokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.tokenManager = tokenManager;
    }


    @Override
    public void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
            try {
                username = tokenManager.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                handleErrorResponse(response, "1013", "Unable to get JWT Token", 400);
                return;
            } catch (ExpiredJwtException e) {
                handleErrorResponse(response, "1002", "Token has expired", 401);
                return;
            } catch (Exception e) {
                handleErrorResponse(response, "1001", "Invalid Token", 401);
                return;
            }
        } 

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Boolean isJwtTokenValid = tokenManager.validateJwtToken(token, userDetails);
            if (Boolean.TRUE.equals(isJwtTokenValid)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), "",
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleErrorResponse(HttpServletResponse response, String errorCode, String errorMessage, int httpStatus) throws IOException {
        LOGGER.error(errorMessage);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorMessage(errorMessage);
        response.setStatus(httpStatus);
        response.setContentType(CONTENT_TYPE);
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.print(mapper.writeValueAsString(errorResponse));
        out.flush();
    }
}
