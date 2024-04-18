/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

import com.innovature.resourceit.entity.SkillExperience;
import lombok.Data;

import java.util.List;

/**
 * @author abdul.fahad
 */
@Data
public class SkillExperienceResponseDTO {
    private int skillId;
    private int skillMinValue;
    private int skillMaxValue;
    private String name;
    private List<Byte> proficiency;

    public SkillExperienceResponseDTO(SkillExperience skillExperience) {
        this.skillId = skillExperience.getSkillId();
        this.skillMinValue = skillExperience.getSkillMinValue();
        this.skillMaxValue = skillExperience.getSkillMaxValue();
        this.name = skillExperience.getName();
        this.proficiency = skillExperience.getProficiency();
    }
}
