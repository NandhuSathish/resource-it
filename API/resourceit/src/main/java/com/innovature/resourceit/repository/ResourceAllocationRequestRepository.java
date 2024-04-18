package com.innovature.resourceit.repository;

import com.innovature.resourceit.entity.ResourceAllocationRequest;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResourceAllocationRequestRepository extends JpaRepository<ResourceAllocationRequest, Integer>, JpaSpecificationExecutor<ResourceAllocationRequest> {
    Page<ResourceAllocationRequest> findAll(Specification<ResourceAllocationRequest> specification, Pageable pageable);

    List<ResourceAllocationRequest> findByResourceIdAndApprovalFlowNotInAndStartDateBeforeAndEndDateAfterAndProjectProjectIdAndStatus(Integer resourceId, List<Byte> value, Date endDate, Date startDate, Integer projectId, byte status);

    List<ResourceAllocationRequest> findByResourceIdAndApprovalFlowNotInAndStartDateAndProjectProjectIdAndStatus(Integer resourceId, List<Byte> value, Date startDate, Integer projectId, byte status);

    List<ResourceAllocationRequest> findByResourceIdAndApprovalFlowNotInAndEndDateAndProjectProjectIdAndStatus(Integer resourceId, List<Byte> value, Date endDate, Integer projectId, byte status);

    ResourceAllocationRequest findByIdAndApprovalFlowNotInAndStatus(Integer requestId, List<Byte> value, Byte status);

    List<ResourceAllocationRequest> findByResourceIdAndStatus(Integer resourceId, Byte status);

    List<ResourceAllocationRequest> findAllByProjectProjectIdAndStatusAndApprovalFlowNotIn(Integer projectId, Byte value, List<Byte> value1);

    List<ResourceAllocationRequest> findAllByProjectProjectId(Integer projectId);

    List<ResourceAllocationRequest> findAllByAllocationId(Integer id);
}
