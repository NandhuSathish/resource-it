package com.innovature.resourceit.entity.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleRequestDTO {
    @NotBlank(message = "ROLE_REQUIRED")
    @Size(min = 1, max = 49, message = "ROLE_SIZE_INVALID")
    @Pattern(regexp = "^[a-zA-Z0-9\\p{Punct}]+([\\s][a-zA-Z0-9\\p{Punct}]+)*$|^$", message = "ROLE_PATTERN_INVALID")
    private String role;
}
