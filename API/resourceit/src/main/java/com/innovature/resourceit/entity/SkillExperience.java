/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author abdul.fahad
 */
@Entity
@Data
@NoArgsConstructor
public class SkillExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int skillId;

    private String name;

    private int skillMinValue;
    private int skillMaxValue;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Byte> proficiency;

    public SkillExperience(int skillId, int skillMinValue, int skillMaxValue, String name, List<Byte> proficiency) {
        this.skillId = skillId;
        this.skillMinValue = skillMinValue;
        this.skillMaxValue = skillMaxValue;
        this.name = name;
        this.proficiency = proficiency;
    }
}
