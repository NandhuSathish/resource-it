package com.innovature.resourceit.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.innovature.resourceit.entity.dto.responsedto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.http.converter.HttpMessageNotReadableException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponseDTO> handleValidationException(Exception ex) {
        // Get the validation error details
        List<FieldError> fieldErrors;

        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            fieldErrors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> new FieldError(error.getObjectName(), error.getField(), error.getDefaultMessage()))
                    .toList();
        } else if (ex instanceof ConstraintViolationException constraintViolationException) {
            fieldErrors = constraintViolationException.getConstraintViolations()
                    .stream()
                    .map(this::mapConstraintViolationToFieldError)
                    .toList();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        // Create the validation error response
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        String message = messageSource.getMessage(fieldErrors.get(0).getDefaultMessage(), null, Locale.ENGLISH);
        String[] parts = message.split("-");

        String errorCode = parts[0];
        String errorMessage = parts[1].trim();

        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorMessage(errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String fieldName = ex.getName();
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        String message = messageSource.getMessage("ARGUMENT_TYPE_MISMATCH", null, Locale.ENGLISH);
        String[] parts = message.split("-");

        String errorCode = parts[0];
        String errorMessage = parts[1].trim();

        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorMessage(errorMessage + " " + fieldName);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private FieldError mapConstraintViolationToFieldError(ConstraintViolation<?> violation) {
        String objectName = Objects.requireNonNullElse(violation.getLeafBean(), "").getClass().getSimpleName();
        String propertyPath = Objects.requireNonNullElse(violation.getPropertyPath(), "").toString();

        if (objectName == null) {
            throw new IllegalArgumentException("Object name must not be null");
        }

        return new FieldError(objectName, propertyPath, violation.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponseDTO> handleJsonParseException(HttpMessageNotReadableException ex) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();

        String errorMessage = "Invalid Request body";

        if (mostSpecificCause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = invalidFormatException.getPath().get(0).getFieldName();
            errorMessage = "Invalid value for field: " + fieldName;
        }
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(errorMessage, "1531");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}
