/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.innovature.resourceit.entity.dto.requestdto.ResourceRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

/**
 * @author abdul.fahad
 */
@Entity
@Table(name = "resource")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer employeeId;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", referencedColumnName = "departmentId")
    private Department department;
    @NotNull
    @Column(unique = true)
    private String email;

    private String name;

    private Date joiningDate;
    private Integer prevExperience;
    private Integer experience;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
    private Byte allocationStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    private byte status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastWorkedProjectDate;
    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ResourceSkill> resourceSkills = new HashSet<>();

    public Resource(Integer id) {
        this.id = id;

    }

    public enum Status {
        // active-1 inactive(relieve)-0 exclude( permanent delete)-2
        INACTIVE((byte) 0), ACTIVE((byte) 1), EXCLUDE((byte) 2);

        public final byte value;

        Status(byte value) {
            this.value = value;
        }
    }

    public enum AllocationStatus {
        BENCH((byte) 0), INTERNAL((byte) 1), EXTERNAL((byte) 2);

        public final byte value;

        AllocationStatus(byte value) {
            this.value = value;
        }
    }

    public Resource(ResourceRequestDTO dto, Role role, Department department) {
        this.employeeId = dto.getEmployeeId();
        this.name = dto.getName();
        this.email = dto.getEmail().toLowerCase();
        this.prevExperience = dto.getExperience();
        this.experience = dto.getExperience();
        this.joiningDate = dto.getJoiningDate();
        this.createdDate = new Date();
        this.updatedDate = new Date();
        this.status = Status.ACTIVE.value;
        this.role = role;
        this.department = department;
        this.allocationStatus = AllocationStatus.BENCH.value;
        this.lastWorkedProjectDate = dto.getJoiningDate();
    }

    public enum Roles {
        ADMIN("ADMIN", 1),
        HOD("HOD", 2),
        HR("HR", 3),
        DH("DIVISION HEAD", 4),
        PM("PROJECT MANAGER", 5),
        RM("RESOURCE MANAGER", 6),
        RESOURCE("RESOURCE", 7);

        private final String value;
        private final Integer id;

        private Roles(String value, Integer id) {
            this.value = value;
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public Integer getId() {
            return id;
        }
    }
}
