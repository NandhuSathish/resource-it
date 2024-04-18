package com.innovature.resourceit.service;

import com.innovature.resourceit.entity.dto.requestdto.AllocationRequestResourceFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.RejectionRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceAllocationRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceSkillWiseAllocationRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceSkillWiseAllocationRequestListDTO;
import com.innovature.resourceit.entity.dto.responsedto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface AllocationService {

    void addResourceWiseAllocationRequest(List<ResourceAllocationRequestDTO> dtoList);

    Page<ResourceAllocationResponseDTO> listResourceAllocationRequests(Set<Byte> approvalStatus, Set<Integer> departmentIds,
                                                                       Set<Integer> projectIds, Set<Integer> requestedBys, String searchKey, Boolean isRequestList, Pageable pageable);

    void saveSkillWiseResourceRequest(ResourceSkillWiseAllocationRequestDTO resourceSkillWiseAllocationRequestDTO);

    PagedResponseDTO<ResourceSkillWiseAllocationResponseListDTO> listSkillWiseResourceRequest(
            ResourceSkillWiseAllocationRequestListDTO resourceSkillWiseAllocationRequestListDTO, boolean isRequestList);

    PagedResponseDTO<AllocationRequestResourceFilterResponseDTO> listResourcesForAllocation(
            AllocationRequestResourceFilterRequestDTO requestDTO);

    void deleteRequestResourceWithSkill(String id);

    void resourceWiseAllocationEditRequest(ResourceAllocationRequestDTO dto);

    void approveResourceWiseAllocationRequest(Integer requestId);

    void rejectResourceWiseAllocationRequest(Integer requestId, RejectionRequestDTO dto);

    void approveByHODSkillWiseAllocationRequest(Integer requestId);

    void approveByHRSkillWiseAllocationRequest(Integer requestId, List<ResourceAllocationRequestDTO> dtoList);

    List<AllocationConflictsByResourceResponseDTO> getAllocationConflictList(List<Integer> resourceIdList,
                                                                             Date allocationStartDate, Date allocationEndDate, Integer allocationId);

    void rejectSkillWiseAllocationRequest(Integer requestId, RejectionRequestDTO dto);

    void deleteResourceWiseAllocationRequest(Integer requestId);

    PagedResponseDTO<AllocationResponseDTO> getAllocationListByProjectId(int projectId, String page, String size, Boolean isExpired);

    ResourceSkillWiseAllocationResponseListDTO getRequestResourceWithSkill(String id);

    void deleteAllocation(Integer id);
}
