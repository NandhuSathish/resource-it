package com.innovature.resourceit.entity.dto.responsedto;

import lombok.Getter;
import lombok.Setter;

//for listing resource wise allocation requests
@Getter
@Setter
public class ResourceAllocationResponseDTO {
    private Integer id;
    private Integer allocationId;
    private Integer projectId;
    private String projectName;
    private String projectCode;
    private Integer resourceId;
    private String resourceName;
    private String resourceEmployeeId;
    private Integer departmentId;
    private String departmentName;
    private String requestedByName;
    private String requestedByEmployeeId;
    private String startDate;
    private String endDate;
    private String rejectionReason;
    private Byte approvalFlow;
    private Integer conflictDays;
    private String createdDate;
    private String updatedDate;

}
