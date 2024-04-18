package com.innovature.resourceit.exception;

import com.innovature.resourceit.exceptionhandler.UserAuthenticationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationExceptionTest {

    @Test
    void testConstructorWithoutMessage() {
        UserAuthenticationException exception = new UserAuthenticationException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Custom message for the exception";
        UserAuthenticationException exception = new UserAuthenticationException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Custom message for the exception";
        AuthenticationException cause = new AuthenticationException("Authentication failed") {};
        UserAuthenticationException exception = new UserAuthenticationException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}

