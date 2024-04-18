package com.innovature.resourceit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resource_skill")
public class ResourceSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private int experience;
    private Byte proficiency = proficiencyValues.BEGINNER.value;

    public enum proficiencyValues {
        BEGINNER((byte) 0), INTERMEDIATE((byte) 1), ADVANCED((byte) 2);

        public final byte value;

        proficiencyValues(byte value) {
            this.value = value;
        }
    }

}

