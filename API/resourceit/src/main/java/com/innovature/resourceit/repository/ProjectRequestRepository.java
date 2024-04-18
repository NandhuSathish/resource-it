package com.innovature.resourceit.repository;

import com.innovature.resourceit.entity.ProjectRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProjectRequestRepository extends JpaRepository<ProjectRequest, Integer>, JpaSpecificationExecutor<ProjectRequest> {
    Optional<ProjectRequest> findByName(String name);

    Optional<ProjectRequest> findByProjectRequestIdAndStatusNotAndApprovalStatus(Integer projectId, Byte status, Byte approvalStatus);

    Page<ProjectRequest> findAll(Specification<ProjectRequest> specification, Pageable pageable);

    List<ProjectRequest> findAllByProjectProjectId(Integer projectId);
}
