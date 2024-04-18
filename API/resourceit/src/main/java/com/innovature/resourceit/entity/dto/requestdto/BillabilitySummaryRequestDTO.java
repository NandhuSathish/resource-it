package com.innovature.resourceit.entity.dto.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class BillabilitySummaryRequestDTO {
    private String name;
    private String employeeId;
    private List<Integer> departmentIds;
    private List<ResourceFilterSkillAndExperienceRequestDTO> skillAndExperiences;
    private List<Integer> projectIds;
    private Integer lowerExperience;
    private Integer highExperience;
    private Date startDate;
    private Date endDate;
    private Integer status;
    private String pageNumber;
    private String pageSize;
    private Boolean sortOrder = false;
    private String sortKey;
}
