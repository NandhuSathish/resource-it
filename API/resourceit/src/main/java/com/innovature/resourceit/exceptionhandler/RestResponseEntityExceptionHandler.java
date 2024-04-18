package com.innovature.resourceit.exceptionhandler;

import com.innovature.resourceit.entity.dto.responsedto.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Abdul.fahad
 */
@ControllerAdvice
public final class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @Autowired
    MessageSource messageSource;

    public RestResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ResponseEntity<Object> createErrorResponseEntity(String errorString, HttpStatus httpStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ErrorResponseDTO responseView = null;

        String errorMessage = null;
        String errorCode = null;

        String message = messageSource.getMessage(errorString, null, "An error occurred", Locale.ENGLISH);
        if (message != null) {
            String[] errorResponse = message.split("-");
            errorMessage = errorResponse.length == 2 ? errorResponse[1] : errorResponse[0];
            errorCode = errorResponse.length == 2 ? errorResponse[0] : "500"; // Default error code
            responseView = new ErrorResponseDTO(errorMessage, errorCode);
        } else {
            responseView = new ErrorResponseDTO("An error occurred", "500");
        }
        return new ResponseEntity<>(responseView, headers, httpStatus);
    }

    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public ResponseEntity<Object> handleConflict(MaxUploadSizeExceededException ex, WebRequest request) {
        EXCEPTION_LOGGER.error("Max upload size exceeded exception caught. Exception: {}", ex.getMessage());
        return createErrorResponseEntity("INVALID_FILE_SIZE", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, WebRequest request) {
        ErrorResponseDTO responseView = new ErrorResponseDTO(ex.getMessage(), "600");
        EXCEPTION_LOGGER.error("HTTP Method not supported exception caught. Exception: {}", ex.getMessage());
        return handleExceptionInternal(ex, responseView, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
        Collections.sort(errors);
        String[] list = messageSource.getMessage(errors.get(0), null, Locale.ENGLISH).split("-");
        EXCEPTION_LOGGER.error("Method argument not valid exception caught. Exception: {}", ex.getMessage());
        ErrorResponseDTO responseView = new ErrorResponseDTO(list[1], list[0]);
        return handleExceptionInternal(ex, responseView, headers, HttpStatus.BAD_REQUEST, request);
    }

    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, WebRequest request) {
        EXCEPTION_LOGGER.error("HttpMediaTypeNotSupportedException:{}", ex.getMessage());
        ErrorResponseDTO responseView = new ErrorResponseDTO("mediaTypeNotSupported", "32000");
        return handleExceptionInternal(ex, responseView, headers, HttpStatus.BAD_REQUEST, request);
    }

    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, WebRequest request) {
        EXCEPTION_LOGGER.error("NoHandlerFoundException: {}", ex.getMessage());
        ErrorResponseDTO responseView = new ErrorResponseDTO("Resource not found", "1031");
        return handleExceptionInternal(ex, responseView, headers, HttpStatus.BAD_REQUEST, request);
    }

    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, WebRequest request) {
        EXCEPTION_LOGGER.error("HttpMessageNotReadableException: {}", ex.getMessage());
        ErrorResponseDTO responseView = new ErrorResponseDTO("Invalid JSON", "1531");
        return handleExceptionInternal(ex, responseView, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {ResponseStatusException.class})
    public ResponseEntity<Object> responseStatus(ResponseStatusException ex) throws NullPointerException {
        String reason = ex.getReason();
        String errorCode = null;
        String errorMessage = null;
        if (reason != null) {
            String[] arrOfStr = reason.split("-", 2);
            try {
                if (arrOfStr.length > 1) {
                    errorCode = arrOfStr[0];
                    errorMessage = arrOfStr[1];
                } else {
                    errorMessage = ex.getReason();
                }
            } catch (Exception e) {
                EXCEPTION_LOGGER.error(e.getMessage());
            }
        }

        if (errorMessage == null) {
            String field = null;
            if (errorCode != null && errorCode.contains("java.lang.NumberFormatException")) {
                field = errorCode.replaceAll("^.*for property '(.*)';.*$", "$1");
            }
            if (field != null && !"".equals(field)) {
                errorCode = "invalid " + field;
            }
            errorMessage = errorCode;
        }
        ErrorResponseDTO responseView = new ErrorResponseDTO(errorMessage, errorCode);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(responseView, httpHeaders, ex.getStatusCode());
    }

    @ExceptionHandler(value = {SignatureException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> signatureException(Exception ex) {
        EXCEPTION_LOGGER.error("Signature Exception: {}", ex.getMessage());
        return createErrorResponseEntity("invalid.token", HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> expiredJwtException(Exception ex) {
        EXCEPTION_LOGGER.error("Expired JWT Exception: {}", ex.getMessage());
        return createErrorResponseEntity("jwt.token.expired", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> illegalException(IllegalArgumentException ex) {
        EXCEPTION_LOGGER.error("Unable to get JWT Token: {}", ex.getMessage());
        return createErrorResponseEntity("unable.to.get.jwt.token", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MalformedJwtException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> malformedException(MalformedJwtException ex) {
        EXCEPTION_LOGGER.error("MalformedJwtException: {}", ex.getMessage());
        return createErrorResponseEntity("invalid.token.format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserDisabledException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> userDisabledException(Exception ex) {
        EXCEPTION_LOGGER.error("UserDisabledException: {}", ex.getMessage());
        return createErrorResponseEntity("user.disabled", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidUserException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> inValidUserException(Exception ex) {
        EXCEPTION_LOGGER.error("InvalidUserException: {}", ex.getMessage());
        return createErrorResponseEntity("invalid.user", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> userNotFoundException(Exception ex) {
        EXCEPTION_LOGGER.error("UserNotFoundException: {}", ex.getMessage());
        return createErrorResponseEntity("user.not.found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> nullPointerException(NullPointerException ex) {
        EXCEPTION_LOGGER.error("NullPointerException:{}", ex.getMessage());
        ErrorResponseDTO responseView = null;
        if (ex.getMessage() == null) {
            return createErrorResponseEntity("invalid.request.parameter", HttpStatus.BAD_REQUEST);
        }
        responseView = new ErrorResponseDTO(ex.getMessage(), "1030");
        return new ResponseEntity<>(responseView, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MultipartException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> multipartException(MultipartException ex) {
        EXCEPTION_LOGGER.error("Multipart exception: {}", ex.getMessage());
        String message = ex.getMessage();
        if (message != null) {
            if (message.equals("Current request is not a multipart request")) {
                return createErrorResponseEntity("media.type.not.supported", HttpStatus.UNAUTHORIZED);
            } else {
                return unKnownException(ex);
            }
        }
        return unKnownException(ex);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> unKnownException(Exception ex) {
        EXCEPTION_LOGGER.error("Unknown exception from class: {}. Exception: {}", ex.getClass(), ex.getMessage());
        ErrorResponseDTO responseView = new ErrorResponseDTO(ex.getMessage(), "400");
        return new ResponseEntity<>(responseView, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserAuthenticationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> userAuthenticationException(Exception ex) {
        EXCEPTION_LOGGER.error("UserAuthenticationException: {}", ex.getMessage());
        ErrorResponseDTO responseView = new ErrorResponseDTO("User authentication  failed", "1056");
        return new ResponseEntity<>(responseView, HttpStatus.BAD_REQUEST);
    }
}
