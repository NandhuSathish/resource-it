/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.exception;

import com.innovature.resourceit.entity.dto.responsedto.ErrorResponseDTO;
import com.innovature.resourceit.exceptionhandler.InvalidUserException;
import com.innovature.resourceit.exceptionhandler.RestResponseEntityExceptionHandler;
import com.innovature.resourceit.exceptionhandler.UserDisabledException;
import com.innovature.resourceit.exceptionhandler.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author abdul.fahad
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RestResponseEntityExceptionHandlerTest.class)
class RestResponseEntityExceptionHandlerTest {

    @InjectMocks
    private RestResponseEntityExceptionHandler exceptionHandler;

    @Mock
    MessageSource messageSource;

    private ErrorResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new RestResponseEntityExceptionHandler(messageSource);
    }

    /**
     * Test: Method for creation of error response entity.
     */
    @DisplayName("Test for createErrorResponseEntity()")
    @Test
    void testCreateErrorResponseEntity() {
        //Custom Error Message
        when(messageSource.getMessage(anyString(), any(), anyString(), any())).thenReturn("123-SampleError");

        ResponseEntity<Object> responseEntity = exceptionHandler.createErrorResponseEntity("", HttpStatus.BAD_REQUEST);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        responseDTO = (ErrorResponseDTO) responseEntity.getBody();

        assertEquals("SampleError", responseDTO.getErrorMessage());
        assertEquals("123", responseDTO.getErrorCode());

        //Default error message
        reset(messageSource);
        responseEntity = exceptionHandler.createErrorResponseEntity("", HttpStatus.BAD_REQUEST);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        responseDTO = (ErrorResponseDTO) responseEntity.getBody();

        assertEquals("An error occurred", responseDTO.getErrorMessage());
        assertEquals("500", responseDTO.getErrorCode());

        //Default error message in case of invalid message
        reset(messageSource);
        when(messageSource.getMessage(anyString(), any(), anyString(), any())).thenReturn("invalid_message");

        responseEntity = exceptionHandler.createErrorResponseEntity("", HttpStatus.BAD_REQUEST);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        responseDTO = (ErrorResponseDTO) responseEntity.getBody();

        assertEquals("invalid_message", responseDTO.getErrorMessage());
        assertEquals("500", responseDTO.getErrorCode());
    }

    /**
     * Test: Method for handling request not supported exception.
     */
    @DisplayName("Test for handleHttpRequestMethodNotSupported()")
    @Test
    void testHandleHttpRequestMethodNotSupported() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("Unsupported method");
        ResponseEntity<Object> responseEntity = exceptionHandler.handleHttpRequestMethodNotSupported(ex, null, null);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling method argument not valid exception.
     */
//    @DisplayName("Test for handleMethodArgumentNotValid()")
//    @Test
//    void testHandleMethodArgumentNotValid() throws NoSuchMethodException {
//        MethodArgumentNotValidException ex = createMockMethodArgumentNotValidException();
//
//        HttpHeaders headers = new HttpHeaders();
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        WebRequest request = mock(WebRequest.class);
//
//        when(messageSource.getMessage(ArgumentMatchers.any(), any(), any())).thenReturn("123-sample_message");
//        ResponseEntity<Object> responseEntity = exceptionHandler.handleMethodArgumentNotValid(ex, headers, status, request);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//
//        Object responseBody = responseEntity.getBody();
//        assertEquals(ErrorResponseDTO.class, responseBody.getClass());
//        responseDTO = (ErrorResponseDTO) responseBody;
//
//        assertEquals("sample_message", responseDTO.getErrorMessage());
//        assertEquals("123", responseDTO.getErrorCode());
//    }

