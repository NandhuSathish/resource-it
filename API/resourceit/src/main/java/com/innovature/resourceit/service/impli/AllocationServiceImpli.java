package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.criteriaquery.AllocationRepositoryCriteria;
import com.innovature.resourceit.entity.criteriaquery.ProjectRepositoryCriteria;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.*;
import com.innovature.resourceit.entity.dto.responsedto.*;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.AllocationService;
import com.innovature.resourceit.util.CommonFunctions;
import com.innovature.resourceit.util.EmailUtils;
import jakarta.persistence.criteria.Join;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class AllocationServiceImpli implements AllocationService {

    @Autowired
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;
    @Autowired
    private CommonFunctions commonFunctions;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ResourceRepository resourceRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(AllocationServiceImpli.class);

    @Autowired
    private ParameterValidator parameterValidator;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ResourceSkillRepository resourceSkillRepository;
    @Autowired
    private AllocationRepository allocationRepository;

    @Autowired
    ProjectRepositoryCriteria projectRepositoryCriteria;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    ResourceSkillWiseAllocationRequestRepository skillWiseAllocationRequestRepository;

    @Autowired
    AllocationRepositoryCriteria allocationRepositoryCriteria;

    @Autowired
    CommonServiceForResourceDownloadAndListingImpli listingImpli;
    @Autowired
    EmailUtils emailUtils;
    private static final String SKILL_WISE_ALLOCATION_LIST_FETCHING_FAILED = "SKILL_WISE_ALLOCATION_LIST_FETCHING_FAILED";
    private static final String PROJECT_ID = "projectId";
    private static final String CREATED_DATE = "createdDate";
    private static final String PROJECT_NOT_FOUND = "PROJECT_NOT_FOUND";
    private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String APPROVAL_FLAW = "approvalFlow";
    private static final String RESOURCE_ALLOCATION_LIST_FETCHING_FAILED = "RESOURCE_ALLOCATION_LIST_FETCHING_FAILED";
    private static final String DEPARTMENT_ID = "departmentId";
    private static final String ALLOCATION_NOT_FOUND = "ALLOCATION_NOT_FOUND";
    private static final String RESOURCE_ALLOCATION_REQUEST_NOT_FOUND = "RESOURCE_ALLOCATION_REQUEST_NOT_FOUND";
    private static final String ACCESS_DENIED = "ACCESS_DENIED";
    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PROJECT_MANAGER = "PROJECT MANAGER";

    @Override
    @Transactional
    public void addResourceWiseAllocationRequest(List<ResourceAllocationRequestDTO> dtoList) {
        Resource requestedBy = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        List<ResourceAllocationRequest> resourceAllocationRequestList = new ArrayList<>();
        if (dtoList.isEmpty()) {
            throw new BadRequestException(
                    messageSource.getMessage("REQUEST_LIST_CANNOT_BE_EMPTY", null, Locale.ENGLISH));
        }
        dtoList.forEach(
                dto -> {
                    if (Boolean.FALSE.equals(commonFunctions.checkRequestConflicts(
                            dto.getStartDate(), dto.getEndDate(), dto.getResourceId(),
                            dto.getProjectId()))) {
                        throw new BadRequestException(messageSource.getMessage(
                                "CONFLICTS_IN_THE_REQUESTS", null, Locale.ENGLISH));
                    }
                    resourceAllocationRequestList.add(new ResourceAllocationRequest(dto,
                            requestedBy, projectRepository));
                });
        List<ResourceAllocationRequest> resourceAllocationRequests = resourceAllocationRequestRepository
                .saveAll(resourceAllocationRequestList);
        if (requestedBy.getRole().getName().equals("HOD")) {
            this.emailUtils.allocationMailNotification(
                    resourceRepository.findAllIdByRoleIdAndStatus(
                            Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value),
                    1,
                    Optional.empty(),
                    resourceAllocationRequests);
        } else {
            this.emailUtils.allocationMailNotification(
                    resourceRepository.findAllIdByRoleIdAndStatus(
                            Resource.Roles.HOD.getId(), Resource.Status.ACTIVE.value),
                    1,
                    Optional.empty(),
                    resourceAllocationRequests);
        }
    }

    @Override
    public List<AllocationConflictsByResourceResponseDTO> getAllocationConflictList(
            List<Integer> resourceIdList,
            Date allocationStartDate,
            Date allocationEndDate,
            Integer allocationId) {
        resourceIdList.forEach(
                resourceId -> {
                    Optional<Resource> resource = resourceRepository.findByIdAndStatus(resourceId,
                            Resource.Status.ACTIVE.value);
                    if (resource.isEmpty()) {
                        throw new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH));
                    }
                });
        return commonFunctions.checkAllocationConflicts(
                resourceIdList, allocationStartDate, allocationEndDate, allocationId);
    }

    @Override
    public Page<ResourceAllocationResponseDTO> listResourceAllocationRequests(
            Set<Byte> approvalStatus,
            Set<Integer> departmentIds,
            Set<Integer> projectIds,
            Set<Integer> requestedBys,
            String searchKey,
            Boolean isRequestList,
            Pageable pageable) {
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        try {
            if (Boolean.TRUE.equals(isRequestList)) {
                if (requestedBys != null) {
                    requestedBys.clear();
                } else {
                    requestedBys = new HashSet<>();
                }
                requestedBys.add(currentUser.getId());
            }
            Specification<ResourceAllocationRequest> specification = buildSpecification(
                    approvalStatus,
                    departmentIds,
                    projectIds,
                    requestedBys,
                    searchKey,
                    isRequestList,
                    currentUser);
            Page<ResourceAllocationRequest> resourceAllocationRequests = resourceAllocationRequestRepository
                    .findAll(specification, pageable);

            return resourceAllocationRequests.map(this::convertToResourceAllocationResponseDto);
        } catch (Exception e) {
            LOGGER.error(
                    messageSource.getMessage(
                            "UNEXPECTED_ERROR_WHILE_LISTING_ALLOCATION_REQUESTS", null,
                            Locale.ENGLISH));
            throw new BadRequestException(
                    messageSource.getMessage(
                            "UNEXPECTED_ERROR_WHILE_LISTING_ALLOCATION_REQUESTS", null,
                            Locale.ENGLISH));
        }
    }

    public Specification<ResourceAllocationRequest> buildSpecification(
            Set<Byte> approvalStatus,
            Set<Integer> departmentIds,
            Set<Integer> projectIds,
            Set<Integer> requestedBys,
            String searchKey,
            Boolean isRequestList,
            Resource currentUser) {
        Specification<ResourceAllocationRequest> approvalStatusSpecification = buildApprovalStatusSpecification(
                approvalStatus, isRequestList, currentUser);
        Specification<ResourceAllocationRequest> departmentSpecification = buildDepartmentSpecification(
                departmentIds);
        Specification<ResourceAllocationRequest> projectSpecification = buildProjectSpecification(projectIds);
        Specification<ResourceAllocationRequest> requestedBySpecification = buildRequestedBySpecification(
                requestedBys);
        Specification<ResourceAllocationRequest> searchSpecification = buildSearchSpecification(searchKey);
        Specification<ResourceAllocationRequest> statusSpecification = buildStatusSpecification();

        return Specification.where(approvalStatusSpecification)
                .and(departmentSpecification)
                .and(projectSpecification)
                .and(requestedBySpecification)
                .and(searchSpecification)
                .and(statusSpecification);
    }

    public Specification<ResourceAllocationRequest> buildApprovalStatusSpecification(
            Set<Byte> approvalStatus, Boolean isRequestList, Resource currentUser) {
        List<Byte> approvalStatusList = new ArrayList<>();
        if (approvalStatus != null && !approvalStatus.isEmpty()) {
            handleApprovalStatus(approvalStatusList, approvalStatus, isRequestList, currentUser);
        } else if (Boolean.TRUE.equals(isRequestList)) {
            handleIsRequestList(approvalStatusList, currentUser);
        } else {
            handleIsNotRequestList(approvalStatusList, currentUser);
        }
        return (root, query, criteriaBuilder) -> {
            if (!approvalStatusList.isEmpty()) {
                return criteriaBuilder.and(root.get(APPROVAL_FLAW).in(approvalStatusList));
            }
            return null;
        };
    }

    private void handleApprovalStatus(
            List<Byte> approvalStatusList,
            Set<Byte> approvalStatus,
            Boolean isRequestList,
            Resource currentUser) {
        String roleName = currentUser.getRole().getName();
        if (Boolean.TRUE.equals(isRequestList)) {
            handleApprovalStatusForIsRequestList(approvalStatusList, approvalStatus, roleName);
        } else {
            handleApprovalStatusForIsNotRequestList(approvalStatusList, approvalStatus, roleName);
        }
    }

    public void handleIsRequestList(List<Byte> approvalStatusList, Resource currentUser) {
        String roleName = currentUser.getRole().getName();
        switch (roleName) {
            case "HR":
                approvalStatusList.addAll(
                        List.of(
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HR.value,
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value));
                break;
            case PROJECT_MANAGER:
                approvalStatusList.addAll(
                        List.of(
                                ResourceAllocationRequest.ApprovalFlowValues.PENDING.value,
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value,
                                ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value));
                break;
            case "HOD":
                approvalStatusList.addAll(
                        List.of(
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value,
                                ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value));
                break;
            default:
                break;
        }
    }

    public void handleIsNotRequestList(List<Byte> approvalStatusList, Resource currentUser) {
        String roleName = currentUser.getRole().getName();
        switch (roleName) {
            case "HOD":
                approvalStatusList.addAll(
                        List.of(
                                ResourceAllocationRequest.ApprovalFlowValues.PENDING.value,
                                ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value,
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value,
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HR.value));
                break;
            case "HR":
                approvalStatusList.addAll(
                        List.of(
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value,
                                ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value));
                break;
            default:
                break;
        }
    }

    private void handleApprovalStatusForIsRequestList(
            List<Byte> approvalStatusList, Set<Byte> approvalStatus, String roleName) {
        if (PROJECT_MANAGER.equals(roleName) && approvalStatus.contains((byte) 0)) {
            approvalStatusList.addAll(
                    List.of(
                            ResourceAllocationRequest.ApprovalFlowValues.PENDING.value,
                            ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value));
        } else if ("HOD".equals(roleName) && approvalStatus.contains((byte) 0)) {
            approvalStatusList.add(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);
        } else if ("HR".equals(roleName) && approvalStatus.contains((byte) 0)) {
            approvalStatusList.add(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HR.value);
        }
        if (approvalStatus.contains((byte) 1)) {
            approvalStatusList.add(ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value);
        }
        if (approvalStatus.contains((byte) 2)) {
            approvalStatusList.add(ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value);
        }
    }

    private void handleApprovalStatusForIsNotRequestList(
            List<Byte> approvalStatusList, Set<Byte> approvalStatus, String roleName) {
        if ("HOD".equals(roleName) && approvalStatus.contains((byte) 0)) {
            approvalStatusList.addAll(
                    List.of(
                            ResourceAllocationRequest.ApprovalFlowValues.PENDING.value,
                            ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HR.value));
        } else if ("HR".equals(roleName) && approvalStatus.contains((byte) 0)) {
            approvalStatusList.add(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);
        }
        if (approvalStatus.contains((byte) 1)) {
            if ("HOD".equals(roleName)) {
                approvalStatusList.addAll(
                        List.of(
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value));
            } else {
                approvalStatusList.add(ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value);
            }
        }
        if (approvalStatus.contains((byte) 2)) {
            approvalStatusList.add(ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value);
        }
    }

    private Specification<ResourceAllocationRequest> buildProjectSpecification(
            Set<Integer> projectIds) {
        return (root, query, criteriaBuilder) -> {
            if (projectIds != null && !projectIds.isEmpty()) {
                return criteriaBuilder.and(root.join("project").get("id").in(projectIds));
            }
            return null;
        };
    }

    private Specification<ResourceAllocationRequest> buildStatusSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("status"), ResourceAllocationRequest.StatusValues.ACTIVE.value);
    }

    private Specification<ResourceAllocationRequest> buildDepartmentSpecification(
            Set<Integer> departmentIds) {
        return (root, query, criteriaBuilder) -> {
            if (departmentIds != null && !departmentIds.isEmpty()) {
                return criteriaBuilder.and(
                        root.join("resource").get("department").get(DEPARTMENT_ID)
                                .in(departmentIds));
            }
            return null;
        };
    }

    private Specification<ResourceAllocationRequest> buildRequestedBySpecification(
            Set<Integer> requestedBys) {
        return (root, query, criteriaBuilder) -> {
            if (requestedBys != null && !requestedBys.isEmpty()) {
                return criteriaBuilder.and(root.join("requestedBy").get("id").in(requestedBys));
            }
            return null;
        };
    }

    private Specification<ResourceAllocationRequest> buildSearchSpecification(String searchKey) {
        return (root, query, criteriaBuilder) -> {
            if (searchKey != null && !searchKey.isEmpty()) {
                Join<ResourceAllocationRequest, Resource> resourceJoin = root.join("resource");
                return criteriaBuilder.like(resourceJoin.get("name"), "%" + searchKey + "%");
            }
            return null;
        };
    }

    public ResourceAllocationResponseDTO convertToResourceAllocationResponseDto(
            ResourceAllocationRequest resourceAllocationRequest) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ResourceAllocationResponseDTO responseDTO = new ResourceAllocationResponseDTO();
        responseDTO.setId(resourceAllocationRequest.getId());
        responseDTO.setAllocationId(
                resourceAllocationRequest.getAllocation() != null
                        ? resourceAllocationRequest.getAllocation().getId()
                        : null);
        responseDTO.setProjectId(resourceAllocationRequest.getProject().getProjectId());
        responseDTO.setProjectCode(resourceAllocationRequest.getProject().getProjectCode());
        responseDTO.setProjectName(resourceAllocationRequest.getProject().getName());
        responseDTO.setResourceId(resourceAllocationRequest.getResource().getId());
        responseDTO.setResourceName(resourceAllocationRequest.getResource().getName());
        responseDTO.setResourceEmployeeId(
                resourceAllocationRequest.getResource().getEmployeeId().toString());
        responseDTO.setDepartmentId(
                resourceAllocationRequest.getResource().getDepartment().getDepartmentId());
        responseDTO.setDepartmentName(
                resourceAllocationRequest.getResource().getDepartment().getName());
        responseDTO.setRequestedByEmployeeId(
                resourceAllocationRequest.getRequestedBy().getEmployeeId().toString());
        responseDTO.setRequestedByName(resourceAllocationRequest.getRequestedBy().getName());
        responseDTO.setStartDate(dateFormat.format(resourceAllocationRequest.getStartDate()));
        responseDTO.setEndDate(dateFormat.format(resourceAllocationRequest.getEndDate()));
        responseDTO.setRejectionReason(resourceAllocationRequest.getRejectionReason());
        responseDTO.setApprovalFlow(resourceAllocationRequest.getApprovalFlow());
        Integer conflictDays = this.commonFunctions.calculateConflictDays(
                resourceAllocationRequest.getResource().getId(),
                resourceAllocationRequest.getAllocation(),
                resourceAllocationRequest.getStartDate(),
                resourceAllocationRequest.getEndDate());
        responseDTO.setConflictDays(conflictDays);
        if (resourceAllocationRequest
                .getApprovalFlow()
                .equals(ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value)) {
            responseDTO.setConflictDays(0);
        } else {
            responseDTO.setConflictDays(conflictDays);
        }
        responseDTO.setCreatedDate(dateFormat.format(resourceAllocationRequest.getCreatedDate()));
        responseDTO.setUpdatedDate(dateFormat.format(resourceAllocationRequest.getUpdatedDate()));
        return responseDTO;
    }

    @Override
    public void saveSkillWiseResourceRequest(
            ResourceSkillWiseAllocationRequestDTO resourceSkillWiseAllocationRequestDTO) {
        Optional<Resource> resourceOptional = resourceRepository.findByEmailAndStatus(
                SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value);

        Resource currentUser = resourceOptional.orElseThrow(
                () -> new BadRequestException(
                        messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));

        List<ResourceFilterSkillAndExperienceRequestDTO> skillExperienceRequestDTOs = resourceSkillWiseAllocationRequestDTO
                .getSkillExperienceRequestDTO();
        ResourceSkillWiseAllocationRequest resourceSkillWiseAllocationRequest = new ResourceSkillWiseAllocationRequest();
        Set<SkillExperience> skillExperiences = new HashSet<>();

        if (skillExperienceRequestDTOs != null && !skillExperienceRequestDTOs.isEmpty()) {
            for (ResourceFilterSkillAndExperienceRequestDTO newSkillAndExperience : skillExperienceRequestDTOs) {
                if (newSkillAndExperience.getSkillId() == null
                        || newSkillAndExperience.getSkillMinValue() == null
                        || newSkillAndExperience.getSkillMaxValue() == null) {
                    throw new BadRequestException(
                            messageSource.getMessage("SKILL_EXPERIENCE_REQUIRED", null,
                                    Locale.ENGLISH));
                }
                int skillId = parameterValidator.isNumber("skillId",
                        newSkillAndExperience.getSkillId());
                int skillMinValue = parameterValidator.isNumber("skillMinValue",
                        newSkillAndExperience.getSkillMinValue());
                int skillMaxValue = parameterValidator.isNumber("skillMaxValue",
                        newSkillAndExperience.getSkillMaxValue());
                Optional<Skill> optSkill = skillRepository.findById(skillId);
                if (optSkill.isEmpty()) {
                    throw new BadRequestException(
                            messageSource.getMessage("SKILL_NOT_FOUND", null,
                                    Locale.ENGLISH));
                }
                skillExperiences.add(
                        new SkillExperience(skillId, skillMinValue, skillMaxValue,
                                optSkill.get().getName(), newSkillAndExperience.getProficiency()));
            }
        }

        resourceSkillWiseAllocationRequest.setSkillExperiences(skillExperiences);

        int projectId = parameterValidator.isNumber(
                PROJECT_ID, resourceSkillWiseAllocationRequestDTO.getProjectId());
        int departmentId = parameterValidator.isNumber(
                DEPARTMENT_ID, resourceSkillWiseAllocationRequestDTO.getDepartmentId());
        int experience = parameterValidator.isNumber(
                "experience", resourceSkillWiseAllocationRequestDTO.getExperience());
        int count = parameterValidator.isNumber(
                "resourceCount", resourceSkillWiseAllocationRequestDTO.getCount());
        Optional<Project> optProject = projectRepository.findById(projectId);
        if (optProject.isEmpty()) {
            throw new BadRequestException(
                    messageSource.getMessage(PROJECT_NOT_FOUND, null, Locale.ENGLISH));
        }
        resourceSkillWiseAllocationRequest.setProject(optProject.get());
        resourceSkillWiseAllocationRequest.setExperience(experience);
        resourceSkillWiseAllocationRequest.setCreatedDate(new Date());
        resourceSkillWiseAllocationRequest.setUpdatedDate(new Date());
        resourceSkillWiseAllocationRequest.setResourceCount(count);
        Optional<Department> optDepartment = validateDepartment(departmentId);

        resourceSkillWiseAllocationRequest.setDepartment(optDepartment.get());
        Date startDate = listingImpli
                .getDateFromStringHyphen(resourceSkillWiseAllocationRequestDTO.getStartDate());
        Date endDate = listingImpli.getDateFromStringHyphen(resourceSkillWiseAllocationRequestDTO.getEndDate());
        resourceSkillWiseAllocationRequest.setStartDate(startDate);
        resourceSkillWiseAllocationRequest.setEndDate(endDate);
        resourceSkillWiseAllocationRequest.setRequestedBy(currentUser);
        resourceSkillWiseAllocationRequest.setRejectedBy(null);
        if (currentUser.getRole().getName().equals(Resource.Roles.HOD.getValue())) {
            resourceSkillWiseAllocationRequest.setApprovalFlow(
                    ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);
        }
        resourceSkillWiseAllocationRequest = skillWiseAllocationRequestRepository
                .save(resourceSkillWiseAllocationRequest);
        if (currentUser.getRole().getName().equals("HOD")) {
            this.emailUtils.allocationMailNotification(
                    resourceRepository.findAllIdByRoleIdAndStatus(
                            Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value),
                    2,
                    Optional.of(resourceSkillWiseAllocationRequest),
                    new ArrayList<>());
        } else {
            this.emailUtils.allocationMailNotification(
                    resourceRepository.findAllIdByRoleIdAndStatus(
                            Resource.Roles.HOD.getId(), Resource.Status.ACTIVE.value),
                    2,
                    Optional.of(resourceSkillWiseAllocationRequest),
                    new ArrayList<>());
        }
    }

    private Optional<Department> validateDepartment(int departmentId) {
        Optional<Department> optDepartment = departmentRepository.findById(departmentId);
        if (optDepartment.isEmpty()) {
            throw new BadRequestException(
                    messageSource.getMessage("DEPARTMENT_NOT_FOUND", null, Locale.ENGLISH));
        }
        return optDepartment;
    }

    @Override
    public PagedResponseDTO<ResourceSkillWiseAllocationResponseListDTO> listSkillWiseResourceRequest(
            ResourceSkillWiseAllocationRequestListDTO requestDTO, boolean isRequestList) {

        Optional<Resource> resourceOptional = resourceRepository.findByEmailAndStatus(
                SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value);

        Resource currentUser = resourceOptional.orElseThrow(
                () -> new BadRequestException(
                        messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));

        int pageNumber = parameterValidator.isNumber(PAGE_NUMBER, requestDTO.getPageNumber());
        int pageSize = parameterValidator.isNumber(PAGE_SIZE, requestDTO.getPageSize());
        List<Integer> departmentIdInt = parameterValidator.isNumbersNum(DEPARTMENT_ID,
                requestDTO.getDepartmentIds());
        List<Integer> approvalStatusInt = parameterValidator.isNumbersNum("approvalStatus",
                requestDTO.getApprovalStatus());
        List<Integer> managerIdInt = parameterValidator.isNumbersNum("managerId", requestDTO.getManagerIds());
        List<Integer> newApprovalStatus = new ArrayList<>();
        if (approvalStatusInt != null && approvalStatusInt.contains(0)) {
            if (isRequestList) {
                handleIsRequestListSkillWise(newApprovalStatus, approvalStatusInt, currentUser);
                newApprovalStatus.add(3);
            } else {
                switch (currentUser.getRole().getName()) {
                    case "HOD" ->
                            addApprovalStatusWhenHODAndApprovalStatusContainsPendingStatus(newApprovalStatus, approvalStatusInt);
                    case "HR" -> {
                        newApprovalStatus = new ArrayList<>(approvalStatusInt);
                        newApprovalStatus.remove(Integer.valueOf(0));
                        newApprovalStatus.add(3);
                    }
                    default -> throw new BadRequestException(
                            messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
                }
            }

        } else if (approvalStatusInt != null && approvalStatusInt.contains(1)) {
            newApprovalStatus = addApprovalStatusWhenHODAndApprovalStatusOne(approvalStatusInt, newApprovalStatus, currentUser, isRequestList);
        } else if (approvalStatusInt != null && !approvalStatusInt.isEmpty()) {
            newApprovalStatus.addAll(approvalStatusInt);
        } else {
            newApprovalStatus = allocationFlowNull(isRequestList, currentUser);
        }

        List<Integer> projectIdsInt = parameterValidator.isNumbersNum(PROJECT_ID, requestDTO.getProjectIds());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, CREATED_DATE));
        List<ResourceSkillWiseAllocationResponseListDTO> listDTOs;
        try {

            Page<ResourceSkillWiseAllocationRequest> pagedList = projectRepositoryCriteria
                    .findFilteredResourceSkillWiseAllocationWithPagination(
                            newApprovalStatus, projectIdsInt, departmentIdInt, managerIdInt,
                            pageable);
            listDTOs = pagedList.getContent().stream()
                    .map(ResourceSkillWiseAllocationResponseListDTO::new)
                    .toList();
            return new PagedResponseDTO<>(
                    listDTOs, pagedList.getTotalPages(), pagedList.getTotalElements(),
                    pagedList.getNumber());
        } catch (Exception e) {
            LOGGER.error(
                    messageSource.getMessage(
                            SKILL_WISE_ALLOCATION_LIST_FETCHING_FAILED, null,
                            Locale.ENGLISH));
            throw new BadRequestException(
                    messageSource.getMessage(
                            SKILL_WISE_ALLOCATION_LIST_FETCHING_FAILED, null,
                            Locale.ENGLISH));
        }
    }

    private void addApprovalStatusWhenHODAndApprovalStatusContainsPendingStatus(List<Integer> newApprovalStatus, List<Integer> approvalStatusInt) {
        newApprovalStatus.addAll(approvalStatusInt);
        if (approvalStatusInt.contains(1)) {
            newApprovalStatus.add(3);
        }
    }

    private List<Integer> addApprovalStatusWhenHODAndApprovalStatusOne(List<Integer> approvalStatusInt, List<Integer> newApprovalStatus, Resource currentUser, boolean isRequestList) {
        if (isRequestList) {
            newApprovalStatus.addAll(approvalStatusInt);
        } else {

            switch (currentUser.getRole().getName()) {
                case "HOD" -> {
                    newApprovalStatus.add(3);
                    newApprovalStatus.addAll(approvalStatusInt);
                }

                case "HR" -> newApprovalStatus = new ArrayList<>(approvalStatusInt);
                default -> throw new BadRequestException(
                        messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
            }
        }
        return newApprovalStatus;
    }

    private void handleIsRequestListSkillWise(
            List<Integer> newApprovalStatus, List<Integer> approvalStatusInt, Resource currentUser) {
        if (currentUser.getRole().getName().equals("HOD")) {
            newApprovalStatus = new ArrayList<>(approvalStatusInt);
            newApprovalStatus.remove(Integer.valueOf(0));
        } else {
            newApprovalStatus.addAll(approvalStatusInt);
        }
    }

    public List<Integer> allocationFlowNull(boolean isRequestList, Resource currentUser) {
        List<Integer> newApprovalStatus = new ArrayList<>();
        if (isRequestList) {
            switch (currentUser.getRole().getName()) {
                case "HOD", "HR" -> newApprovalStatus.addAll(
                        Arrays.asList(
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.REJECTED.value,
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value));
                case PROJECT_MANAGER -> newApprovalStatus.addAll(
                        Arrays.asList(
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.PENDING.value,
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.REJECTED.value,
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value));
                default -> throw new BadRequestException(
                        messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
            }

        } else {
            switch (currentUser.getRole().getName()) {
                case "HOD" -> newApprovalStatus.addAll(
                        Arrays.asList(
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.PENDING.value,
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.REJECTED.value,
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value));
                case "HR" -> newApprovalStatus.addAll(
                        Arrays.asList(
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.REJECTED.value,
                                (int) ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value));
                default -> throw new BadRequestException(
                        messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
            }
        }
        return newApprovalStatus;
    }

    @Override
    public PagedResponseDTO<AllocationRequestResourceFilterResponseDTO> listResourcesForAllocation(
            AllocationRequestResourceFilterRequestDTO requestDTO) {
        listingImpli.checkLowerExpLessThanHighExp(
                requestDTO.getExperienceMinValue(), requestDTO.getExperienceMaxValue());
        try {
            List<Resource> resourceList = allocationRepositoryCriteria
                    .findFilteredResourceAllocationWithPagination(
                            requestDTO);
            List<AllocationRequestResourceFilterResponseDTO> listDTOs = new ArrayList<>();
            resourceList.forEach(
                    resource -> {
                        AllocationRequestResourceFilterResponseDTO filterResponseDTO = new AllocationRequestResourceFilterResponseDTO();
                        filterResponseDTO.setCode(resource.getEmployeeId());
                        filterResponseDTO.setId(resource.getId());
                        int conflictDays = this.commonFunctions.calculateConflictDays(
                                resource.getId(),
                                null,
                                requestDTO.getAllocationStartDate(),
                                requestDTO.getAllocationEndDate());
                        filterResponseDTO.setName(resource.getName());
                        filterResponseDTO.setDepartmentName(resource.getDepartment().getName());
                        filterResponseDTO.setConflictDays(conflictDays);
                        filterResponseDTO.setJoiningDate(resource.getJoiningDate());
                        filterResponseDTO.setExperience(resource.getExperience());
                        List<ResourceSkill> rs = resourceSkillRepository
                                .findAllByResourceId(resource.getId());
                        List<ResourceSkill> sortedResourceSkills =
                                rs.stream()
                                        .sorted(Comparator.comparing(ResourceSkill::getExperience).reversed())
                                        .toList();

                        filterResponseDTO.setResourceSkillResponseDTOs(
                                sortedResourceSkills.stream().map(ResourceSkillResponseDTO::new)
                                        .toList());
                        listDTOs.add(filterResponseDTO);
                    });
            List<AllocationRequestResourceFilterResponseDTO> filteredList = new ArrayList<>(
                    listDTOs.stream().filter(dto -> dto.getConflictDays() == 0).toList());
            filteredList.sort(
                    Comparator.comparing(AllocationRequestResourceFilterResponseDTO::getJoiningDate)
                            .reversed());
            int start = requestDTO.getPageNumber() * requestDTO.getPageSize();
            int end = Math.min(start + requestDTO.getPageSize(), filteredList.size());
            List<AllocationRequestResourceFilterResponseDTO> pagedList = filteredList.subList(start, end);

            return new PagedResponseDTO<>(
                    pagedList,
                    (int) Math.ceil((double) filteredList.size() / requestDTO.getPageSize()),
                    filteredList.size(),
                    requestDTO.getPageNumber());
        } catch (Exception p) {
            LOGGER.error(
                    messageSource.getMessage(RESOURCE_ALLOCATION_LIST_FETCHING_FAILED, null,
                            Locale.ENGLISH));
            throw new BadRequestException(
                    messageSource.getMessage(RESOURCE_ALLOCATION_LIST_FETCHING_FAILED, null,
                            Locale.ENGLISH));
        }
    }

    @Override
    public void deleteRequestResourceWithSkill(String id) {
        if (id == null) {
            throw new BadRequestException(messageSource.getMessage("ID_REQUIRED", null, Locale.ENGLISH));
        }

        int idInt = parameterValidator.isNumber("id", id);

        Optional<Resource> resourceOptional = resourceRepository.findByEmailAndStatus(
                SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value);

        Resource currentUser = resourceOptional.orElseThrow(
                () -> new BadRequestException(
                        messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        Optional<ResourceSkillWiseAllocationRequest> optionalRSWAR = skillWiseAllocationRequestRepository
                .findById(idInt);
        if (!optionalRSWAR.isPresent()) {
            throw new BadRequestException(
                    messageSource.getMessage(
                            "RESOURCE_SKILL_WISE_ALLOCATION_REQUEST_NOT_FOUND", null,
                            Locale.ENGLISH));
        }
        if ((Objects.equals(optionalRSWAR.get().getRequestedBy().getId(), currentUser.getId()))) {
            ResourceSkillWiseAllocationRequest resourceSkillWiseAllocationRequest = optionalRSWAR.get();
            resourceSkillWiseAllocationRequest.setStatus(
                    ResourceSkillWiseAllocationRequest.StatusValues.DELETED.value);
            if (currentUser.getRole().getName().equals(Resource.Roles.HOD.getValue())
                    && optionalRSWAR.get()
                    .getApprovalFlow() == ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value) {
                skillWiseAllocationRequestRepository.save(resourceSkillWiseAllocationRequest);
            } else if (currentUser.getRole().getName().equals(Resource.Roles.PM.getValue())
                    && optionalRSWAR.get()
                    .getApprovalFlow() == ResourceSkillWiseAllocationRequest.ApprovalFlowValues.PENDING.value) {
                skillWiseAllocationRequestRepository.save(resourceSkillWiseAllocationRequest);
            } else if (currentUser.getRole().getName().equals(Resource.Roles.DH.getValue())
                    && optionalRSWAR.get()
                    .getApprovalFlow() == ResourceSkillWiseAllocationRequest.ApprovalFlowValues.PENDING.value) {
                skillWiseAllocationRequestRepository.save(resourceSkillWiseAllocationRequest);
            } else {
                throw new BadRequestException(
                        messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
            }

        } else {
            throw new BadRequestException(messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
        }
    }

    @Override
    public void resourceWiseAllocationEditRequest(ResourceAllocationRequestDTO dto) {
        Resource requestedBy = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        if (dto.getAllocationId() == null) {
            throw new BadRequestException(
                    messageSource.getMessage("ALLOCATION_ID_CANNOT_BE_NULL", null, Locale.ENGLISH));
        }
        Optional<Allocation> allocation = allocationRepository.findByIdAndStatus(
                dto.getAllocationId(), Allocation.StatusValues.ACTIVE.value);
        if (allocation.isEmpty()) {
            throw new BadRequestException(
                    messageSource.getMessage(ALLOCATION_NOT_FOUND, null, Locale.ENGLISH));
        }
        allocation.get().setIsEdited((byte) 1);
        allocationRepository.save(allocation.get());
        ResourceAllocationRequest resourceAllocationRequest = resourceAllocationRequestRepository
                .save(new ResourceAllocationRequest(dto, requestedBy, projectRepository));
        if (requestedBy.getRole().getName().equals("HOD")) {
            this.emailUtils.allocationMailNotification(
                    resourceRepository.findAllIdByRoleIdAndStatus(
                            Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value),
                    3,
                    Optional.empty(),
                    Collections.singletonList(resourceAllocationRequest));
        } else {
            this.emailUtils.allocationMailNotification(
                    resourceRepository.findAllIdByRoleIdAndStatus(
                            Resource.Roles.HOD.getId(), Resource.Status.ACTIVE.value),
                    3,
                    Optional.empty(),
                    Collections.singletonList(resourceAllocationRequest));
        }
    }

    @Override
    public void approveResourceWiseAllocationRequest(Integer requestId) {
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        ResourceAllocationRequest request = resourceAllocationRequestRepository
                .findByIdAndApprovalFlowNotInAndStatus(
                        requestId,
                        List.of(
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value),
                        ResourceAllocationRequest.StatusValues.ACTIVE.value);
        if (request == null) {
            throw new BadRequestException(
                    messageSource.getMessage(RESOURCE_ALLOCATION_REQUEST_NOT_FOUND, null,
                            Locale.ENGLISH));
        }
        if (currentUser.getRole().getName().equals(Resource.Roles.HOD.getValue())) {
            this.approvalByHODResourceWise(request);
        } else if (currentUser.getRole().getName().equals(Resource.Roles.HR.getValue())) {
            this.approvalByHRResourceWise(request);
        } else {
            throw new BadRequestException(messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
        }
        this.commonFunctions.updateAllocationStatusExpiryAndTeamSize();
    }

    @Override
    public void rejectResourceWiseAllocationRequest(Integer requestId, RejectionRequestDTO dto) {
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        ResourceAllocationRequest request = resourceAllocationRequestRepository
                .findByIdAndApprovalFlowNotInAndStatus(
                        requestId,
                        List.of(
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value,
                                ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value),
                        ResourceAllocationRequest.StatusValues.ACTIVE.value);
        if (request == null) {
            throw new BadRequestException(
                    messageSource.getMessage(RESOURCE_ALLOCATION_REQUEST_NOT_FOUND, null,
                            Locale.ENGLISH));
        }
        if (!(currentUser.getRole().getName().equals(Resource.Roles.HOD.getValue())
                || currentUser.getRole().getName().equals(Resource.Roles.HR.getValue()))) {
            throw new BadRequestException(messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
        }
        if (request.getAllocation() != null) {
            Allocation allocation = allocationRepository
                    .findByIdAndStatus(
                            request.getAllocation().getId(),
                            Allocation.StatusValues.ACTIVE.value)
                    .orElseThrow(
                            () -> new BadRequestException(
                                    messageSource.getMessage(ALLOCATION_NOT_FOUND,
                                            null, Locale.ENGLISH)));
            allocation.setIsEdited((byte) 0);
            allocationRepository.save(allocation);
        }
        request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value);
        request.setRejectedBy(currentUser);
        request.setRejectionReason(dto.getMessage());
        resourceAllocationRequestRepository.save(request);
        this.emailUtils.allocationMailNotification(
                List.of(request.getRequestedBy().getId()),
                6,
                Optional.empty(),
                Collections.singletonList(request));
    }

    @Override
    public void approveByHODSkillWiseAllocationRequest(Integer requestId) {
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        ResourceSkillWiseAllocationRequest request = skillWiseAllocationRequestRepository
                .findByIdAndApprovalFlowNotInAndStatus(
                        requestId,
                        List.of(
                                ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value,
                                ResourceSkillWiseAllocationRequest.ApprovalFlowValues.REJECTED.value,
                                ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED.value),
                        ResourceSkillWiseAllocationRequest.StatusValues.ACTIVE.value);
        if (request == null) {
            throw new BadRequestException(
                    messageSource.getMessage(RESOURCE_ALLOCATION_REQUEST_NOT_FOUND, null,
                            Locale.ENGLISH));
        }
        if (!currentUser.getRole().getName().equals(Resource.Roles.HOD.getValue())) {
            throw new BadRequestException(messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
        }
        request.setApprovalFlow(
                ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);
        skillWiseAllocationRequestRepository.save(request);
        this.emailUtils.allocationMailNotification(
                resourceRepository.findAllIdByRoleIdAndStatus(
                        Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value),
                2,
                Optional.of(request),
                new ArrayList<>());
    }

    @Override
    @Transactional
    public void approveByHRSkillWiseAllocationRequest(
            Integer requestId, List<ResourceAllocationRequestDTO> dtoList) {
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        ResourceSkillWiseAllocationRequest request = skillWiseAllocationRequestRepository
                .findByIdAndApprovalFlowNotInAndStatus(
                        requestId,
                        List.of(
                                ResourceSkillWiseAllocationRequest.ApprovalFlowValues.REJECTED.value,
                                ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED.value),
                        ResourceSkillWiseAllocationRequest.StatusValues.ACTIVE.value);
        List<Allocation> resourceAllocationList = new ArrayList<>();
        if (request == null) {
            throw new BadRequestException(
                    messageSource.getMessage(RESOURCE_ALLOCATION_REQUEST_NOT_FOUND, null,
                            Locale.ENGLISH));
        }
        if (!currentUser.getRole().getName().equals(Resource.Roles.HR.getValue())) {
            throw new BadRequestException(messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
        }
        if (dtoList.isEmpty()) {
            throw new BadRequestException(
                    messageSource.getMessage("REQUEST_LIST_CANNOT_BE_EMPTY", null, Locale.ENGLISH));
        }
        List<Integer> resourceIdList = dtoList.stream().map(ResourceAllocationRequestDTO::getResourceId)
                .toList();

        if (!this.commonFunctions
                .checkAllocationConflicts(
                        resourceIdList, request.getStartDate(), request.getEndDate(), null)
                .isEmpty()) {
            throw new BadRequestException(
                    messageSource.getMessage("CONFLICTS_IN_ALLOCATION", null, Locale.ENGLISH));
        }

        dtoList.forEach(
                dto -> resourceAllocationList.add(new Allocation(dto, request.getRequestedBy())));
        Project project = request.getProject();
        project.setTeamSize(project.getTeamSize() + dtoList.size());
        projectRepository.save(project);
        allocationRepository.saveAll(resourceAllocationList);
        this.commonFunctions.updateAllocationStatusExpiryAndTeamSize();
        request.setApprovalFlow(ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED.value);
        skillWiseAllocationRequestRepository.save(request);
        this.emailUtils.allocationMailNotification(
                List.of(request.getRequestedBy().getId()), 18, Optional.of(request), new ArrayList<>());
    }

    @Override
    public void rejectSkillWiseAllocationRequest(Integer requestId, RejectionRequestDTO dto) {
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        ResourceSkillWiseAllocationRequest request = skillWiseAllocationRequestRepository
                .findByIdAndApprovalFlowNotInAndStatus(
                        requestId,
                        List.of(
                                ResourceSkillWiseAllocationRequest.ApprovalFlowValues.REJECTED.value,
                                ResourceSkillWiseAllocationRequest.ApprovalFlowValues.APPROVED.value),
                        ResourceSkillWiseAllocationRequest.StatusValues.ACTIVE.value);
        if (request == null) {
            throw new BadRequestException(
                    messageSource.getMessage(RESOURCE_ALLOCATION_REQUEST_NOT_FOUND, null,
                            Locale.ENGLISH));
        }
        if (!(currentUser.getRole().getName().equals(Resource.Roles.HOD.getValue())
                || currentUser.getRole().getName().equals(Resource.Roles.HR.getValue()))) {
            throw new BadRequestException(messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
        }
        request.setApprovalFlow(ResourceSkillWiseAllocationRequest.ApprovalFlowValues.REJECTED.value);
        request.setRejectedBy(currentUser);
        request.setRejectionReason(dto.getMessage());
        skillWiseAllocationRequestRepository.save(request);
        this.emailUtils.allocationMailNotification(
                List.of(request.getRequestedBy().getId()), 7, Optional.of(request), new ArrayList<>());
    }

    @Override
    public void deleteResourceWiseAllocationRequest(Integer requestId) {
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        ResourceAllocationRequest request = resourceAllocationRequestRepository
                .findByIdAndApprovalFlowNotInAndStatus(
                        requestId,
                        List.of(
                                ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value,
                                ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value),
                        ResourceAllocationRequest.StatusValues.ACTIVE.value);
        if (request == null) {
            throw new BadRequestException(
                    messageSource.getMessage(RESOURCE_ALLOCATION_REQUEST_NOT_FOUND, null,
                            Locale.ENGLISH));
        } else if (currentUser.getRole().getName().equals(Resource.Roles.HOD.getValue())
                && (request
                .getApprovalFlow()
                .equals(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value))) {
            request.setStatus(ResourceAllocationRequest.StatusValues.DELETED.value);
        } else if (currentUser.getRole().getName().equals(Resource.Roles.HR.getValue())
                && (request
                .getApprovalFlow()
                .equals(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HR.value))) {
            request.setStatus(ResourceAllocationRequest.StatusValues.DELETED.value);
        } else if (currentUser.getRole().getName().equals(Resource.Roles.PM.getValue())
                && (request
                .getApprovalFlow()
                .equals(ResourceAllocationRequest.ApprovalFlowValues.PENDING.value))) {
            request.setStatus(ResourceAllocationRequest.StatusValues.DELETED.value);
        } else {
            throw new BadRequestException(messageSource.getMessage(ACCESS_DENIED, null, Locale.ENGLISH));
        }
        if (request.getAllocation() != null) {
            Allocation allocation = allocationRepository
                    .findByIdAndStatus(
                            request.getAllocation().getId(),
                            Allocation.StatusValues.ACTIVE.value)
                    .orElseThrow(
                            () -> new BadRequestException(
                                    messageSource.getMessage(ALLOCATION_NOT_FOUND,
                                            null, Locale.ENGLISH)));
            allocation.setIsEdited((byte) 0);
            allocationRepository.save(allocation);
        }
        resourceAllocationRequestRepository.save(request);
    }

    public void approvalByHRResourceWise(ResourceAllocationRequest request) {
        if (request.getApprovalFlow() == ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value) {
            if (request.getAllocation() != null) {
                Allocation allocation = allocationRepository
                        .findByIdAndStatus(
                                request.getAllocation().getId(),
                                Allocation.StatusValues.ACTIVE.value)
                        .orElseThrow(
                                () -> new BadRequestException(
                                        messageSource.getMessage(
                                                ALLOCATION_NOT_FOUND,
                                                null, Locale.ENGLISH)));
                allocation.setStartDate(request.getStartDate());
                allocation.setEndDate(request.getEndDate());
                allocation.setIsEdited((byte) 0);
                allocation.setUpdatedDate(new Date());
                allocation.setAllocationExpiry(Allocation.AllocationExpiryValues.NOT_STARTED.value);
                allocation.setRequestedBy(request.getRequestedBy());
                allocationRepository.save(allocation);
            } else {
                allocationRepository.save(new Allocation(request));
            }
            Project project = request.getProject();
            project.setTeamSize(project.getTeamSize() + 1);
            projectRepository.save(project);
            request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value);
            resourceAllocationRequestRepository.save(request);
            this.emailUtils.allocationMailNotification(
                    List.of(request.getRequestedBy().getId()),
                    4,
                    Optional.empty(),
                    Collections.singletonList(request));
        }
    }

    public void approvalByHODResourceWise(ResourceAllocationRequest request) {
        if (request.getApprovalFlow() == ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HR.value) {
            if (request.getAllocation() != null) {
                Allocation allocation = allocationRepository
                        .findByIdAndStatus(
                                request.getAllocation().getId(),
                                Allocation.StatusValues.ACTIVE.value)
                        .orElseThrow(
                                () -> new BadRequestException(
                                        messageSource.getMessage(
                                                ALLOCATION_NOT_FOUND,
                                                null, Locale.ENGLISH)));
                allocation.setStartDate(request.getStartDate());
                allocation.setEndDate(request.getEndDate());
                allocation.setIsEdited((byte) 0);
                allocation.setUpdatedDate(new Date());
                allocation.setAllocationExpiry(Allocation.AllocationExpiryValues.NOT_STARTED.value);
                allocation.setRequestedBy(request.getRequestedBy());
                allocationRepository.save(allocation);
            } else {
                allocationRepository.save(new Allocation(request));
            }
            Project project = request.getProject();
            project.setTeamSize(project.getTeamSize() + 1);
            projectRepository.save(project);
            this.emailUtils.allocationMailNotification(
                    List.of(request.getRequestedBy().getId()),
                    4,
                    Optional.empty(),
                    Collections.singletonList(request));
            request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value);
        } else if (request.getApprovalFlow() == ResourceAllocationRequest.ApprovalFlowValues.PENDING.value) {
            request.setApprovalFlow(ResourceAllocationRequest.ApprovalFlowValues.APPROVED_BY_HOD.value);
            if (request.getAllocation() != null) {
                this.emailUtils.allocationMailNotification(
                        resourceRepository.findAllIdByRoleIdAndStatus(
                                Resource.Roles.HR.getId(),
                                Resource.Status.ACTIVE.value),
                        3,
                        Optional.empty(),
                        Collections.singletonList(request));
            } else
                this.emailUtils.allocationMailNotification(
                        resourceRepository.findAllIdByRoleIdAndStatus(
                                Resource.Roles.HR.getId(),
                                Resource.Status.ACTIVE.value),
                        1,
                        Optional.empty(),
                        Collections.singletonList(request));
        }
        resourceAllocationRequestRepository.save(request);
    }

    @Override
    public PagedResponseDTO<AllocationResponseDTO> getAllocationListByProjectId(
            int projectId, String page, String size, Boolean isExpired) {
        LocalDate currentDate = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date newDate = calendar.getTime();
        int pageNumber = parameterValidator.isNumber(PAGE_NUMBER, page);
        int pageSize = parameterValidator.isNumber(PAGE_SIZE, size);
        List<Byte> expiryStatusValues = new ArrayList<>();
        if (Boolean.TRUE.equals(isExpired)) {
            expiryStatusValues.add(Allocation.AllocationExpiryValues.EXPIRED.value);
        } else {
            expiryStatusValues.addAll(
                    List.of(
                            Allocation.AllocationExpiryValues.NOT_STARTED.value,
                            Allocation.AllocationExpiryValues.ON_GOING.value));
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Allocation> pagedList = allocationRepository
                .findAllByProjectProjectIdAndStatusAndAllocationExpiryInOrderByUpdatedDateDesc(
                        projectId, Allocation.StatusValues.ACTIVE.value, expiryStatusValues,
                        pageable);
        List<AllocationResponseDTO> allocationResponseDTOs = new ArrayList<>();
        pagedList.forEach(
                allocation -> {
                    Date allocationStartDateTimeStamp = commonFunctions
                            .convertDateToTimestamp(allocation.getStartDate());
                    LocalDate allocationStartDateLocal = allocationStartDateTimeStamp.toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    Date allocationStartDate = allocationStartDateLocal.isAfter(currentDate)
                            ? allocationStartDateTimeStamp
                            : newDate;

                    int count = commonFunctions.calculateBusinessDays(
                            allocationStartDate, commonFunctions.convertDateToTimestamp(
                                    allocation.getEndDate()));
                    AllocationResponseDTO allocationResponseDTO = new AllocationResponseDTO(
                            allocation, count);
                    allocationResponseDTOs.add(allocationResponseDTO);
                });
        return new PagedResponseDTO<>(
                allocationResponseDTOs,
                pagedList.getTotalPages(),
                pagedList.getTotalElements(),
                pagedList.getNumber());
    }

    @Override
    public ResourceSkillWiseAllocationResponseListDTO getRequestResourceWithSkill(String id) {
        if (id == null) {
            throw new BadRequestException(messageSource.getMessage("ID_REQUIRED", null, Locale.ENGLISH));
        }

        int idInt = parameterValidator.isNumber("id", id);

        Optional<ResourceSkillWiseAllocationRequest> optionalRSWAR = skillWiseAllocationRequestRepository
                .findById(idInt);
        if (!optionalRSWAR.isPresent()) {
            throw new BadRequestException(
                    messageSource.getMessage(
                            "RESOURCE_SKILL_WISE_ALLOCATION_REQUEST_NOT_FOUND", null,
                            Locale.ENGLISH));
        }

        return new ResourceSkillWiseAllocationResponseListDTO(optionalRSWAR.get());
    }

    @Override
    public void deleteAllocation(Integer id) {
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(USER_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        Allocation allocation = allocationRepository
                .findByIdAndStatus(id, Allocation.StatusValues.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage(ALLOCATION_NOT_FOUND, null,
                                        Locale.ENGLISH)));
        Date currentDate = new Date();
        if (currentUser.getRole().getName().equals("HOD")
                || currentUser.getRole().getName().equals("HR")) {
            allocation.setStatus(Allocation.StatusValues.DELETED.value);
        } else {
            if (allocation.getStartDate().after(currentDate)) {
                allocation.setStatus(Allocation.StatusValues.DELETED.value);
            } else {
                throw new BadRequestException(
                        messageSource.getMessage("CANNOT_DELETE_ALLOCATION", null,
                                Locale.ENGLISH));
            }
        }
        allocationRepository.save(allocation);
        List<ResourceAllocationRequest> allocationRequestList = resourceAllocationRequestRepository.findAllByAllocationId(id);
        List<ResourceAllocationRequest> resourceAllocationRequests = new ArrayList<>();
        for (ResourceAllocationRequest request : allocationRequestList) {
            request.setStatus(ResourceAllocationRequest.StatusValues.DELETED.value);
            resourceAllocationRequests.add(request);
        }
        resourceAllocationRequestRepository.saveAll(resourceAllocationRequests);
        Optional<Allocation> previousAllocation = allocationRepository
                .findTopByResourceIdAndProjectProjectTypeNotAndAllocationExpiryAndStatusOrderByEndDateDesc(
                        allocation.getResource().getId(),
                        Project.projectTypeValues.SUPPORT.value,
                        Allocation.AllocationExpiryValues.EXPIRED.value,
                        Allocation.StatusValues.ACTIVE.value);
        Optional<Resource> resource = resourceRepository.findById(allocation.getResource().getId());
        resource.ifPresent(
                resource1 -> {
                    if (previousAllocation.isPresent()) {
                        resource1.setLastWorkedProjectDate(
                                previousAllocation.get().getEndDate());
                    } else {
                        resource1.setLastWorkedProjectDate(resource1.getJoiningDate());
                    }
                    resourceRepository.save(resource1);
                });
        this.commonFunctions.updateAllocationStatusExpiryAndTeamSize();
    }
}
