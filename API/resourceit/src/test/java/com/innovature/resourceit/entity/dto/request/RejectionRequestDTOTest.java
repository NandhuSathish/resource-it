package com.innovature.resourceit.entity.dto.request;

import com.innovature.resourceit.entity.dto.requestdto.RejectionRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RejectionRequestDTOTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testValidRejectionRequestDTO() {
        RejectionRequestDTO rejectionRequestDTO = new RejectionRequestDTO();
        rejectionRequestDTO.setMessage("Valid rejection message");

        Set violations = validator.validate(rejectionRequestDTO);
        assertEquals(0, violations.size(), "There should not be any violations for a valid RejectionRequestDTO");
    }

    @Test
    void testNullMessage() {
        RejectionRequestDTO rejectionRequestDTO = new RejectionRequestDTO();
        rejectionRequestDTO.setMessage(null);

        Set violations = validator.validate(rejectionRequestDTO);
        assertEquals(2, violations.size(), "There should be a violation for a null message");
    }

    @Test
    void testEmptyMessage() {
        RejectionRequestDTO rejectionRequestDTO = new RejectionRequestDTO();
        rejectionRequestDTO.setMessage("");

        Set violations = validator.validate(rejectionRequestDTO);
        assertEquals(1, violations.size(), "There should be a violation for an empty message");
    }
}
