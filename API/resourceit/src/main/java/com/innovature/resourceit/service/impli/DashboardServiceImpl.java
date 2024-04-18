/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.criteriaquery.DashboardRepositoryCriteria;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.responsedto.DashboardChartResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.DashboardCountResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.DashboardService;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 *
 * @author abdul.fahad
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ParameterValidator parameterValidator;

    @Autowired
    MessageSource messageSource;

    @Autowired
    DashboardRepositoryCriteria dashboardRepositoryCriteria;

    @Override
    public DashboardCountResponseDTO fetchResourceAndProjectCount() {

        DashboardCountResponseDTO dashboardCountResponseDTO = new DashboardCountResponseDTO();
        long internalProjectCount = projectRepository.countByProjectTypeAndProjectStateAndStatus(Project.projectTypeValues.INTERNAL.value, Project.projectStateValues.IN_PROGRESS.value, Project.statusValues.ACTIVE.value);
        long billableProjectCount = projectRepository.countByProjectTypeAndProjectStateAndStatus(Project.projectTypeValues.BILLABLE.value, Project.projectStateValues.IN_PROGRESS.value, Project.statusValues.ACTIVE.value);
        dashboardCountResponseDTO.setInternalProjectCount(internalProjectCount);
        dashboardCountResponseDTO.setBillableProjectCount(billableProjectCount);
        dashboardCountResponseDTO.setTotalProjectCount(internalProjectCount + billableProjectCount);

        long benchResource = resourceRepository.countByAllocationStatusAndStatusAndNotRoleId(Resource.AllocationStatus.BENCH.value, Resource.Status.ACTIVE.value, 1);
        long externalResource = resourceRepository.countByAllocationStatusAndStatusAndNotRoleId(Resource.AllocationStatus.EXTERNAL.value, Resource.Status.ACTIVE.value, 1);
        long internalResource = resourceRepository.countByAllocationStatusAndStatusAndNotRoleId(Resource.AllocationStatus.INTERNAL.value, Resource.Status.ACTIVE.value, 1);
        dashboardCountResponseDTO.setBenchResourceCount(benchResource);
        dashboardCountResponseDTO.setBillableResourceCount(externalResource);
        dashboardCountResponseDTO.setInternalResourceCount(internalResource);
        dashboardCountResponseDTO.setTotalResourceCount(internalResource + externalResource + benchResource);

        return dashboardCountResponseDTO;
    }

    @Override
    public DashboardChartResponseDTO fetchResourceSkillLabelAndCount(List<String> allocationStatus, String flag,List<Integer> skillIds) {
        List<Integer> allocationStatusInt = parameterValidator.isNumbersNum("allocationStatus", allocationStatus);
        int flagInt = parameterValidator.isNumber("flag", flag);
        List<Integer> validAllocationStatus = Arrays.asList(0, 1, 2);
        List<Integer> validFlag = Arrays.asList(0, 1);
        if (!validFlag.contains(flagInt)) {
            throw new BadRequestException(messageSource.getMessage("INVALID_FLAG", null, Locale.ENGLISH));
        }

        allocationStatusInt.stream().forEach(x -> {
            if (!validAllocationStatus.contains(x)) {
                throw new BadRequestException(messageSource.getMessage("INVALID_ALLOCATION_STATUS", null, Locale.ENGLISH));
            }
        });

        DashboardChartResponseDTO dashboardChartResponseDTO;

        if (flagInt == 0) {
            //resource
            dashboardChartResponseDTO = dashboardRepositoryCriteria.findByDepartmentAllocationStatusIn(allocationStatusInt);
        } else {
            //skill
            dashboardChartResponseDTO = dashboardRepositoryCriteria.findByIdInSkillAllocationStatusIn(skillIds,allocationStatusInt);
        }

        return dashboardChartResponseDTO;
    }

}
