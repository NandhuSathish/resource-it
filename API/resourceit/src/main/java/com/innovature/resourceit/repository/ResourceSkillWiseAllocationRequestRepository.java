/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.repository;

import com.innovature.resourceit.entity.ResourceSkillWiseAllocationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author abdul.fahad
 */
public interface ResourceSkillWiseAllocationRequestRepository extends JpaRepository<ResourceSkillWiseAllocationRequest, Integer> {

    ResourceSkillWiseAllocationRequest findByIdAndApprovalFlowNotInAndStatus(Integer requestId, List<Byte> statusList, Byte status);

    List<ResourceSkillWiseAllocationRequest> findAllByProjectProjectIdAndStatusAndApprovalFlowNot(Integer projectId, Byte value, byte value1);

    List<ResourceSkillWiseAllocationRequest> findAllByProjectProjectId(Integer projectId);
}
