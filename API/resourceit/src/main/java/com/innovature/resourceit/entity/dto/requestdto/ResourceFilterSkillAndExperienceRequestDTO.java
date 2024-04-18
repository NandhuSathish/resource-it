package com.innovature.resourceit.entity.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceFilterSkillAndExperienceRequestDTO {
    private String skillId;

    private String skillMinValue;
    private String skillMaxValue;
    private List<Byte> proficiency;
}
