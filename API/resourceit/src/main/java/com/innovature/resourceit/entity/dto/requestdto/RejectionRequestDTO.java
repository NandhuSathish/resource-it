package com.innovature.resourceit.entity.dto.requestdto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectionRequestDTO {
    @NotNull(message = "REJECTION_MESSAGE_CANNOT_BE_NULL")
    @NotEmpty(message = "REJECTION_MESSAGE_CANNOT_BE_EMPTY")
    @Size(max = 1000, message = "ALLOCATION_REJECT_REASON_SIZE")
    private String message;
}
