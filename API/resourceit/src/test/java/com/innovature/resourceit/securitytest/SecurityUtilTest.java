package com.innovature.resourceit.securitytest;

import com.innovature.resourceit.security.SecurityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecurityUtilTest {

    @AfterEach
    void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

//    @Test
//    void getCurrentUserEmail_WithAuthenticatedUser_ReturnsEmail() {
//        // Mock authentication
//        String userEmail = "user@example.com";
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail, "password");
//
//        // Set the authentication in SecurityContextHolder
//        SecurityContextHolder.getContext().setAuthentication(createAuthenticatedAuthentication(authentication));
//
//        // Test
//        String result = SecurityUtil.getCurrentUserEmail();
//
//        // Verify
//        assertEquals(userEmail, result);
//    }

    @Test
    void getCurrentUserEmail_WithUnauthenticatedUser_ReturnsNull() {
        // Ensure SecurityContextHolder has no authentication
        SecurityContextHolder.clearContext();

        // Test
        String result = SecurityUtil.getCurrentUserEmail();

        // Verify
        assertEquals(null, result);
    }

    private Authentication createAuthenticatedAuthentication(Authentication authentication) {
        return new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                authentication.getAuthorities()
        );
    }
}
