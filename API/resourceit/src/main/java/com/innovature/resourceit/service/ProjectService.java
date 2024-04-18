package com.innovature.resourceit.service;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.requestdto.ProjectDownloadRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ProjectListingRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface ProjectService {

    ProjectRequestResponseDTO addProjectRequest(ProjectRequestRequestDTO dto);

    Page<ProjectRequestResponseDTO> listProjectRequests(Set<Byte> approvalStatus, Set<Integer> managerId, Set<Byte> projectType, Resource requestedBy, Pageable pageable);

    Boolean projectApprove(Integer projRequestId, ProjectRequestRequestDTO dto);

    void projectApproveById(Integer projRequestId, Byte status);

    PagedResponseDTO<ProjectListResponseDTO> getProjects(ProjectListingRequestDTO projectListingRequestDTO) throws ParseException;

    Boolean editProject(ProjectRequestRequestDTO dto);

    public void projectExcelDownload(HttpServletResponse response, ProjectDownloadRequestDTO projectDownloadRequestDTO) throws ParseException;

    ProjectResponseDTO getProjectById(Integer id);

    ProjectRequestResponseDTO getProjectRequestById(Integer id);

    List<ProjectListByManagerResponseDTO> getProjectsByManager(Boolean isCurrentUser);

    void rejectProject(Integer projRequestId, Byte approvalStatus);


    void deleteProjectRequest(Integer id);

    void deleteProject(Integer id);
}
