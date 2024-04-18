package com.innovature.resourceit.exceptionhandler;

import com.innovature.resourceit.entity.dto.responsedto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.springframework.http.converter.HttpMessageNotReadableException;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler(messageSource);
    }

    @Test
    void handleValidationException_MethodArgumentNotValidException() {
        // Arrange
        MethodArgumentNotValidException exception = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        List<FieldError> fieldErrors = Collections.singletonList(new FieldError("objectName", "fieldName", "Error Message"));

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(messageSource.getMessage("Error Message", null, Locale.ENGLISH)).thenReturn("CODE - Error Message");

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("CODE ", responseEntity.getBody().getErrorCode());
        assertEquals("Error Message", responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleValidationException_ConstraintViolationException() {
        // Arrange
        ConstraintViolationException exception = Mockito.mock(ConstraintViolationException.class);

        when(exception.getConstraintViolations()).thenReturn(Collections.singleton(new MockedConstraintViolation("Error Message")));
        when(messageSource.getMessage("Error Message", null, Locale.ENGLISH)).thenReturn("CODE - Error Message");

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("CODE ", responseEntity.getBody().getErrorCode());
        assertEquals("Error Message", responseEntity.getBody().getErrorMessage());
    }

    @Test
    void handleValidationException_InternalServerError() {
        // Arrange
        Exception exception = Mockito.mock(Exception.class);

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody()); // You might want to improve this assertion based on your actual implementation
    }

    @Test
    void handleTypeMismatchException() {
        // Arrange
        MethodArgumentTypeMismatchException exception = Mockito.mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("fieldName");

        when(messageSource.getMessage("ARGUMENT_TYPE_MISMATCH", null, Locale.ENGLISH))
                .thenReturn("CODE - Argument type mismatch");

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleTypeMismatchException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("CODE ", responseEntity.getBody().getErrorCode());
        assertEquals("Argument type mismatch fieldName", responseEntity.getBody().getErrorMessage());
    }

    // Additional tests can be added to cover more scenarios

    // Utility class to mock ConstraintViolation
    private static class MockedConstraintViolation implements ConstraintViolation {
        private final String message;

        public MockedConstraintViolation(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public Object getRootBean() {
            return null;
        }

        @Override
        public Class getRootBeanClass() {
            return null;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Path getPropertyPath() {
            return null;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public Object unwrap(Class aClass) {
            return null;
        }
    }
    
    @Test
    void testHandleJsonParseException() {
        // Given
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Test exception");

        // When
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleJsonParseException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponseDTO errorResponse = responseEntity.getBody();
        assertEquals("Invalid Request body", errorResponse.getErrorMessage());
        assertEquals("1531", errorResponse.getErrorCode());
    }
}
