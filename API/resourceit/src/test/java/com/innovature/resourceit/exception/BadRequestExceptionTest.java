package com.innovature.resourceit.exception;

import static org.junit.jupiter.api.Assertions.*;

import com.innovature.resourceit.exceptionhandler.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class BadRequestExceptionTest {

    @Test
    void testConstructorWithoutReason() {
        BadRequestException exception = new BadRequestException();
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertNull(exception.getReason());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithReason() {
        String reason = "Custom reason for the exception";
        BadRequestException exception = new BadRequestException(reason);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithReasonAndCause() {
        String reason = "Custom reason for the exception";
        Throwable cause = new RuntimeException("Root cause");
        BadRequestException exception = new BadRequestException(reason, cause);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(reason, exception.getReason());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testResponseStatusException() {
        BadRequestException exception = new BadRequestException("Custom reason");
        assertTrue(exception instanceof ResponseStatusException);
    }
}
