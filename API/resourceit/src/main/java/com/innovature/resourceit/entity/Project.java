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
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;
    private String projectCode;
    private String name;
    private Byte projectType;
    private String description;
    private String clientName;
    private Integer teamSize;
    private Integer manDay;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Resource manager;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "project_skill", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Collection<Skill> skill;
    private Byte edited;
    private Byte status;
    private Byte projectState;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    public Project(Integer projectId) {
        this.projectId = projectId;
    }

    public Project(ProjectRequestRequestDTO dto, List<Skill> skills, Resource manager) {
        this.projectCode = dto.getProjectCode();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.clientName = dto.getClientName();
        switch (dto.getProjectType()) {
            case (byte) 1 -> this.projectType = projectTypeValues.BILLABLE.value;
            case (byte) 2 -> this.projectType = projectTypeValues.SUPPORT.value;
            default -> this.projectType = projectTypeValues.INTERNAL.value;
        }
        this.teamSize = dto.getTeamSize();
        this.manDay = dto.getManDay();
        this.manager = manager != null ? manager : null;
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.skill = skills;
        this.edited = editedValues.NOT_EDITED.value;
        this.projectState = projectStateValues.NOT_STARTED.value;
        this.status = statusValues.ACTIVE.value;
        Date dt = new Date();
        this.createdDate = dt;
        this.updatedDate = dt;

    }

    public Project update(ProjectRequestRequestDTO dto, List<Skill> skills, Resource manager, Byte projectState) {
        this.projectCode = dto.getProjectCode();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.clientName = dto.getClientName();
        switch (dto.getProjectType()) {
            case (byte) 1 -> this.projectType = projectTypeValues.BILLABLE.value;
            case (byte) 2 -> this.projectType = projectTypeValues.SUPPORT.value;
            default -> this.projectType = projectTypeValues.INTERNAL.value;
        }
        this.manDay = dto.getManDay();
        this.manager = manager;
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.skill = skills;
        this.projectState = projectState;
        this.updatedDate = new Date();
        return this;

    }


    public enum projectStateValues {
        NOT_STARTED((byte) 0), IN_PROGRESS((byte) 1), COMPLETED((byte) 2);

        public final Byte value;

        private projectStateValues(Byte value) {
            this.value = value;
        }
    }

    public enum projectTypeValues {
        INTERNAL((byte) 0), BILLABLE((byte) 1), SUPPORT((byte) 2), BENCH((byte) 3);

        public final Byte value;

        private projectTypeValues(Byte value) {
            this.value = value;
        }
    }

    public enum editedValues {
        NOT_EDITED((byte) 0), EDITED((byte) 1);

        public final Byte value;

        private editedValues(Byte value) {
            this.value = value;
        }
    }

    public enum statusValues {
        PENDING((byte) 0), ACTIVE((byte) 1), DELETED((byte) 3),
        // to indicate that project is in halfway of completion.
        HALFWAY((byte) 2);

        public final Byte value;

        private statusValues(Byte value) {
            this.value = value;
        }
    }
}
