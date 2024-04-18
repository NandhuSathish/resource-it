package com.innovature.resourceit.entity.dto.responsedto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BillabilitySummaryResponseDTO {

    private Integer employeeId;
    private Integer resourceId;
    private String name;
    private String departmentName;
    private String role;
    private byte allocationStatus;
    private String joiningDate;
    private Integer workSpan;
    private String projectName;
    private String totalExperience;
    private Integer billableDays;
    private Integer benchDays;
    private Byte status;
    private List<ResourceSkillResponseDTO> resourceSkillResponseDTOs;
}
