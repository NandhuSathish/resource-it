/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.entity.criteriaquery;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.ResourceSkillWiseAllocationRequest;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author abdul.fahad
 */
public interface ProjectRepositoryCriteria {
    
    Page<Project> findFilteredProjectsWithPagination(
            @Param("projectName") String projectName,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("projectState") List<Integer> projectState,
            @Param("managerId") List<Integer> managerId,
            @Param("projectType") List<Integer> projectType,
            Pageable pageable);
    
    List<Project> findFilteredProjectsForDownload(
            @Param("projectName") String projectName,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("projectState") List<Integer> projectState,
            @Param("managerId") List<Integer> managerId,
            @Param("projectType") List<Integer> projectType);
    
    Page<ResourceSkillWiseAllocationRequest> findFilteredResourceSkillWiseAllocationWithPagination(
            @Param("approvalStatus") List<Integer> approvalStatus,
            @Param("projectIds") List<Integer> projectIds,
            @Param("departmentIds") List<Integer> departmentIds,
            @Param("managerIds") List<Integer> managerIds,
            Pageable pageable);
}
