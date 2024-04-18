package com.innovature.resourceit.entity;

import com.innovature.resourceit.entity.dto.requestdto.ResourceAllocationRequestDTO;
import com.innovature.resourceit.repository.ProjectRepository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResourceAllocationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "project_id")
    @NotNull
    private Project project;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    @NotNull
    private Resource resource;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "allocation_id")
    private Allocation allocation;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date endDate;
    private Byte status = StatusValues.ACTIVE.value;

    private Byte approvalFlow = ApprovalFlowValues.PENDING.value;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "requested_by")
    @NotNull
    private Resource requestedBy;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "rejected_by")
    private Resource rejectedBy;

    private String rejectionReason = "";

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate = new Date();

    public ResourceAllocationRequest(ResourceAllocationRequestDTO dto, Resource requestedBy,
            ProjectRepository projectRepository) {
        this.project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + dto.getProjectId()));
        this.resource = new Resource(dto.getResourceId());
        this.allocation = dto.getAllocationId() != null ? new Allocation(dto.getAllocationId()) : null;
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.requestedBy = requestedBy;

        switch (requestedBy.getRole().getName()) {
            case "HR" -> this.approvalFlow = ApprovalFlowValues.APPROVED_BY_HR.value;
            case "HOD" -> this.approvalFlow = ApprovalFlowValues.APPROVED_BY_HOD.value;
            default -> this.approvalFlow = ApprovalFlowValues.PENDING.value;
        }
    }

    public enum ApprovalFlowValues {
        PENDING((byte) 0), APPROVED_BY_HOD((byte) 1), APPROVED_BY_HR((byte) 2), APPROVED((byte) 3), REJECTED((byte) 4);

        public final byte value;

        ApprovalFlowValues(byte value) {
            this.value = value;
        }
    }

    public enum StatusValues {
        ACTIVE((byte) 1), DELETED((byte) 0);

        public final Byte value;

        private StatusValues(Byte value) {
            this.value = value;
        }
    }
}
