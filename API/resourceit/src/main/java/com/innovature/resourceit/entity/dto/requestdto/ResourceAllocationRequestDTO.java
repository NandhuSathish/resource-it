package com.innovature.resourceit.entity.dto.requestdto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

//dto for getting  resource wise allocation requests
@Getter
@Setter
public class ResourceAllocationRequestDTO {
    @NotNull(message = "PROJECT_ID_CANNOT_BE_NULL")
    private Integer projectId;
    @NotNull(message = "RESOURCE_ID_CANNOT_BE_NULL")
    private Integer resourceId;
    private Integer allocationId;
    @NotNull(message = "ALLOCATION_START_DATE_CANNOT_BE_NULL")
    private Date startDate;
    @NotNull(message = "ALLOCATION_END_DATE_CANNOT_BE_NULL")
    private Date endDate;
}
