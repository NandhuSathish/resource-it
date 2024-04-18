package com.innovature.resourceit.entity.dto.request;

import com.innovature.resourceit.entity.dto.requestdto.RoleRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class RoleRequestDTOTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void testRoleRequestDTO() {
        // Create a sample RoleRequestDTO
        RoleRequestDTO roleRequestDTO = new RoleRequestDTO();
        roleRequestDTO.setRole("Admin");

        // Test the getter
        assertEquals("Admin", roleRequestDTO.getRole());

        // Test validation
        var violations = validator.validate(roleRequestDTO);
        assertTrue(violations.isEmpty(), "There should be no validation errors");

        // Test constraints
        roleRequestDTO.setRole(""); // Set an empty role
        violations = validator.validate(roleRequestDTO);
        assertEquals(2, violations.size(), "There should be two validation errors for an empty role");

        roleRequestDTO.setRole("A very long role name that exceeds the maximum allowed length"); // Set a long role name
        violations = validator.validate(roleRequestDTO);
        assertEquals(1, violations.size(), "There should be one validation error for a role name exceeding the maximum length");

//        roleRequestDTO.setRole("Role With Invalid Characters !@#}"); // Set a role with invalid characters
//        violations = validator.validate(roleRequestDTO);
//        assertEquals(1, violations.size(), "There should be one validation error for a role with invalid characters");
    }
}
