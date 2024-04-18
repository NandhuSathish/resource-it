/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity;

import com.innovature.resourceit.entity.dto.requestdto.ResourceAllocationRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author abdul.fahad
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Allocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", referencedColumnName = "projectId")
    private Project project;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    private Resource resource;

    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    private Byte status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    private Byte allocationExpiry;
    private Byte isEdited = IsEditedValues.NOT_EDITED.value;

    private Byte isRemoved;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "requested_by")
    @NotNull
    private Resource requestedBy;


    public Allocation(Integer allocationId) {
        this.id = allocationId;
    }

    public Allocation(ResourceAllocationRequest request) {
        this.project = request.getProject();
        this.resource = request.getResource();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.status = StatusValues.ACTIVE.value;
        Date dt = new Date();
        this.createdDate = dt;
        this.updatedDate = dt;
        this.allocationExpiry = AllocationExpiryValues.NOT_STARTED.value;
        this.isRemoved = IsRemoved.NO.value;
        this.requestedBy = request.getRequestedBy();

    }

    public Allocation(ResourceAllocationRequestDTO dto, @NotNull Resource requestedBy) {
        this.project = new Project(dto.getProjectId());
        this.resource = new Resource(dto.getResourceId());
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.status = StatusValues.ACTIVE.value;
        Date dt = new Date();
        this.createdDate = dt;
        this.updatedDate = dt;
        this.allocationExpiry = AllocationExpiryValues.NOT_STARTED.value;
        this.isRemoved = IsRemoved.NO.value;
        this.requestedBy = requestedBy;
    }

    public enum AllocationExpiryValues {

        NOT_STARTED((byte) 0), ON_GOING((byte) 1), EXPIRED((byte) 2);

        public final byte value;

        AllocationExpiryValues(byte value) {
            this.value = value;
        }
    }

    public enum IsRemoved {

        NO((byte) 0), YES((byte) 1);

        public final byte value;

        IsRemoved(byte value) {
            this.value = value;
        }
    }

    public enum StatusValues {

        DELETED((byte) 0), ACTIVE((byte) 1);

        public final byte value;

        StatusValues(byte value) {
            this.value = value;
        }
    }

    public enum IsEditedValues {

        NOT_EDITED((byte) 0), EDITED((byte) 1);

        public final byte value;

        IsEditedValues(byte value) {
            this.value = value;
        }
    }

}
