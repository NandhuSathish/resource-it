/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * @author abdul.fahad
 */
@Entity
@Table(name = "skill")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Integer id;

    private String name;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", referencedColumnName = "departmentId")
    private Department department;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    
    public Skill(int id) {
        this.id = id;
    }

    public Skill(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Skill(Integer skillId) {
        this.id = skillId;
    }
}
