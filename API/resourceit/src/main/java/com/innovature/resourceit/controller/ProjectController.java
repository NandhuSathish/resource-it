package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.dto.requestdto.ProjectDownloadRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ProjectListingRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.*;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.ProjectService;
import com.innovature.resourceit.util.CommonFunctions;
import com.innovature.resourceit.util.CustomValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private CustomValidator customValidator;
    @Autowired
    private CommonFunctions commonFunction;

    private static final String USER_NOT_FOUND = "USER_NOT_FOUND";

    public ProjectController(ProjectService projectService, MessageSource messageSource, ResourceRepository resourceRepository) {
        this.projectService = projectService;
        this.messageSource = messageSource;
        this.resourceRepository = resourceRepository;
    }

    @PostMapping
    public ResponseEntity<Object> addProjectRequest(@Valid @RequestBody ProjectRequestRequestDTO dto) {
        customValidator.validateProjectRequestInputParameters(dto.getDescription(), dto.getStartDate(),
                dto.getEndDate(), null, dto.getProjectType(), dto.getManagerId());
        projectService.addProjectRequest(dto);
        String[] list = messageSource.getMessage("PROJECT_REQUEST_ADDED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.CREATED);
    }

    @GetMapping("/project-request")
    public ResponseEntity<PagedResponseDTO<ProjectRequestResponseDTO>> listProjectRequests(
            @RequestParam(name = "pageNumber", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "20") int size,
            @RequestParam(name = "approvalStatus", required = false) Set<Byte> approvalStatus,
            @RequestParam(name = "projectType", required = false) Set<Byte> projectType,
            @RequestParam(name = "managerId", required = false) Set<Integer> managerId,
            @RequestParam(name = "isRequestList", defaultValue = "false") Boolean isRequestList) {

        List<Byte> projectTypeValues = List.of((byte) 0, (byte) 1, (byte) 2);
        List<Byte> approvalStatusValues = List.of((byte) 0, (byte) 1, (byte) 2);
        Resource requestedBy = null;
        if (Boolean.TRUE.equals(isRequestList)) {
            requestedBy = resourceRepository
                    .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value).orElseThrow(
                            () -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        }
        if (projectType != null && projectType.stream().anyMatch(value -> value != null && !projectTypeValues.contains(value))) {
            throw new BadRequestException(messageSource.getMessage("INVALID_PROJECT_TYPE", null, Locale.ENGLISH));
        }
        if (approvalStatus != null && approvalStatus.stream().anyMatch(value -> value != null && !approvalStatusValues.contains(value))) {
            throw new BadRequestException(messageSource.getMessage("INVALID_APPROVAL_STATUS", null, Locale.ENGLISH));
        }
        if (managerId != null && managerId.stream().anyMatch(value -> value != null && !resourceRepository.findById(value).isPresent())) {
            throw new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdDate");
        Page<ProjectRequestResponseDTO> resultPage = projectService.listProjectRequests(approvalStatus, managerId,
                projectType, requestedBy, pageable);

        List<ProjectRequestResponseDTO> content = resultPage.getContent();
        int totalPages = resultPage.getTotalPages();
        long totalItems = resultPage.getTotalElements();
        int currentPage = resultPage.getNumber();

        PagedResponseDTO<ProjectRequestResponseDTO> response = new PagedResponseDTO<>(content, totalPages, totalItems, currentPage);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //project creation request approval, here HOD have the option to enter or modify the data for the project.
    @PostMapping("/approval/{projRequestId}")
    public ResponseEntity<Object> projectApprove(@PathVariable("projRequestId") Integer projRequestId, @Valid @RequestBody ProjectRequestRequestDTO projectRequestDTO) {
        Boolean isCompleted =
                projectService.projectApprove(projRequestId, projectRequestDTO);
        String[] list;
        if (Boolean.TRUE.equals(isCompleted)) {
            list = messageSource.getMessage("PROJECT_APPROVED", null, Locale.ENGLISH).split("-");
        } else {
            list = messageSource.getMessage("PROJECT_APPROVED_IN_HALF", null, Locale.ENGLISH).split("-");
        }
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @PutMapping("/reject/{projRequestId}/{approvalStatus}")
    public ResponseEntity<Object> rejectProject(@PathVariable("projRequestId") Integer projRequestId, @PathVariable("approvalStatus") Byte approvalStatus) {
        projectService.rejectProject(projRequestId, approvalStatus);
        String[] list = messageSource.getMessage("PROJECT_REJECTED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    //project edit request approval
    @PostMapping("/approve/{projRequestId}")
    public ResponseEntity<Object> projectApproveById(@PathVariable("projRequestId") Integer
                                                             projRequestId, @RequestParam(name = "approvalStatus", required = true) Byte approvalStatus) {
        projectService.projectApproveById(projRequestId, approvalStatus);
        String[] list = messageSource.getMessage("PROJECT_APPROVED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @PostMapping("/listing")
    public ResponseEntity<PagedResponseDTO<ProjectListResponseDTO>> getProjects(@RequestBody ProjectListingRequestDTO projectListingRequestDTO) throws ParseException {
        PagedResponseDTO<ProjectListResponseDTO> pagedResponseDTO = projectService.getProjects(projectListingRequestDTO);
        return new ResponseEntity<>(pagedResponseDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> editProject(@Valid @RequestBody ProjectRequestRequestDTO dto) {
        Optional<Resource> resourceOptional = resourceRepository.findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value);
        String[] list;
        Resource currentUser = resourceOptional.orElseThrow(() -> new BadRequestException(messageSource.getMessage(USER_NOT_FOUND, null, Locale.ENGLISH)));
        Boolean isRequest = projectService.editProject(dto);
        Role userRole = currentUser.getRole();
        if ((userRole != null && Objects.equals(userRole.getName(), Resource.Roles.HOD.getValue()) || userRole != null && Boolean.TRUE.equals(!Objects.equals(userRole.getName(), Resource.Roles.HOD.getValue()) && this.commonFunction.checkProjectChanges(dto))) && Boolean.FALSE.equals(isRequest)) {
            list = messageSource.getMessage("PROJECT_EDITED", null, Locale.ENGLISH).split("-");
        } else {
            list = messageSource.getMessage("PROJECT_REQUEST_ADDED", null, Locale.ENGLISH).split("-");
        }

        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.CREATED);
    }

    @PostMapping("/download")
    public ResponseEntity<Object> exportProjects(HttpServletResponse response, @RequestBody ProjectDownloadRequestDTO projectDownloadRequestDTO) throws ParseException {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"project.xlsx\"");
        projectService.projectExcelDownload(response, projectDownloadRequestDTO);
        return new ResponseEntity<>(new SuccessResponseDTO("200", "Successfully downloaded"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProjectById(@PathVariable Integer id) {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/project-request/{id}")
    public ResponseEntity<Object> getProjectRequestById(@PathVariable Integer id) {
        ProjectRequestResponseDTO projectRequestResponseDTO = projectService.getProjectRequestById(id);
        return new ResponseEntity<>(projectRequestResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/manager")
    public ResponseEntity<Object> getProjectsByManager(@RequestParam(name = "isCurrentUser", defaultValue = "false") Boolean isCurrentUser) {
        List<ProjectListByManagerResponseDTO> project = projectService.getProjectsByManager(isCurrentUser);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping("/project-request/{id}")
    public ResponseEntity<Object> deleteProjectRequest(@PathVariable Integer id) {
        projectService.deleteProjectRequest(id);
        String[] list = messageSource.getMessage("PROJECT_REQUEST_DELETED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        String[] list = messageSource.getMessage("PROJECT_DELETED", null, Locale.ENGLISH).split("-");
        return new ResponseEntity<>(new SuccessResponseDTO(list[0], list[1]), HttpStatus.OK);
    }

}
