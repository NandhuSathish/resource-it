package com.innovature.resourceit.entity.dto.responsedto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectAllocationResponseDTO {

    Integer resourceId;
    Integer employeeId;
    String resourceName;
    String projectName;
    String projectCode;
    Byte projectType;
    String startDate;
    String endDate;
    Byte status;
}
