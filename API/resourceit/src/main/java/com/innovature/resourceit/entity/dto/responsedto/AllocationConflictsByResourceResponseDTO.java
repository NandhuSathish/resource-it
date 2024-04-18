package com.innovature.resourceit.entity.dto.responsedto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AllocationConflictsByResourceResponseDTO {
    private String resourceName;
    private String projectCode;
    private String projectName;
    private String startDate;
    private String endDate;
}
