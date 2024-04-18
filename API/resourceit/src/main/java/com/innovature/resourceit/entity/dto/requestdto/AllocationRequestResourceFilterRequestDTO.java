package com.innovature.resourceit.entity.dto.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AllocationRequestResourceFilterRequestDTO {

    private Integer pageNumber;

    private Integer pageSize;

    private String resourceName;

    private List<Integer> departmentIds;

    private Integer experienceMinValue;

    private Integer experienceMaxValue;

    private List<ResourceFilterSkillAndExperienceRequestDTO> skillsAndExperiences;
    private Date allocationStartDate;

    private Date allocationEndDate;

}
