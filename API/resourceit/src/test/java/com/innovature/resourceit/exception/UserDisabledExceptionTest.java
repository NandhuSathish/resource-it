package com.innovature.resourceit.exception;

import com.innovature.resourceit.exceptionhandler.UserDisabledException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class UserDisabledExceptionTest {

    @Test
    void testConstructorWithoutMessage() {
        UserDisabledException exception = new UserDisabledException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Custom message for the exception";
        UserDisabledException exception = new UserDisabledException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Custom message for the exception";
        DisabledException disabledexception = new DisabledException(message);
        UserDisabledException exception = new UserDisabledException(message, disabledexception);
        assertEquals(message, exception.getMessage());
        assertEquals(disabledexception, exception.getCause());
    }
}