//    private MethodArgumentNotValidException createMockMethodArgumentNotValidException() throws NoSuchMethodException {
//        List<FieldError> fieldErrors = new ArrayList<>();
//        fieldErrors.add(new FieldError("objectName", "fieldName", "First error message"));
//        BindingResult bindingResult = mock(BindingResult.class);
//        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
//
//        Method method = UserDTO.class.getMethod("setUsername", String.class);
//        MethodParameter methodParameter = new MethodParameter(method, 0);
//        return new MethodArgumentNotValidException(methodParameter, bindingResult);
//    }

    /**
     * Test: Method for handling handleHttpMediaTypeNotSupported.
     */
    @DisplayName("Test for handleHttpMediaTypeNotSupported() exception")
    @Test
    void testHandleHttpMediaTypeNotSupported() {
        HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException("123-sample_message");
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest request = mock(WebRequest.class);
        ResponseEntity<Object> responseEntity = exceptionHandler.handleHttpMediaTypeNotSupported(ex, headers, request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling conflicts.
     */
    @DisplayName("Test for handleConflict() method")
    @Test
    void testHandleConflict() {
        MaxUploadSizeExceededException ex = new MaxUploadSizeExceededException(1000000L);
        WebRequest request = mock(WebRequest.class);
        ResponseEntity<Object> responseEntity = exceptionHandler.handleConflict(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling handleNoHandlerFoundException.
     */
    @DisplayName("Test for handleNoHandlerFoundException() method")
    @Test
    void testHandleNoHandlerFoundException() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/not-found", HttpHeaders.EMPTY);

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> responseEntity = exceptionHandler.handleNoHandlerFoundException(ex, headers, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Object responseBody = responseEntity.getBody();
        assertEquals(ErrorResponseDTO.class, responseBody.getClass());
        responseDTO = (ErrorResponseDTO) responseBody;
        assertEquals("Resource not found", responseDTO.getErrorMessage());
        assertEquals("1031", responseDTO.getErrorCode());
    }

    /**
     * Test: Method for handling handleHttpMessageNotReadable.
     */
    @DisplayName("Test for handleHttpMessageNotReadable() method")
    @Test
    void testHandleHttpMessageNotReadable() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Invalid JSON");

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest request = mock(WebRequest.class
        );

        ResponseEntity<Object> responseEntity = exceptionHandler.handleHttpMessageNotReadable(ex, headers, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Object responseBody = responseEntity.getBody();
        assertEquals(ErrorResponseDTO.class, responseBody.getClass());
        responseDTO = (ErrorResponseDTO) responseBody;
        assertEquals("Invalid JSON", responseDTO.getErrorMessage());
        assertEquals("1531", responseDTO.getErrorCode());
    }

    /**
     * Test: Method for handling ResponseStatusException.
     */
    @DisplayName("Test for ResponseStatusException() method")
    @Test
    void testResponseStatusException() {
        //when reason is not null
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST, "123-sample_reason");
        ResponseEntity<Object> responseEntity = exceptionHandler.responseStatus(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        //when reason is null
        ResponseStatusException responseStatusExceptionMock = mock(ResponseStatusException.class);
        when(responseStatusExceptionMock.getReason()).thenReturn(null);
        responseEntity = exceptionHandler.unKnownException(responseStatusExceptionMock);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        reset(responseStatusExceptionMock);

        //when reason is not valid
        ex = new ResponseStatusException(HttpStatus.BAD_REQUEST, "ïnvalid_reason");
        responseEntity = exceptionHandler.unKnownException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        //if exception is thrown
        when(responseStatusExceptionMock.getReason()).thenThrow(NullPointerException.class);
        responseEntity = exceptionHandler.unKnownException(responseStatusExceptionMock);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        reset(responseStatusExceptionMock);

    }

    /**
     * Test: Method for handling SignatureException.
     */
    @DisplayName("Test for SignatureException() method")
    @Test
    void testSignatureException() {
        SignatureException ex = new SignatureException("Invalid signature");
        ResponseEntity<Object> responseEntity = exceptionHandler.signatureException(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling ExpiredJwtException.
     */
    @DisplayName("Test for ExpiredJwtException() method")
    @Test
    void testExpiredJwtException() {
        ExpiredJwtException ex = new ExpiredJwtException(null, null, "Token has expired");
        ResponseEntity<Object> responseEntity = exceptionHandler.expiredJwtException(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling IllegalArgumentException.
     */
    @DisplayName("Test for IllegalArgumentException() method")
    @Test
    void testIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");
        ResponseEntity<Object> responseEntity = exceptionHandler.illegalException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling MalformedJwtException.
     */
    @DisplayName("Test for MalformedJwtException() method")
    @Test
    void testMalformedJwtException() {
        MalformedJwtException ex = new MalformedJwtException("Malformed JWT");
        ResponseEntity<Object> responseEntity = exceptionHandler.malformedException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling UserDisabledException.
     */
    @DisplayName("Test for UserDisabledException() method")
    @Test
    void testUserDisabledException() {
        UserDisabledException ex = new UserDisabledException("Malformed JWT");
        ResponseEntity<Object> responseEntity = exceptionHandler.userDisabledException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling InvalidUserException.
     */
    @DisplayName("Test for InvalidUserException() method")
    @Test
    void testInvalidUserException() {
        InvalidUserException ex = new InvalidUserException("Malformed JWT");
        ResponseEntity<Object> responseEntity = exceptionHandler.inValidUserException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling UserNotFoundException.
     */
    @DisplayName("Test for UserNotFoundException() method")
    @Test
    void testUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("Malformed JWT");
        ResponseEntity<Object> responseEntity = exceptionHandler.userNotFoundException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling FileUploadException.
     */
//    @DisplayName("Test for FileUploadException() method")
//    @Test
//    void testFileUploadException() {
//        FileUploadException ex = new FileUploadException("Malformed JWT");
//        ResponseEntity<Object> responseEntity = exceptionHandler.fileUploadException(ex);
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//    }

    /**
     * Test: Method for handling NullPointerException.
     */
    @DisplayName("Test for NullPointerException() method")
    @Test
    void testNullPointerException() {
        NullPointerException ex = new NullPointerException("Malformed JWT");
        ResponseEntity<Object> responseEntity = exceptionHandler.nullPointerException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        //when message is null
        ex = new NullPointerException(null);
        responseEntity = exceptionHandler.nullPointerException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling MultipartException.
     */
    @DisplayName("Test for MultipartException() method")
    @Test
    void testMultipartException() {
        MultipartException ex = new MultipartException("Malformed JWT");
        ResponseEntity<Object> responseEntity = exceptionHandler.multipartException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        //when message is null
        ex = new MultipartException(null);
        responseEntity = exceptionHandler.multipartException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        //when message is "Current request is not a multipart request"
        ex = new MultipartException("Current request is not a multipart request");
        responseEntity = exceptionHandler.multipartException(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling unknown exceptions.
     */
    @DisplayName("Test for handling unknown exceptions")
    @Test
    void testUnknownException() {
        Exception ex = new Exception("Malformed JWT");
        ResponseEntity<Object> responseEntity = exceptionHandler.unKnownException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Test: Method for handling unknown exceptions.
     */
    @DisplayName("Test for handling user authentication failed")
    @Test
    void testUserAuthenticationException() {
        Exception ex = new Exception("User Authentication Exception");
        ResponseEntity<Object> responseEntity = exceptionHandler.userAuthenticationException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testResponseStatusException_WithReason() {
        // Arrange
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST, "400-Bad Request");

        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.responseStatus(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        responseDTO = (ErrorResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(responseDTO);
        assertEquals("Bad Request", responseDTO.getErrorMessage());
        assertEquals("400", responseDTO.getErrorCode());
    }

    @Test
    void testResponseStatusException_WithoutReason() {
        // Arrange
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST);

        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.responseStatus(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        responseDTO = (ErrorResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(responseDTO);
        assertEquals(null, responseDTO.getErrorMessage());
        assertEquals(null, responseDTO.getErrorCode());
    }
}
