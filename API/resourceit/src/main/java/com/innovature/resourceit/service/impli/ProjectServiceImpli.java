package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.criteriaquery.ProjectRepositoryCriteria;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.ProjectDownloadRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ProjectListingRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.*;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.ProjectService;
import com.innovature.resourceit.util.CommonFunctions;
import com.innovature.resourceit.util.CustomValidator;
import com.innovature.resourceit.util.EmailUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpli implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpli.class);

    @Autowired
    private ProjectRequestRepository projectRequestRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private AllocationRepository allocationRepository;
    @Autowired
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;
    @Autowired
    private ResourceSkillWiseAllocationRequestRepository resourceSkillWiseAllocationRequestRepository;
    public static final String DATE_PATTERN = "^(0[1-9]|[1-2]\\d|3[0-1])-(0[1-9]|1[0-2])-\\d{4}$";

    @Autowired
    private CustomValidator customValidator;
    @Autowired
    private CommonFunctions commonFunction;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    ParameterValidator parameterValidator;

    @Autowired
    ProjectRepositoryCriteria projectRepositoryCriteria;
    @Autowired
    EmailUtils emailUtils;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private static final String PROJECT_NOT_FOUND = "PROJECT_NOT_FOUND";
    private static final String INVALID_SKILL_ID = "INVALID_SKILL_ID";
    private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String PROJECT_TYPE = "projectType";
    private static final String PROJECT_LIST_FETCHING_FAILED = "PROJECT_LIST_FETCHING_FAILED";
    private static final String PROJECT_REQUEST_NOT_FOUND = "PROJECT_REQUEST_NOT_FOUND";
    private static final String INVALID_APPROVAL_STATUS = "INVALID_APPROVAL_STATUS";
    private static final String START_DATE = "startDate";

    public ProjectServiceImpli(CustomValidator customValidator, MessageSource messageSource, ProjectRepository projectRepository, SkillRepository skillRepository, ResourceRepository resourceRepository, ProjectRequestRepository projectRequestRepository) {
        this.messageSource = messageSource;
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.resourceRepository = resourceRepository;
        this.projectRequestRepository = projectRequestRepository;
        this.customValidator = customValidator;
    }

    @Override
    public ProjectRequestResponseDTO addProjectRequest(ProjectRequestRequestDTO dto) {
        List<Skill> skills = new ArrayList<>();
        Resource requestedBy = resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        if (dto.getSkillIds() != null && !dto.getSkillIds().isEmpty()) {
            skills = skillRepository.findAllById(dto.getSkillIds());

            if (skills.size() != dto.getSkillIds().size()) {
                throw new BadRequestException(messageSource.getMessage(INVALID_SKILL_ID, null, Locale.ENGLISH));
            }
        }
        ProjectRequest projectRequest = new ProjectRequest(dto, skills, requestedBy);
        projectRequest = projectRequestRepository.save(projectRequest);
        List<Integer> userIdlist = resourceRepository.findAllIdByRoleIdAndStatus(Resource.Roles.HOD.getId(), Resource.Status.ACTIVE.value);
        this.emailUtils.projectMailNotification(userIdlist, 9, Optional.of(projectRequest), Optional.empty());
        return new ProjectRequestResponseDTO(projectRequest);
    }

    @Override
    public Page<ProjectRequestResponseDTO> listProjectRequests(Set<Byte> approvalStatus, Set<Integer> managerIds, Set<Byte> projectType, Resource requestedBy, Pageable pageable) {
        try {
            Page<ProjectRequest> projectRequestList = projectRequestRepository.findAll((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (approvalStatus != null && !approvalStatus.isEmpty()) {
                    predicates.add(root.get("approvalStatus").in(approvalStatus));
                }

                if (managerIds != null && !managerIds.isEmpty()) {
                    predicates.add(root.join("manager").get("id").in(managerIds));
                }
                if (projectType != null && !projectType.isEmpty()) {
                    predicates.add(root.get(PROJECT_TYPE).in(projectType));
                }
                if (requestedBy != null) {
                    predicates.add(root.get("requestedBy").in(requestedBy));
                }
                predicates.add(root.get("status").in(ProjectRequest.statusValues.ACTIVE.value));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);

            return projectRequestList.map(this::convertToProjectRequestResponseDto);
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("UNEXPECTED_ERROR_WHILE_LISTING_PROJECT_REQUESTS", null, Locale.ENGLISH));
            throw new BadRequestException(messageSource.getMessage("UNEXPECTED_ERROR_WHILE_LISTING_PROJECT_REQUESTS", null, Locale.ENGLISH));
        }
    }

    public ProjectRequestResponseDTO convertToProjectRequestResponseDto(ProjectRequest projectRequest) {
        return new ProjectRequestResponseDTO(projectRequest);
    }

    @Override
    public Boolean projectApprove(Integer projRequestId, ProjectRequestRequestDTO dto) {
        Boolean isCompleted = Boolean.FALSE;
        Resource requestedBy = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value).orElseThrow(
                        () -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        if (dto.getApprovalStatus() == null) {
            throw new BadRequestException(messageSource.getMessage("APPROVAL_STATUS_CANNOT_BE_NULL", null, Locale.ENGLISH));

        }
        customValidator.validateProjectRequestInputParameters(dto.getDescription(), dto.getStartDate(), dto.getEndDate(), dto.getApprovalStatus(), null, dto.getManagerId());
        if (dto.getManagerId() == null) {
            throw new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH));
        }
        ProjectRequest projectRequest = projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(projRequestId, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(PROJECT_REQUEST_NOT_FOUND, null, Locale.ENGLISH)));
        Resource manager = resourceRepository.findByIdAndStatus(dto.getManagerId(), Resource.Status.ACTIVE.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        List<Skill> skills = dto.getSkillIds().isEmpty() ? Collections.emptyList() : skillRepository.findAllById(dto.getSkillIds());
        if (skills.size() != dto.getSkillIds().size()) {
            throw new BadRequestException(messageSource.getMessage(INVALID_SKILL_ID, null, Locale.ENGLISH));
        }
        if (dto.getProjectId() != null) {
            Optional<Project> projectOptional = projectRepository.findByProjectIdAndStatusNot(dto.getProjectId(), Project.statusValues.DELETED.value);
            if (projectOptional.isEmpty()) {
                throw new BadRequestException(messageSource.getMessage(PROJECT_NOT_FOUND, null, Locale.ENGLISH));
            }
        }

        projectRequest.setDescription(dto.getDescription());
        projectRequest.setTeamSize(dto.getTeamSize());
        projectRequest.setManDay(dto.getManDay());
        projectRequest.setManager(manager);
        projectRequest.setStartDate(dto.getStartDate());
        projectRequest.setEndDate(dto.getEndDate());
        projectRequest.setSkill(skills);
        projectRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.APPROVED.value);
        projectRequest.setProject(dto.getProjectId() != null ? projectRepository.findByProjectIdAndStatusNot(dto.getProjectId(), Project.statusValues.DELETED.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(PROJECT_NOT_FOUND, null, Locale.ENGLISH))) : null);
        projectRequestRepository.save(projectRequest);

        if (dto.getApprovalStatus() == 1) {
            Project project = projectRepository.save(new Project(dto, skills, manager));
            if (areProjectFieldsNotNull(project)) {
                project.setStatus(Project.statusValues.ACTIVE.value);
                isCompleted = Boolean.TRUE;
            } else {
                project.setStatus(Project.statusValues.HALFWAY.value);
            }
            project.setTeamSize(1);
            project = projectRepository.save(project);
            if (project.getEndDate() != null && project.getStartDate() != null) {
                Date dt = new Date();
                Allocation newAllocation = new Allocation();
                newAllocation.setProject(project);
                newAllocation.setResource(new Resource(dto.getManagerId()));
                newAllocation.setStatus(Allocation.StatusValues.ACTIVE.value);
                newAllocation.setAllocationExpiry(Allocation.AllocationExpiryValues.ON_GOING.value);
                newAllocation.setStartDate(dto.getStartDate());
                newAllocation.setEndDate(dto.getEndDate());
                newAllocation.setCreatedDate(dt);
                newAllocation.setUpdatedDate(dt);
                newAllocation.setRequestedBy(requestedBy);
                allocationRepository.save(newAllocation);
                project.setProjectState(this.calculateProjectState(projectRequest.getStartDate(), projectRequest.getEndDate()));
                projectRepository.save(project);

            }
            this.emailUtils.projectMailNotification(List.of(dto.getManagerId()), 15, Optional.empty(), Optional.of(project));
            this.emailUtils.projectMailNotification(List.of(projectRequest.getRequestedBy().getId()), 8, Optional.empty(), Optional.of(project));
        }
        this.commonFunction.updateAllocationStatusExpiryAndTeamSize();
        return isCompleted;
    }

    public boolean areProjectRequestFieldsNotNull(ProjectRequest projectRequest) {
        return projectRequest.getProjectCode() != null && projectRequest.getName() != null && projectRequest.getProjectType() != null && projectRequest.getClientName() != null && projectRequest.getManager() != null && projectRequest.getStartDate() != null && projectRequest.getEndDate() != null && projectRequest.getProject() != null;
    }

    public boolean areProjectFieldsNotNull(Project project) {
        return project.getProjectCode() != null && project.getName() != null && project.getProjectType() != null && project.getClientName() != null && project.getManager() != null && project.getStartDate() != null && project.getEndDate() != null;
    }

    @Transactional
    public void projectApproveById(Integer projRequestId, Byte approvalStatus) {
        Resource requestedBy = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value).orElseThrow(
                        () -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        ProjectRequest projectRequest = projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(projRequestId, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(PROJECT_REQUEST_NOT_FOUND, null, Locale.ENGLISH)));
        if (approvalStatus != 1) {
            throw new BadRequestException(messageSource.getMessage(INVALID_APPROVAL_STATUS, null, Locale.ENGLISH));
        }
        if (projectRequest.getProject() == null) {
            throw new BadRequestException(messageSource.getMessage(PROJECT_NOT_FOUND, null, Locale.ENGLISH));
        }
        Project project = projectRepository.findByProjectIdAndStatusNot(projectRequest.getProject().getProjectId(), Project.statusValues.DELETED.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(PROJECT_NOT_FOUND, null, Locale.ENGLISH)));
        if (project.getStartDate() == null || project.getEndDate() == null) {
            Date dt = new Date();
            Allocation newAllocation = new Allocation();
            newAllocation.setProject(project);
            newAllocation.setResource(new Resource(project.getManager().getId()));
            newAllocation.setStatus(Allocation.StatusValues.ACTIVE.value);
            newAllocation.setAllocationExpiry(Allocation.AllocationExpiryValues.NOT_STARTED.value);
            newAllocation.setStartDate(projectRequest.getStartDate());
            newAllocation.setEndDate(projectRequest.getEndDate());
            newAllocation.setCreatedDate(dt);
            newAllocation.setUpdatedDate(dt);
            newAllocation.setRequestedBy(requestedBy);
            allocationRepository.save(newAllocation);
        }
        if (areProjectRequestFieldsNotNull(projectRequest)) {
            handleProjectManagerAllocationOnApproval(project, projectRequest);
            project.setName(projectRequest.getName());
            project.setProjectCode(projectRequest.getProjectCode());
            project.setProjectType(projectRequest.getProjectType());
            project.setClientName(projectRequest.getClientName());
            project.setManDay(projectRequest.getManDay());
            project.setManager(projectRequest.getManager());
            project.setStartDate(projectRequest.getStartDate());
            project.setEndDate(projectRequest.getEndDate());
            project.setDescription(projectRequest.getDescription());
            // persisting skills
            List<Skill> mergedSkillsList = null;
            Collection<Skill> skills = projectRequest.getSkill();
            mergedSkillsList = new ArrayList<>();
            for (Skill skill : skills) {
                mergedSkillsList.add(entityManager.merge(skill));
            }
            project.setSkill(mergedSkillsList);
            project.setStatus(Project.statusValues.ACTIVE.value);
            Date dt = new Date();
            project.setUpdatedDate(dt);
            project.setEdited(Project.editedValues.NOT_EDITED.value);
            project.setProjectState(this.calculateProjectState(projectRequest.getStartDate(), projectRequest.getEndDate()));
            project = projectRepository.save(project);
            this.handleAllocationsAndAllocationRequests(project);
            sendMailNotificationOnApproval(projectRequest, project);
            projectRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.APPROVED.value);
            projectRequestRepository.save(projectRequest);
        } else {
            projectRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.REJECTED.value);
            projectRequestRepository.save(projectRequest);
            throw new BadRequestException(messageSource.getMessage("PROJECT_REQUEST_CANNOT_APPROVE_EDIT", null, Locale.ENGLISH));
        }
        this.commonFunction.scheduleUpdateAllocationStatusExpiryAndTeamSize();
    }

    private void sendMailNotificationOnApproval(ProjectRequest projectRequest, Project project) {
        if (Objects.equals(project.getManager().getId(), projectRequest.getRequestedBy().getId())) {
            if (Objects.equals(projectRequest.getRequestedBy().getRole().getId(), Resource.Roles.HR.getId())) {
                this.emailUtils.projectMailNotification(List.of(projectRequest.getRequestedBy().getId()), 13, Optional.empty(), Optional.of(project));
            } else {
                this.emailUtils.projectMailNotification(List.of(projectRequest.getRequestedBy().getId()), 13, Optional.empty(), Optional.of(project));
                this.emailUtils.projectMailNotification(resourceRepository.findAllIdByRoleIdAndStatus(Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value), 17, Optional.empty(), Optional.of(project));
            }
        } else {
            if (Objects.equals(projectRequest.getRequestedBy().getRole().getId(), Resource.Roles.HR.getId())) {
                this.emailUtils.projectMailNotification(List.of(projectRequest.getRequestedBy().getId()), 13, Optional.empty(), Optional.of(project));
                this.emailUtils.projectMailNotification(List.of(project.getManager().getId()), 14, Optional.empty(), Optional.of(project));

            } else {
                this.emailUtils.projectMailNotification(List.of(projectRequest.getRequestedBy().getId()), 13, Optional.empty(), Optional.of(project));
                this.emailUtils.projectMailNotification(resourceRepository.findAllIdByRoleIdAndStatus(Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value), 17, Optional.empty(), Optional.of(project));
            }
        }
    }

    public String getSortKey(String sortKey) {
        if (sortKey == null) {
            sortKey = "createdDate";
        } else {
            switch (sortKey) {
                case "projectCode":
                    sortKey = "projectCode";
                    break;
                case "projectName":
                    sortKey = "name";
                    break;
                case "teamSize":
                    sortKey = "teamSize";
                    break;
                case "endDate":
                    sortKey = "endDate";
                    break;
                case START_DATE:
                    sortKey = START_DATE;

                    break;
                default:
                    sortKey = "createdDate";

            }
        }

        return sortKey;
    }

    @Override
    public PagedResponseDTO<ProjectListResponseDTO> getProjects(ProjectListingRequestDTO projectListingRequestDTO) throws ParseException {
        int pageNumber = parameterValidator.isNumber("pageNumber", projectListingRequestDTO.getPageNumber());
        int pageSize = parameterValidator.isNumber("pageSize", projectListingRequestDTO.getPageSize());
        List<Integer> projectTypes = parameterValidator.isNumbersNum(PROJECT_TYPE, projectListingRequestDTO.getProjectType());
        List<Integer> projectStates = parameterValidator.isNumbersNum("projectState", projectListingRequestDTO.getProjectState());
        List<Integer> managerIds = parameterValidator.isNumbersNum("managerId", projectListingRequestDTO.getManagerId());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<ProjectListResponseDTO> projectListResponseDTO;

        String sortKey = getSortKey(projectListingRequestDTO.getSortKey());
        boolean sortOrder = projectListingRequestDTO.getSortOrder();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Boolean.FALSE.equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortKey));

        if ((projectListingRequestDTO.getStartDate() != null && !projectListingRequestDTO.getStartDate().trim().isEmpty() && !isValidDate(projectListingRequestDTO.getStartDate())) || (projectListingRequestDTO.getEndDate() != null && !projectListingRequestDTO.getEndDate().trim().isEmpty() && !isValidDate(projectListingRequestDTO.getEndDate()))) {
            throw new BadRequestException(messageSource.getMessage("DATE_FORMAT_ERROR", null, Locale.ENGLISH));
        }
        try {
            Date sDate = null;
            Date eDate = null;
            if (projectListingRequestDTO.getStartDate() != null) {
                sDate = projectListingRequestDTO.getStartDate().trim().isEmpty() ? null : sdf.parse(projectListingRequestDTO.getStartDate());
            }

            if (projectListingRequestDTO.getEndDate() != null) {
                eDate = projectListingRequestDTO.getEndDate().trim().isEmpty() ? null : sdf.parse(projectListingRequestDTO.getEndDate());
            }

            Page<Project> projectList = projectRepositoryCriteria.findFilteredProjectsWithPagination(projectListingRequestDTO.getProjectName(), sDate, eDate, projectStates, managerIds, projectTypes, pageable);
            projectListResponseDTO = projectList.getContent().stream().map(ProjectListResponseDTO::new).toList();
            return new PagedResponseDTO<>(projectListResponseDTO, projectList.getTotalPages(), projectList.getTotalElements(), projectList.getNumber());
        } catch (IllegalArgumentException e) {
            LOGGER.error(messageSource.getMessage("ARGUMENT_IS_INVALID", null, Locale.ENGLISH));
            throw new BadRequestException(messageSource.getMessage("ARGUMENT_IS_INVALID", null, Locale.ENGLISH));

        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage(PROJECT_LIST_FETCHING_FAILED, null, Locale.ENGLISH));
            throw new BadRequestException(messageSource.getMessage(PROJECT_LIST_FETCHING_FAILED, null, Locale.ENGLISH));

        }

    }

    @Override
    @Transactional
    public Boolean editProject(ProjectRequestRequestDTO dto) {
        boolean flag = Boolean.FALSE;
        boolean isRequest = Boolean.FALSE;
        this.customValidator.validateProjectRequestDTO(dto);
        Optional<Project> project = projectRepository.findById(dto.getProjectId());
        Optional<Resource> managerOptional = resourceRepository.findByIdAndStatus(dto.getManagerId(), Resource.Status.ACTIVE.value);
        Resource manager = managerOptional.orElseThrow(() -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        Optional<Resource> resourceOptional = resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value);

        Resource currentUser = resourceOptional.orElseThrow(() -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));

        if (project.isEmpty()) {
            throw new BadRequestException(messageSource.getMessage(PROJECT_NOT_FOUND, null, Locale.ENGLISH));
        }
        List<Skill> skills = dto.getSkillIds().isEmpty() ? Collections.emptyList() : skillRepository.findAllById(dto.getSkillIds());
        if (skills.size() != dto.getSkillIds().size()) {
            throw new BadRequestException(messageSource.getMessage(INVALID_SKILL_ID, null, Locale.ENGLISH));
        }
        if (Objects.equals(currentUser.getRole().getName(), Resource.Roles.HOD.getValue())) {
            Byte projectState = this.calculateProjectState(dto.getStartDate(), dto.getEndDate());
            Integer prevManager = project.get().getManager().getId();
            dto.setName(project.get().getName());
            dto.setProjectCode(project.get().getProjectCode());
            dto.setProjectType(project.get().getProjectType());
            dto.setClientName(project.get().getClientName());
            handleNewAllocationOfManager(project.get(), dto);
            handleProjectManagerAllocation(project.get(), dto);
            if (!dto.getManagerId().equals(project.get().getManager().getId())) {
                flag = Boolean.TRUE;
            }
            Project project2 = projectRepository.save(project.get().update(dto, skills, manager, projectState));
            handleProjectStatus(project2);
            this.handleAllocationsAndAllocationRequests(project2);
            sendMailNotificationOnEdit(dto, project2, prevManager, flag);
        } else {
            if (Boolean.TRUE.equals(this.commonFunction.checkProjectChanges(dto))) {
                project.get().setDescription(dto.getDescription());
                project.get().setSkill(skills);
                projectRepository.save(project.get());
            } else {
                isRequest = Boolean.TRUE;
                // HR
                if (Objects.equals(currentUser.getRole().getName(), Resource.Roles.HR.getValue())) {
                    dto.setManagerId(project.get().getManager().getId());
                } // PM
                else {
                    dto.setName(project.get().getName());
                    dto.setProjectCode(project.get().getProjectCode());
                    dto.setProjectType(project.get().getProjectType());
                    dto.setClientName(project.get().getClientName());
                    dto.setManagerId(project.get().getManager().getId());
                }
                ProjectRequest projectRequest = new ProjectRequest(dto, skills, project.get().getTeamSize(), currentUser);
                projectRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.PENDING.value);
                projectRequest = projectRequestRepository.save(projectRequest);
                project.get().setEdited(Project.editedValues.EDITED.value);
                projectRepository.save(project.get());
                this.emailUtils.projectMailNotification(resourceRepository.findAllIdByRoleIdAndStatus(Resource.Roles.HOD.getId(), Resource.Status.ACTIVE.value), 11, Optional.of(projectRequest), Optional.empty());
            }
        }
        this.commonFunction.scheduleUpdateAllocationStatusExpiryAndTeamSize();
        return isRequest;
    }

    //handle the project manager allocation when project period changes
    public void handleProjectManagerAllocation(Project project, ProjectRequestRequestDTO dto) {
        if ((project.getStartDate() != null) && (project.getEndDate() != null)) {
            Allocation prevAllocation = allocationRepository.findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(dto.getProjectId(), project.getManager().getId(), Allocation.StatusValues.ACTIVE.value);
            LocalDate projectStartDate = this.commonFunction.convertDateToTimestamp(project.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate projectEndDate = this.commonFunction.convertDateToTimestamp(project.getEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate newProjectStartDate = this.commonFunction.convertDateToTimestamp(dto.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate newProjectEndDate = this.commonFunction.convertDateToTimestamp(dto.getEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (dto.getManagerId().equals(project.getManager().getId())) {
                if ((newProjectEndDate.isBefore(projectStartDate)) || (newProjectStartDate.isAfter(projectEndDate))) {
                    prevAllocation.setStartDate(dto.getStartDate());
                }
                prevAllocation.setEndDate(dto.getEndDate());
                prevAllocation.setAllocationExpiry(Allocation.AllocationExpiryValues.NOT_STARTED.value);
                allocationRepository.save(prevAllocation);
            } else {
                handleProjectManagerAllocationOnProjectMangerChange(prevAllocation, projectStartDate, projectEndDate, newProjectStartDate, newProjectEndDate, dto, project);
            }
        }
    }

    public void handleProjectManagerAllocationOnApproval(Project project, ProjectRequest projectRequest) {
        if (project.getStartDate() != null && project.getEndDate() != null) {
            Allocation managerAllocation = allocationRepository.findTopByProjectProjectIdAndResourceIdAndStatusOrderByCreatedDateDesc(projectRequest.getProject().getProjectId(), project.getManager().getId(), Allocation.StatusValues.ACTIVE.value);
            LocalDate projectStartDate = this.commonFunction.convertDateToTimestamp(project.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate projectEndDate = this.commonFunction.convertDateToTimestamp(project.getEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate newProjectStartDate = this.commonFunction.convertDateToTimestamp(projectRequest.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate newProjectEndDate = this.commonFunction.convertDateToTimestamp(projectRequest.getEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (projectRequest.getProject().getManager().getId().equals(project.getManager().getId())) {
                if ((newProjectEndDate.isBefore(projectStartDate)) || (newProjectStartDate.isAfter(projectEndDate))) {
                    managerAllocation.setStartDate(projectRequest.getStartDate());
                }
                managerAllocation.setEndDate(projectRequest.getEndDate());
                managerAllocation.setAllocationExpiry(Allocation.AllocationExpiryValues.NOT_STARTED.value);
                allocationRepository.save(managerAllocation);
            }
        }
    }

    public void handleProjectManagerAllocationOnProjectMangerChange(Allocation prevAllocation, LocalDate projectStartDate, LocalDate projectEndDate, LocalDate newProjectStartDate, LocalDate newProjectEndDate, ProjectRequestRequestDTO dto, Project project) {
        Resource requestedBy = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value).orElseThrow(
                        () -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        LocalDate currentDate = LocalDate.now();
        Date dt = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        prevAllocation.setEndDate(dt);
        Allocation newAllocation = new Allocation();
        newAllocation.setProject(project);
        newAllocation.setResource(new Resource(dto.getManagerId()));
        newAllocation.setStatus(Allocation.StatusValues.ACTIVE.value);
        newAllocation.setAllocationExpiry(Allocation.AllocationExpiryValues.NOT_STARTED.value);
        newAllocation.setCreatedDate(dt);
        newAllocation.setUpdatedDate(dt);
        newAllocation.setRequestedBy(requestedBy);
        if ((newProjectEndDate.isBefore(projectStartDate)) || (newProjectStartDate.isAfter(projectEndDate)) || currentDate.isBefore(newProjectStartDate)) {
            prevAllocation.setStatus(Allocation.StatusValues.DELETED.value);
            newAllocation.setStartDate(dto.getStartDate());
            newAllocation.setEndDate(dto.getEndDate());
        } else {
            LocalDate prevAllocationStartDate = this.commonFunction.convertDateToTimestamp(prevAllocation.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (prevAllocationStartDate.isAfter(currentDate)) {
                dt = prevAllocation.getStartDate();
                prevAllocation.setStatus(Allocation.StatusValues.DELETED.value);
            }
            prevAllocation.setEndDate(dt);
            newAllocation.setStartDate(calendar.getTime());
            newAllocation.setEndDate(dto.getEndDate());
        }
        allocationRepository.saveAll(List.of(prevAllocation, newAllocation));
        this.commonFunction.deleteWrongAllocations();
    }

    //for fetching project state
    private void handleProjectStatus(Project project2) {
        if (areProjectFieldsNotNull(project2)) {
            project2.setStatus(Project.statusValues.ACTIVE.value);
        }
        projectRepository.save(project2);
    }

    //handle the case where first time allocation of project manager(when start date and end date get filled)
    private void handleNewAllocationOfManager(Project project, ProjectRequestRequestDTO dto) {
        Resource requestedBy = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value).orElseThrow(
                        () -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        if (dto.getManagerId().equals(project.getManager().getId()) && (project.getStartDate() == null || project.getEndDate() == null)) {
            Date dt = new Date();
            Allocation newAllocation = new Allocation();
            newAllocation.setProject(project);
            newAllocation.setResource(new Resource(project.getManager().getId()));
            newAllocation.setStatus(Allocation.StatusValues.ACTIVE.value);
            newAllocation.setAllocationExpiry(Allocation.AllocationExpiryValues.NOT_STARTED.value);
            newAllocation.setStartDate(dto.getStartDate());
            newAllocation.setEndDate(dto.getEndDate());
            newAllocation.setCreatedDate(dt);
            newAllocation.setUpdatedDate(dt);
            newAllocation.setRequestedBy(requestedBy);
            allocationRepository.save(newAllocation);

        }
    }

    private void handleAllocationsAndAllocationRequests(Project project2) {
        if (project2.getStartDate() != null && project2.getEndDate() != null) {
            this.commonFunction.adjustAllocationDates(project2.getProjectId(), project2.getStartDate(), project2.getEndDate());
            this.commonFunction.removeAllocationRequests(project2.getProjectId(), project2.getStartDate(), project2.getEndDate());
        }
    }

    private void sendMailNotificationOnEdit(ProjectRequestRequestDTO dto, Project project2, Integer prevManager, Boolean flag) {
        if (Boolean.TRUE.equals(flag)) {
            this.emailUtils.projectMailNotification(List.of(dto.getManagerId()), 15, Optional.empty(), Optional.of(project2));
            this.emailUtils.projectMailNotification(List.of(prevManager), 16, Optional.empty(), Optional.of(project2));
        } else
            this.emailUtils.projectMailNotification(List.of(dto.getManagerId()), 14, Optional.empty(), Optional.of(project2));
        this.emailUtils.projectMailNotification(resourceRepository.findAllIdByRoleIdAndStatus(Resource.Roles.HR.getId(), Resource.Status.ACTIVE.value), 17, Optional.empty(), Optional.of(project2));

    }

    private Byte calculateProjectState(Date startDate, Date endDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate localStartDate = this.commonFunction.convertDateToTimestamp(startDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = this.commonFunction.convertDateToTimestamp(endDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (currentDate.isBefore(localStartDate)) {
            return Project.projectStateValues.NOT_STARTED.value;
        } else if (currentDate.isAfter(localEndDate)) {
            return Project.projectStateValues.COMPLETED.value;
        } else {
            return Project.projectStateValues.IN_PROGRESS.value;
        }
    }

    public static boolean isValidDate(String date) {
        Pattern pattern = Pattern.compile(DATE_PATTERN);
        Matcher matcher = pattern.matcher(date);

        return matcher.matches();
    }

    @Override
    public void projectExcelDownload(HttpServletResponse response, ProjectDownloadRequestDTO projectDownloadRequestDTO) throws ParseException {
        workbook = new XSSFWorkbook();
        writeHeader();

        write(getProjectList(projectDownloadRequestDTO));
        ServletOutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);

            workbook.close();
            outputStream.close();
        } catch (IOException ex) {
            LOGGER.info(String.format("Error %s ", ex));
            throw new BadRequestException(messageSource.getMessage("RESOURCE_DOWNLOAD_FAILED", null, Locale.ENGLISH));
        }
    }

    @Override
    public ProjectResponseDTO getProjectById(Integer id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new BadRequestException(messageSource.getMessage(PROJECT_NOT_FOUND, null, Locale.ENGLISH)));
        return new ProjectResponseDTO(project);
    }

    @Override
    public ProjectRequestResponseDTO getProjectRequestById(Integer id) {
        ProjectRequest projectRequest = projectRequestRepository.findById(id).orElseThrow(() -> new BadRequestException(messageSource.getMessage(PROJECT_REQUEST_NOT_FOUND, null, Locale.ENGLISH)));
        return new ProjectRequestResponseDTO(projectRequest);
    }

    @Override
    public List<ProjectListByManagerResponseDTO> getProjectsByManager(Boolean isCurrentUser) {
        Set<Project> projects = new HashSet<>();
        List<Project> projectList;
        if (Boolean.TRUE.equals(isCurrentUser)) {
            Optional<Resource> resource = resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value);
            if (!resource.isPresent()) {
                throw new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH));
            }
            projectList = projectRepository.findByManagerIdAndStatusAndProjectStateNotAndEdited(resource.get().getId(), Project.statusValues.ACTIVE.value, Project.projectStateValues.COMPLETED.value, Project.editedValues.NOT_EDITED.value);
        } else {
            projectList = projectRepository.findByStatusAndProjectStateNotAndEdited(Project.statusValues.ACTIVE.value, Project.projectStateValues.COMPLETED.value, Project.editedValues.NOT_EDITED.value);
        }
        projects.addAll(projectList);
        projects.addAll(projectRepository.findAllByProjectTypeAndStatusAndProjectStateNotAndEdited(Project.projectTypeValues.SUPPORT.value, Project.statusValues.ACTIVE.value, Project.projectStateValues.COMPLETED.value, Project.editedValues.NOT_EDITED.value));
        List<Project> sortedProjects = new ArrayList<>(projects);
        sortedProjects.sort(Comparator.comparing(Project::getCreatedDate).reversed());
        return sortedProjects.stream().map(project -> new ProjectListByManagerResponseDTO(project.getProjectId(), project.getName(), project.getProjectCode())).toList();
    }

    @Override
    public void rejectProject(Integer projRequestId, Byte approvalStatus) {
        if (approvalStatus != 2) {
            throw new BadRequestException(messageSource.getMessage(INVALID_APPROVAL_STATUS, null, Locale.ENGLISH));
        }
        ProjectRequest projectRequest = projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(projRequestId, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(PROJECT_REQUEST_NOT_FOUND, null, Locale.ENGLISH)));
        projectRequest.setApprovalStatus(ProjectRequest.approvalStatusValues.REJECTED.value);
        projectRequest = projectRequestRepository.save(projectRequest);
        if (projectRequest.getProject() == null) {
            this.emailUtils.projectMailNotification(List.of(projectRequest.getRequestedBy().getId()), 10, Optional.of(projectRequest), Optional.empty());

        } else {
            Project project = projectRequest.getProject();
            project.setEdited(Project.editedValues.NOT_EDITED.value);
            projectRepository.save(project);
            this.emailUtils.projectMailNotification(List.of(projectRequest.getRequestedBy().getId()), 12, Optional.of(projectRequest), Optional.empty());
        }
    }

    @Override
    public void deleteProjectRequest(Integer requestId) {
        Resource currentUser = resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        Optional<ProjectRequest> request = projectRequestRepository.findByProjectRequestIdAndStatusNotAndApprovalStatus(requestId, ProjectRequest.statusValues.DELETED.value, ProjectRequest.approvalStatusValues.PENDING.value);
        if (request.isEmpty()) {
            throw new BadRequestException(messageSource.getMessage(PROJECT_REQUEST_NOT_FOUND, null, Locale.ENGLISH));
        } else if (currentUser.getRole().getName().equals(Resource.Roles.HR.getValue())) {
            request.get().setStatus(ProjectRequest.statusValues.DELETED.value);
        } else if (currentUser.getRole().getName().equals(Resource.Roles.PM.getValue())) {
            request.get().setStatus(ProjectRequest.statusValues.DELETED.value);
        } else {
            throw new BadRequestException(messageSource.getMessage("ACCESS_DENIED", null, Locale.ENGLISH));
        }
        if (request.get().getProject() != null) {
            Project project = request.get().getProject();
            project.setEdited(Project.editedValues.NOT_EDITED.value);
            projectRepository.save(project);
        }
        projectRequestRepository.save(request.get());
    }

    @Override
    public void deleteProject(Integer projectId) {
        Resource currentUser = resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        Optional<Project> project = projectRepository.findByProjectIdAndStatusNot(projectId, Project.statusValues.DELETED.value);
        if (project.isEmpty()) {
            throw new BadRequestException(messageSource.getMessage(PROJECT_NOT_FOUND, null, Locale.ENGLISH));
        } else if (currentUser.getRole().getName().equals(Resource.Roles.HR.getValue())) {
            project.get().setStatus(Project.statusValues.DELETED.value);
            List<Allocation> allocationList = allocationRepository.findAllByProjectProjectId(projectId);
            allocationList
                    .forEach(allocation -> allocation.setStatus(Allocation.StatusValues.DELETED.value));
            allocationRepository.saveAll(allocationList);
            List<ProjectRequest> projectRequestList = projectRequestRepository.findAllByProjectProjectId(projectId);
            projectRequestList
                    .forEach(projectRequest -> projectRequest.setStatus(ProjectRequest.statusValues.DELETED.value));
            projectRequestRepository.saveAll(projectRequestList);
            List<ResourceAllocationRequest> resourceAllocationRequestList = resourceAllocationRequestRepository.findAllByProjectProjectId(projectId);
            resourceAllocationRequestList
                    .forEach(resourceAllocationRequest -> resourceAllocationRequest.setStatus(ResourceAllocationRequest.StatusValues.DELETED.value));
            resourceAllocationRequestRepository.saveAll(resourceAllocationRequestList);
            List<ResourceSkillWiseAllocationRequest> resourceSkillWiseAllocationRequestsList = resourceSkillWiseAllocationRequestRepository.findAllByProjectProjectId(projectId);
            resourceSkillWiseAllocationRequestsList
                    .forEach(resourceSkillWiseAllocationRequest -> resourceSkillWiseAllocationRequest.setStatus(ResourceSkillWiseAllocationRequest.StatusValues.DELETED.value));
            resourceSkillWiseAllocationRequestRepository.saveAll(resourceSkillWiseAllocationRequestsList);
        } else {
            throw new BadRequestException(messageSource.getMessage("ACCESS_DENIED", null, Locale.ENGLISH));
        }
        projectRepository.save(project.get());
        this.commonFunction.updateAllocationStatusExpiryAndTeamSize();
    }

    private void write(List<ProjectListResponseDTO> lists) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        style.setFont(font);
        for (ProjectListResponseDTO projectListResponseDTO : lists) {
            Row row = sheet.createRow(rowCount++);
            createCell(row, projectListResponseDTO, style);
        }
    }

    private void createCell(Row row, ProjectListResponseDTO valueOfCell, CellStyle style) {
        for (int i = 0; i <= 11; i++) {
            sheet.autoSizeColumn(i);
            switch (i) {
                case 0 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getProjectCode());
                    cell.setCellStyle(style);
                }
                case 1 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getName());
                    cell.setCellStyle(style);
                }
                case 2 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getDescription());
                    cell.setCellStyle(style);
                }
                case 3 -> {
                    Cell cell = row.createCell(i);
                    String s = "Innovature Billing";
                    if (valueOfCell.getProjectType() == 2) {
                        s = "Support";
                    } else if (valueOfCell.getProjectType() == 1) {
                        s = "Customer Billing";
                    }
                    cell.setCellValue(s);
                    cell.setCellStyle(style);
                }
                case 4 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getManager().getName());
                    cell.setCellStyle(style);
                }
                case 5 -> {
                    Cell cell = row.createCell(i);
                    String manDayValue = (valueOfCell.getManDay() != null) ? String.valueOf(valueOfCell.getManDay()) : "Nil";
                    cell.setCellValue(manDayValue);
                    cell.setCellStyle(style);
                }
                case 6 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getClientName());
                    cell.setCellStyle(style);
                }
                case 7 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getStartDate());
                    cell.setCellStyle(style);
                }
                case 8 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getEndDate());
                    cell.setCellStyle(style);
                }
                case 9 -> {
                    Cell cell = row.createCell(i);
                    String c = valueOfCell.getSkill().stream().map(Skill::getName).collect(Collectors.joining(", "));
                    cell.setCellValue(c);
                    cell.setCellStyle(style);
                }
                case 10 -> {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(valueOfCell.getTeamSize().toString());
                    cell.setCellStyle(style);
                }
                case 11 -> {
                    String g = "Not Started";
                    Cell cell = row.createCell(i);
                    if (valueOfCell.getProjectState() == 1) {
                        g = "In Progress";
                    } else if (valueOfCell.getProjectState() == 2) {
                        g = "Completed";
                    }
                    cell.setCellValue(g);
                    cell.setCellStyle(style);
                }
                default -> throw new AssertionError();
            }
        }

    }

    public String dateToStringFormat(Date date) {
        // 2023-09-11 14:41:57.618
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Sheet1");
        Row row = sheet.createRow(0);
        row.setHeight((short) 600);
        CellStyle style = createHeaderCellStyle();

        String[] headers = {"Project Code", "Project Name", "Description", "Project Type", "Project Manager", "Estimated Man Days", "Client", "Start Date", "End Date", "Technology", "Team Size", "Project Status"};

        for (int i = 0; i < headers.length; i++) {
            createCellHeader(row, i, headers[i], style);
        }
    }

    private CellStyle createHeaderCellStyle() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(15);
        style.setFont(font);
        return style;
    }

    private void createCellHeader(Row row, int columnCount, String valueOfCell, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        cell.setCellValue(valueOfCell);
        cell.setCellStyle(style);
    }

    public List<ProjectListResponseDTO> getProjectList(ProjectDownloadRequestDTO projectDownloadRequestDTO) throws ParseException {
        List<Integer> projectTypes = parameterValidator.isNumbersNum(PROJECT_TYPE, projectDownloadRequestDTO.getProjectType());
        List<Integer> projectStates = parameterValidator.isNumbersNum("projectState", projectDownloadRequestDTO.getProjectState());
        List<Integer> managerIds = parameterValidator.isNumbersNum("managerId", projectDownloadRequestDTO.getManagerId());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<ProjectListResponseDTO> projectListResponseDTO;

        if ((projectDownloadRequestDTO.getStartDate() != null && !projectDownloadRequestDTO.getStartDate().trim().isEmpty() && !isValidDate(projectDownloadRequestDTO.getStartDate())) || (projectDownloadRequestDTO.getEndDate() != null && !projectDownloadRequestDTO.getEndDate().trim().isEmpty() && !isValidDate(projectDownloadRequestDTO.getEndDate()))) {
            throw new BadRequestException(messageSource.getMessage("DATE_FORMAT_ERROR", null, Locale.ENGLISH));
        }

        try {
            Date sDate = null;
            Date eDate = null;
            if (projectDownloadRequestDTO.getStartDate() != null) {
                sDate = projectDownloadRequestDTO.getStartDate().trim().isEmpty() ? null : sdf.parse(projectDownloadRequestDTO.getStartDate());
            }

            if (projectDownloadRequestDTO.getEndDate() != null) {
                eDate = projectDownloadRequestDTO.getEndDate().trim().isEmpty() ? null : sdf.parse(projectDownloadRequestDTO.getEndDate());
            }

            List<Project> projectList = projectRepositoryCriteria.findFilteredProjectsForDownload(projectDownloadRequestDTO.getProjectName(), sDate, eDate, projectStates, managerIds, projectTypes);

            projectListResponseDTO = projectList.stream()
                    .map(ProjectListResponseDTO::new)
                    .collect(Collectors.toList());

            Collections.reverse(projectListResponseDTO);

            return projectListResponseDTO;
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage(PROJECT_LIST_FETCHING_FAILED, null, Locale.ENGLISH));
            throw new BadRequestException(messageSource.getMessage(PROJECT_LIST_FETCHING_FAILED, null, Locale.ENGLISH));

        }

    }


}
