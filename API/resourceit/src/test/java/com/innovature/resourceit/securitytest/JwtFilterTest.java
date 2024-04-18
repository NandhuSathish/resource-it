package com.innovature.resourceit.securitytest;


import com.innovature.resourceit.security.JwtFilter;
import com.innovature.resourceit.security.JwtUserDetailsService;
import com.innovature.resourceit.security.TokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    TokenManager tokenManager;

    @Mock
    MessageSource messageSource;

    @Mock
    private JwtUserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    JwtFilter jwtFilter;
    
    private String username ;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        username = "sampleUser@mail.com";
        userDetails = new User(username, "", Arrays.asList( new SimpleGrantedAuthority("ADMIN")));
        jwtFilter = new JwtFilter(userDetailsService, tokenManager);
    }


    @Test
    void testDoFilterInternalValidToken() throws ServletException, IOException {
        String validToken = "validToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(tokenManager.getUsernameFromToken(validToken)).thenReturn(username);


        SecurityContext securityContext = mock(SecurityContext.class);
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(tokenManager.validateJwtToken(validToken, userDetails)).thenReturn(Boolean.TRUE);
        SecurityContextHolder.setContext(securityContext);

        jwtFilter.doFilterInternal(request, response, filterChain);


        when(request.getHeader("Authorization")).thenReturn(null);
        jwtFilter.doFilterInternal(request, response, filterChain);
        when(request.getHeader("Authorization")).thenReturn(validToken);
        jwtFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, times(3)).doFilter(request, response);
        verify(securityContext, times(1)).getAuthentication(); // Called once after setting the authentication
    }

    @Test
    void testDoFilterInternalWithIllegalArgumentException() throws ServletException, IOException {
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(tokenManager.getUsernameFromToken("invalidToken")).thenThrow(new IllegalArgumentException("Invalid token"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(400);
        verify(printWriter).print("{\"errorCode\":\"1013\",\"errorMessage\":\"Unable to get JWT Token\"}");
        verify(printWriter).flush();
    }

    @Test
    void testDoFilterInternalWithExpiredJwtException() throws ServletException, IOException {
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(tokenManager.getUsernameFromToken("invalidToken")).thenThrow(ExpiredJwtException.class);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(401);
        verify(printWriter).print("{\"errorCode\":\"1002\",\"errorMessage\":\"Token has expired\"}");
        verify(printWriter).flush();
    }

    @Test
    void testDoFilterInternalWithAnyException() throws ServletException, IOException {
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(tokenManager.getUsernameFromToken("invalidToken")).thenThrow(new RuntimeException());

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(401);
        verify(printWriter).print("{\"errorCode\":\"1001\",\"errorMessage\":\"Invalid Token\"}");
        verify(printWriter).flush();
    }


}
