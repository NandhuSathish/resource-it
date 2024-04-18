package com.innovature.resourceit.entity;

import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectRequestId;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;
    private String name;
    private String projectCode;
    private String description;
    private String clientName;
    private Byte projectType;
    private Integer teamSize;
    private Integer manDay;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private Resource requestedBy;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private Resource manager;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "project_request_skill", joinColumns = @JoinColumn(name = "project_request_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Collection<Skill> skill;
    private Byte approvalStatus;
    private Byte status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    public ProjectRequest(ProjectRequestRequestDTO dto, List<Skill> skills, Resource requestedBy) {
        initializeFields(dto, skills, 0, requestedBy);
    }

    public ProjectRequest(ProjectRequestRequestDTO dto, List<Skill> skills, Integer teamSize, Resource requestedBy) {
        initializeFields(dto, skills, teamSize, requestedBy);
    }

    private void initializeFields(ProjectRequestRequestDTO dto, List<Skill> skills, Integer teamSize, Resource requestedBy) {
        this.project = dto.getProjectId() != null ? new Project(dto.getProjectId()) : null;
        this.projectCode = dto.getProjectCode();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.clientName = dto.getClientName();
        switch (dto.getProjectType()) {
            case (byte) 1 -> this.projectType = ProjectRequest.projectTypeValues.BILLABLE.value;
            case (byte) 2 -> this.projectType = ProjectRequest.projectTypeValues.SUPPORT.value;
            default -> this.projectType = ProjectRequest.projectTypeValues.INTERNAL.value;
        }
        this.teamSize = teamSize;
        this.manDay = dto.getManDay();
        this.manager = dto.getManagerId() != null ? new Resource(dto.getManagerId()) : null;
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.skill = skills;
        this.approvalStatus = dto.getApprovalStatus() == null ? approvalStatusValues.PENDING.value : dto.getApprovalStatus();
        this.status = statusValues.ACTIVE.value;
        this.requestedBy = requestedBy;
        Date dt = new Date();
        this.createdDate = dt;
        this.updatedDate = dt;
    }

    public enum approvalStatusValues {
        PENDING((byte) 0), APPROVED((byte) 1), REJECTED((byte) 2);

        public final byte value;

        private approvalStatusValues(byte value) {
            this.value = value;
        }
    }

    public enum projectTypeValues {
        INTERNAL((byte) 0), BILLABLE((byte) 1), SUPPORT((byte) 2);
        public final Byte value;

        private projectTypeValues(Byte value) {
            this.value = value;
        }
    }

    public enum statusValues {
        ACTIVE((byte) 1), DELETED((byte) 0);
        public final Byte value;

        private statusValues(Byte value) {
            this.value = value;
        }
    }

}
