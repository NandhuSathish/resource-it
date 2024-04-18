package com.innovature.resourceit.entity.dto.requestdto;

import com.innovature.resourceit.entity.ResourceSkill;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceSkillRequestDTO {
    @NotNull
    private Integer skillId;
    @NotNull
    private Integer experience;
    private Byte proficiency = ResourceSkill.proficiencyValues.BEGINNER.value;

    public ResourceSkillRequestDTO(int skillId, int experience, Byte proficiency) {
        this.skillId = skillId;
        this.experience = experience;
        this.proficiency = proficiency;
    }
}
