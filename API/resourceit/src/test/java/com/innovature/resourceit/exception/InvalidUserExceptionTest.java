package com.innovature.resourceit.exception;

import com.innovature.resourceit.exceptionhandler.InvalidUserException;
import com.innovature.resourceit.exceptionhandler.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InvalidUserExceptionTest {

    @Test
    void testConstructorWithoutMessage() {
        InvalidUserException exception = new InvalidUserException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Custom message for the exception";
        InvalidUserException exception = new InvalidUserException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Custom message for the exception";
        BadCredentialsException cause = new BadCredentialsException("Bad credentials");
        InvalidUserException exception = new InvalidUserException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    void testDefaultContructorForUserNotFoundException(){
        UserNotFoundException userNot= new UserNotFoundException();
        if(userNot != null)
        assertTrue(true);
    }
}

