package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.requestdto.*;
import com.innovature.resourceit.entity.dto.responsedto.*;
import com.innovature.resourceit.service.AllocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/allocation")
public class AllocationController {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AllocationService allocationService;
    private static final String ALLOCATION_REQUEST_APPROVED = "ALLOCATION_REQUEST_APPROVED";

    // resource wise allocation request
    @PostMapping("/request/resource")
    public ResponseEntity<Object> addResourceWiseAllocationRequest(
            @Valid @RequestBody List<ResourceAllocationRequestDTO> dtoList) {
        allocationService.addResourceWiseAllocationRequest(dtoList);
        String[] list = messageSource.getMessage("ALLOCATION_REQUEST_ADDED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    // to check if there is any other allocation exist in the same date range for a
    // list of resources.
    @GetMapping("/conflicts/list")
    public ResponseEntity<Object> getAllocationConflict(@RequestParam(name = "resourceId") List<Integer> resourceIdList,
                                                        @RequestParam(name = "allocationStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date allocationStartDate,
                                                        @RequestParam(name = "allocationEndDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date allocationEndDate,
                                                        @RequestParam(name = "allocationId", required = false) Integer allocationId
    ) {
        List<AllocationConflictsByResourceResponseDTO> conflictsResponseDTOS = allocationService
                .getAllocationConflictList(resourceIdList, allocationStartDate, allocationEndDate, allocationId);
        return new ResponseEntity<>(conflictsResponseDTOS, HttpStatus.OK);

    }

    @GetMapping("/request/resource")
    public ResponseEntity<PagedResponseDTO<ResourceAllocationResponseDTO>> listProjectRequests(
            @RequestParam(name = "pageNumber", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "20") int size,
            @RequestParam(name = "approvalStatus", required = false) Set<Byte> approvalStatus,
            @RequestParam(name = "projectIds", required = false) Set<Integer> projectIds,
            @RequestParam(name = "departmentIds", required = false) Set<Integer> departmentIds,
            @RequestParam(name = "requestedBys", required = false) Set<Integer> requestedBys,
            @RequestParam(name = "searchKey", required = false) String searchKey,
            @RequestParam(name = "sortDirection", defaultValue = "false") Boolean sortDirection,
            @RequestParam(name = "isRequestList", defaultValue = "false") Boolean isRequestList) {
        Pageable pageable = PageRequest.of(page, size,
                Boolean.TRUE.equals(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC, "createdDate");
        Page<ResourceAllocationResponseDTO> resultPage = allocationService.listResourceAllocationRequests(
                approvalStatus, departmentIds, projectIds,
                requestedBys, searchKey, isRequestList, pageable);

        List<ResourceAllocationResponseDTO> content = resultPage.getContent();
        int totalPages = resultPage.getTotalPages();
        long totalItems = resultPage.getTotalElements();
        int currentPage = resultPage.getNumber();

        PagedResponseDTO<ResourceAllocationResponseDTO> response = new PagedResponseDTO<>(content, totalPages,
                totalItems, currentPage);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/request/skill-wise")
    public ResponseEntity<Object> requestResourceWithSkill(
            @Valid @RequestBody ResourceSkillWiseAllocationRequestDTO resourceSkillWiseAllocationRequestDTO) {
        allocationService.saveSkillWiseResourceRequest(resourceSkillWiseAllocationRequestDTO);
        return new ResponseEntity<>(new SuccessResponseDTO("200", "Successfully requested"), HttpStatus.OK);
    }

    @PostMapping("/request/skill-wise/list")
    public ResponseEntity<Object> listRequestResourceWithSkill(
            @Valid @RequestBody ResourceSkillWiseAllocationRequestListDTO requestListDTO, @RequestParam(name = "isRequestList", defaultValue = "false") Boolean isRequestList) {
        PagedResponseDTO<ResourceSkillWiseAllocationResponseListDTO> pagedResponseDTO = allocationService
                .listSkillWiseResourceRequest(requestListDTO, isRequestList);
        return new ResponseEntity<>(pagedResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/request/resource/list")
    public ResponseEntity<Object> listResourcesForAllocation(
            @Valid @RequestBody AllocationRequestResourceFilterRequestDTO requestListDTO) {
        PagedResponseDTO<AllocationRequestResourceFilterResponseDTO> pagedResponseDTO = allocationService
                .listResourcesForAllocation(requestListDTO);
        return new ResponseEntity<>(pagedResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/request/skill-wise/{id}")
    public ResponseEntity<Object> getRequestResourceWithSkill(@PathVariable("id") String id) {
        return new ResponseEntity<>(allocationService.getRequestResourceWithSkill(id), HttpStatus.OK);
    }

    @DeleteMapping("/request/skill-wise/{id}")
    public ResponseEntity<Object> deleteRequestResourceWithSkill(@PathVariable("id") String id) {
        allocationService.deleteRequestResourceWithSkill(id);
        return new ResponseEntity<>(new SuccessResponseDTO("200", "Successfully deleted"), HttpStatus.OK);
    }

    @PutMapping("/request/resource")
    public ResponseEntity<Object> resourceWiseAllocationEditRequest(
            @Valid @RequestBody ResourceAllocationRequestDTO dto) {
        allocationService.resourceWiseAllocationEditRequest(dto);
        String[] list = messageSource.getMessage("ALLOCATION_EDIT_REQUEST_ADDED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @PutMapping("/request/resource/approve/{requestId}")
    public ResponseEntity<Object> approveResourceWiseAllocationRequest(@PathVariable Integer requestId) {
        allocationService.approveResourceWiseAllocationRequest(requestId);
        String[] list = messageSource.getMessage(ALLOCATION_REQUEST_APPROVED, null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @PutMapping("/request/resource/reject/{requestId}")
    public ResponseEntity<Object> rejectResourceWiseAllocationRequest(@PathVariable Integer requestId,
                                                                      @Valid @RequestBody RejectionRequestDTO dto) {
        allocationService.rejectResourceWiseAllocationRequest(requestId, dto);
        String[] list = messageSource.getMessage("ALLOCATION_REQUEST_REJECTED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @PutMapping("/request/skill-wise/approve/{requestId}")
    public ResponseEntity<Object> approveByHODSkillWiseAllocationRequest(@PathVariable Integer requestId) {
        allocationService.approveByHODSkillWiseAllocationRequest(requestId);
        String[] list = messageSource.getMessage(ALLOCATION_REQUEST_APPROVED, null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @PostMapping("/request/skill-wise/approve/{requestId}")
    public ResponseEntity<Object> approveByHRSkillWiseAllocationRequest(@PathVariable Integer requestId,
                                                                        @Valid @RequestBody List<ResourceAllocationRequestDTO> dtoList) {
        allocationService.approveByHRSkillWiseAllocationRequest(requestId, dtoList);
        String[] list = messageSource.getMessage(ALLOCATION_REQUEST_APPROVED, null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @PutMapping("/request/skill-wise/reject/{requestId}")
    public ResponseEntity<Object> rejectSkillWiseAllocationRequest(@PathVariable Integer requestId,
                                                                   @Valid @RequestBody RejectionRequestDTO dto) {
        allocationService.rejectSkillWiseAllocationRequest(requestId, dto);
        String[] list = messageSource.getMessage("ALLOCATION_REQUEST_REJECTED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @DeleteMapping("/request/resource/{requestId}")
    public ResponseEntity<Object> deleteResourceWiseAllocationRequest(@PathVariable Integer requestId) {
        allocationService.deleteResourceWiseAllocationRequest(requestId);
        String[] list = messageSource.getMessage("ALLOCATION_REQUEST_DELETED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    // For listing allocations based on project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Object> getAllocationDetailsByProjectId(@PathVariable Integer projectId, @RequestParam(name = "page", required = false) String page, @RequestParam(name = "size", required = false) String size, @RequestParam(name = "isExpired", defaultValue = "false") Boolean isExpired) {
        PagedResponseDTO<AllocationResponseDTO> allocationResponseDTOs = allocationService.getAllocationListByProjectId(projectId, page, size, isExpired);
        return new ResponseEntity<>(allocationResponseDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAllocation(@PathVariable Integer id) {
        allocationService.deleteAllocation(id);
        String[] list = messageSource.getMessage("ALLOCATION_DELETED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }
}


    
    
      

    
            
            
                