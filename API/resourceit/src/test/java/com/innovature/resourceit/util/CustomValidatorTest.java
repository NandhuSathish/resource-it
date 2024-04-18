package com.innovature.resourceit.util;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomValidatorTest {

    @InjectMocks
    private CustomValidator customValidator;

    @Mock
    private MessageSource messageSource;

    @Mock
    private ResourceRepository resourceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateType_ShouldNotThrowException_WhenTypeMatches() {
        // Arrange
        Integer value = 10;
        Class<?> expectedType = Integer.class;

        // Act & Assert
        customValidator.validateType(value, expectedType, "Message");
    }

    @Test
    void validateType_ShouldThrowBadRequestException_WhenTypeDoesNotMatch() {
        // Arrange
        String value = "InvalidValue";
        Class<?> expectedType = Integer.class;
        when(messageSource.getMessage(any(), any(), any())).thenReturn("InvalidTypeMessage");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> customValidator.validateType(value, expectedType, "Message"));
    }

    @Test
    void validateValue_ShouldNotThrowException_WhenValueIsValid() {
        // Arrange
        Byte value = 1;
        List<Byte> validValues = List.of((byte) 0, (byte) 1);

        // Act & Assert
        customValidator.validateValue(value, validValues, "Message");
    }

    @Test
    void validateValue_ShouldThrowBadRequestException_WhenValueIsInvalid() {
        // Arrange
        Byte value = 2;
        List<Byte> validValues = List.of((byte) 0, (byte) 1);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("InvalidValueMessage");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> customValidator.validateValue(value, validValues, "Message"));
    }

    @Test
    void validateProjectRequestInputParameters_ShouldThrowBadRequestException_WhenManagerIdNotFound() {
        // Arrange
        Integer managerId = 1;
        when(resourceRepository.findByIdAndStatus(managerId, Resource.Status.ACTIVE.value)).thenReturn(Optional.empty());
        when(messageSource.getMessage(any(), any(), any())).thenReturn("UserNotFoundMessage");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> customValidator.validateProjectRequestInputParameters(
                "Description", new Date(), new Date(), (byte) 1, (byte) 0, managerId));
    }

    @Test
    void validateType1_ShouldThrowBadRequestException_WhenValueIsNullAndExpectedTypeIsNotNull() {
        // Arrange
        Integer value = null;
        Class<?> expectedType = Integer.class;
        when(messageSource.getMessage(any(), any(), any())).thenReturn("NullMessage");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> customValidator.validateType1(value, expectedType, "TypeMessage", "NullMessage"));
    }

    @Test
    void validateProjectRequestDTO_ShouldThrowBadRequestException_WhenStartDateAfterEndDate() {
        // Arrange
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setStartDate(new Date(System.currentTimeMillis() + 100000)); // Set a future start date
        dto.setEndDate(new Date());
        dto.setManDay(100);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("InvalidDateMessage");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> customValidator.validateProjectRequestDTO(dto));
    }

    @Test
    void validateProjectRequestDTO_ShouldNotThrowException_WhenDatesAreValid() {
        // Arrange
        ProjectRequestRequestDTO dto = new ProjectRequestRequestDTO();
        dto.setStartDate(new Date());
        dto.setManDay(100);
        dto.setEndDate(new Date(System.currentTimeMillis() + 100000)); // Set a future end date
        dto.setManagerId(1);
        dto.setProjectId(1);
        // Act & Assert
        assertDoesNotThrow(() -> customValidator.validateProjectRequestDTO(dto));
    }


}
