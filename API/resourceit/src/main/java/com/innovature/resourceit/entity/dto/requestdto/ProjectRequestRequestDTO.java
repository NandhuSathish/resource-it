package com.innovature.resourceit.entity.dto.requestdto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProjectRequestRequestDTO {
    private Integer projectId;
    @Size(max = 1000, message = "PROJECT_CODE_SIZE_INVALID")
    @NotBlank(message = "PROJECT_CODE_REQUIRED")
    @Pattern(regexp = "^[a-zA-Z0-9_\\s]*$", message = "INVALID_PROJECT_CODE")
    private String projectCode;
    @Size(max = 1000, message = "PROJECT_NAME_SIZE_INVALID")
    @NotBlank(message = "PROJECT_NAME_REQUIRED")
    @NotNull(message = "PROJECT_NAME_REQUIRED")
    private String name;
    @Min(value = 0, message = "PROJECT_TYPE_INVALID")
    @Max(value = 1, message = "PROJECT_TYPE_INVALID")
    @NotNull(message = "PROJECT_TYPE_REQUIRED")
    private Byte projectType;
    @Size(max = 1000, message = "DESCRIPTION_SIZE_INVALID")
    private String description;
    @NotBlank(message = "CLIENT_NAME_REQUIRED")
    @Pattern(regexp = "^(?!\\s*$)\\s*[\\s\\S]*$", message = "INVALID_CLIENT_NAME")
    @Size(max = 100, message = "CLIENT_NAME_SIZE_INVALID")
    private String clientName;
    private Integer teamSize;
    private Integer manDay;
    private Integer managerId;
    private Date startDate;
    private Date endDate;
    private List<Integer> skillIds;
    private Byte approvalStatus;
}
