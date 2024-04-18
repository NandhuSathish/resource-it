/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author abdul.fahad
 */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResourceSkillWiseAllocationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    @NotNull
    private Project project;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    @NotNull
    private Department department;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @NotNull
    private Set<SkillExperience> skillExperiences = new HashSet<>();

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date endDate;

    private int experience;

    private int resourceCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requested_by")
    @NotNull
    private Resource requestedBy;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "rejected_by")
    private Resource rejectedBy;
    private Byte status = StatusValues.ACTIVE.value;

    private Byte approvalFlow = ApprovalFlowValues.PENDING.value;

    private String rejectionReason = "";

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate = new Date();

    public enum ApprovalFlowValues {
        PENDING((byte) 0), APPROVED_BY_HOD((byte) 3), APPROVED((byte) 1), REJECTED((byte) 2);

        public final byte value;

        ApprovalFlowValues(byte value) {
            this.value = value;
        }
    }

    public enum StatusValues {
        ACTIVE((byte) 1), DELETED((byte) 0);

        public final Byte value;

        StatusValues(Byte value) {
            this.value = value;
        }
    }
}
