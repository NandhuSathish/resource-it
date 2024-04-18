/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

import com.innovature.resourceit.entity.ResourceSkill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author abdul.fahad
 */
@Getter
@Setter
@NoArgsConstructor
public class ResourceSkillResponseDTO {

    private Integer skillId;
    private String skillName;
    private Integer experience;
    private Byte proficiency;

    public ResourceSkillResponseDTO(ResourceSkill resourceSkill) {
        this.skillId = resourceSkill.getSkill().getId();
        this.skillName = resourceSkill.getSkill().getName();
        this.experience = resourceSkill.getExperience();
        this.proficiency = resourceSkill.getProficiency();
    }
}
