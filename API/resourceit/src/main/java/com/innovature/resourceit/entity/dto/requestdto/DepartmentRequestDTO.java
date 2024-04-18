package com.innovature.resourceit.entity.dto.requestdto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class DepartmentRequestDTO {

    @NotBlank(message = "NAME_REQUIRED")
    @Size(max = 50, message = "NAME_SIZE_INVALID")
    private String name;

    @NotNull(message = "DISPLAY_ORDER_REQUIRED")
    @Max(value = 20, message = "INVALID_DISPLAY_ORDER")
    private Integer displayOrder;

}
