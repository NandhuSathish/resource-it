package com.innovature.resourceit.util;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.dto.requestdto.ProjectRequestRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.AllocationConflictsByResourceResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

@Component
public class CommonFunctions {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ResourceAllocationRequestRepository resourceAllocationRequestRepository;
    @Autowired
    private AllocationRepository allocationRepository;
    @Autowired
    private HolidayCalendarRepository holidayCalendarRepository;
    @Autowired
    private ResourceSkillWiseAllocationRequestRepository resourceSkillWiseAllocationRequestRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonFunctions.class);

    public Boolean checkProjectChanges(ProjectRequestRequestDTO dto) {
        List<String> changedFields = new ArrayList<>();

        if (dto.getProjectId() != null) {
            Project project = projectRepository
                    .findByProjectIdAndStatusNot(dto.getProjectId(), Project.statusValues.DELETED.value)
                    .orElseThrow(() -> new BadRequestException(
                            messageSource.getMessage("PROJECT_NOT_FOUND", null, Locale.ENGLISH)));
            checkFieldChange("projectCode", dto.getProjectCode(), project.getProjectCode(), changedFields);
            checkFieldChange("name", dto.getName(), project.getName(), changedFields);
            checkFieldChange("projectType", dto.getProjectType(), project.getProjectType(), changedFields);
            checkFieldChange("clientName", dto.getClientName(), project.getClientName(), changedFields);
            checkFieldChange("manDay", dto.getManDay(), project.getManDay(), changedFields);
            checkFieldChange("startDate", dto.getStartDate(), project.getStartDate(), changedFields);
            checkFieldChange("endDate", dto.getEndDate(), project.getEndDate(), changedFields);
        }
        return changedFields.isEmpty();
    }

    void checkFieldChange(String fieldName, Object oldValue, Object newValue, List<String> changedFields) {
        if (!Objects.equals(oldValue, newValue)) {
            changedFields.add(fieldName);
        }
    }

    public Boolean checkRequestConflicts(Date startDate, Date endDate, Integer resourceId, Integer projectId) {
        Set<ResourceAllocationRequest> conflictingRequestsSet = new HashSet<>();
        List<ResourceAllocationRequest> conflictingRequests = resourceAllocationRequestRepository
                .findByResourceIdAndApprovalFlowNotInAndStartDateBeforeAndEndDateAfterAndProjectProjectIdAndStatus(resourceId,
                        List.of(ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value, ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value), endDate, startDate, projectId, ResourceAllocationRequest.StatusValues.ACTIVE.value);
        List<ResourceAllocationRequest> conflictingRequestsWithStartDate = resourceAllocationRequestRepository
                .findByResourceIdAndApprovalFlowNotInAndStartDateAndProjectProjectIdAndStatus(resourceId,
                        List.of(ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value, ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value), endDate, projectId, ResourceAllocationRequest.StatusValues.ACTIVE.value);
        List<ResourceAllocationRequest> conflictingRequestsWithEndDate = resourceAllocationRequestRepository
                .findByResourceIdAndApprovalFlowNotInAndEndDateAndProjectProjectIdAndStatus(resourceId,
                        List.of(ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value, ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value), startDate, projectId, ResourceAllocationRequest.StatusValues.ACTIVE.value);
        conflictingRequestsSet.addAll(conflictingRequests);
        conflictingRequestsSet.addAll(conflictingRequestsWithStartDate);
        conflictingRequestsSet.addAll(conflictingRequestsWithEndDate);
        return conflictingRequestsSet.isEmpty();
    }

    public List<AllocationConflictsByResourceResponseDTO> checkAllocationConflicts(
            List<Integer> resourceIdList, Date startDate, Date endDate, Integer allocationId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Set<String> uniqueEntries = new HashSet<>();
        return resourceIdList.stream().map(resourceId -> {
                    List<Allocation> conflictingAllocations = allocationRepository
                            .findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(
                                    resourceId, endDate, startDate,
                                    Allocation.StatusValues.ACTIVE.value);

// Include the case where the requested allocation starts or ends exactly at the start or end of an existing allocation
                    List<Allocation> overlappingStartAllocations = allocationRepository
                            .findAllByResourceIdAndEndDateAndStatus(
                                    resourceId, startDate, Allocation.StatusValues.ACTIVE.value);

                    List<Allocation> overlappingEndAllocations = allocationRepository
                            .findAllByResourceIdAndStartDateAndStatus(
                                    resourceId, endDate,
                                    Allocation.StatusValues.ACTIVE.value);


                    conflictingAllocations.addAll(overlappingStartAllocations);
                    conflictingAllocations.addAll(overlappingEndAllocations);
                    List<Allocation> conflictingAllocationsRevised = conflictingAllocations.stream().filter(t -> !Objects.equals(t.getId(), allocationId)).toList();
                    return conflictingAllocationsRevised.stream().map(allocation -> {
                        AllocationConflictsByResourceResponseDTO dto = new AllocationConflictsByResourceResponseDTO();
                        dto.setResourceName(allocation.getResource().getName());
                        dto.setProjectCode(allocation.getProject().getName());
                        dto.setProjectName(allocation.getProject().getName());
                        dto.setStartDate(dateFormat.format(allocation.getStartDate()));
                        dto.setEndDate(dateFormat.format(allocation.getEndDate()));
                        String uniqueIdentifier = dto.getResourceName() + dto.getProjectName() + dto.getStartDate() + dto.getEndDate();

                        if (uniqueEntries.add(uniqueIdentifier)) {
                            return dto;
                        } else {
                            return null; // Skip duplicate entry
                        }
                    }).filter(Objects::nonNull).toList();

                }).flatMap(List::stream) // Flatten the list of lists
                .toList();

    }

    public int calculateDaysBetween(Date startDate, Date endDate) {

        int count = 0;
        LocalDate startLocalDate = convertToLocalDate(startDate);
        LocalDate endLocalDate = convertToLocalDate(endDate);
        while (startLocalDate.isBefore(endLocalDate) || startLocalDate.isEqual(endLocalDate)) {
            startLocalDate = startLocalDate.plusDays(1);
            count++;
        }
        return count;
    }

    public int calculateBusinessDays(Date startDate, Date endDate) {

        int count = 0;
        LocalDate startLocalDate = convertToLocalDate(startDate);
        LocalDate endLocalDate = convertToLocalDate(endDate);
        while (startLocalDate.isBefore(endLocalDate) || startLocalDate.isEqual(endLocalDate)) {

            if (!isWeekend(startLocalDate) && !isHoliday(startLocalDate)) {
                count++;
            }
            startLocalDate = startLocalDate.plusDays(1);
        }
        return count;
    }


    public boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public boolean isHoliday(LocalDate date) {
        Optional<List<HolidayCalendar>> holidayListWithDesc = holidayCalendarRepository.findByYear(LocalDate.now().getYear(), Sort.by(Sort.Direction.ASC, "date"));
        if (holidayListWithDesc.isPresent()) {
            List<LocalDate> holidayList = holidayListWithDesc.get().stream().map(ob -> convertToLocalDate(ob.getDate()))
                    .toList();
            return holidayList.contains(date);
        }
        return false;
    }

    public static LocalDate convertToLocalDate(Date date) {

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Date convertDateToTimestamp(Date d) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(d);
        Date date;
        try {
            date = format.parse(dateString);
        } catch (ParseException ex) {
            throw new BadRequestException(messageSource.getMessage("DATE_FORMAT_ERROR", null, Locale.ENGLISH));
        }
        return date;
    }

    public int calculateConflictDays(Integer resourceId, Allocation allocation, Date proposedStartDate, Date proposedEndDate) {
        LocalDate localProposedStartDate = (proposedStartDate != null) ? convertToLocalDate(convertDateToTimestamp(proposedStartDate)) : null;
        LocalDate localProposedEndDate = (proposedEndDate != null) ? convertToLocalDate(convertDateToTimestamp(proposedEndDate)) : null;

        Set<Allocation> finalAllocationList = new HashSet<>(Stream.of(
                        allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(
                                resourceId, proposedEndDate, proposedStartDate,
                                Allocation.StatusValues.ACTIVE.value),
                        allocationRepository.findAllByResourceIdAndEndDateAndStatus(
                                resourceId, proposedStartDate,
                                Allocation.StatusValues.ACTIVE.value),
                        allocationRepository.findAllByResourceIdAndStartDateAndStatus(
                                resourceId, proposedEndDate,
                                Allocation.StatusValues.ACTIVE.value))
                .flatMap(List::stream)
                .toList());

        finalAllocationList.removeIf(value -> allocation != null && value.getId().equals(allocation.getId()));

        return finalAllocationList.stream()
                .mapToInt(value -> calculateDaysBetween(
                        convertDateToTimestamp(getStartDate(value, localProposedStartDate, proposedStartDate)),
                        convertDateToTimestamp(getEndDate(value, localProposedEndDate, proposedEndDate))))
                .sum();
    }

    public Date getStartDate(Allocation allocation, LocalDate localProposedStartDate, Date proposedStartDate) {
        LocalDate localAllocationStartDate = convertToLocalDate(convertDateToTimestamp(allocation.getStartDate()));
        if ((proposedStartDate != null)) {
            if (localAllocationStartDate.isAfter(localProposedStartDate)) return allocation.getStartDate();
            return proposedStartDate;
        } else {
            return allocation.getStartDate();
        }
    }

    public Date getEndDate(Allocation allocation, LocalDate localProposedEndDate, Date proposedEndDate) {
        LocalDate localAllocationEndDate = convertToLocalDate(convertDateToTimestamp(allocation.getEndDate()));
        if ((proposedEndDate != null)) {
            if (localAllocationEndDate.isBefore(localProposedEndDate)) return allocation.getEndDate();
            return proposedEndDate;
        } else {
            return allocation.getEndDate();
        }
    }


    public void resourceDelete(Resource deleteResource) {
        LocalDate currentDate = LocalDate.now();
        List<Allocation> resourceAllocations = allocationRepository.findAllByResourceIdAndStatus(deleteResource.getId(), Allocation.StatusValues.ACTIVE.value);

        if (!resourceAllocations.isEmpty()) {
            for (Allocation allocation : resourceAllocations) {
                LocalDate allocationStartDate = this.convertDateToTimestamp(allocation.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (allocationStartDate.isAfter(currentDate)) {
                    allocation.setIsRemoved(Allocation.IsRemoved.YES.value);
                    allocation.setStatus(Allocation.StatusValues.DELETED.value);
                    allocation.setAllocationExpiry(Allocation.AllocationExpiryValues.EXPIRED.value);
                } else {
                    allocation.setIsRemoved(Allocation.IsRemoved.YES.value);
                    if (allocation.getEndDate().after(new Date())) {
                        allocation.setEndDate(new Date());
                    }
                    allocation.setAllocationExpiry(Allocation.AllocationExpiryValues.EXPIRED.value);
                }
            }
            allocationRepository.saveAllAndFlush(resourceAllocations);
        }
        List<ResourceAllocationRequest> resourceAllocationRequests = resourceAllocationRequestRepository.findByResourceIdAndStatus(deleteResource.getId(), ResourceAllocationRequest.StatusValues.ACTIVE.value);
        if (!resourceAllocationRequests.isEmpty()) {
            List<ResourceAllocationRequest> resourceAllocationRequestList = new ArrayList<>();
            for (ResourceAllocationRequest requests : resourceAllocationRequests) {
                requests.setStatus(ResourceAllocationRequest.StatusValues.DELETED.value);
                resourceAllocationRequestList.add(requests);
            }
            resourceAllocationRequestRepository.saveAllAndFlush(resourceAllocationRequestList);
        }
    }

    public void removeAllocationRequests(Integer projectId, Date newStartDateInDate, Date newEndDateInDate) {
        try {
            LocalDate newStartDate = this.convertDateToTimestamp(newStartDateInDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate newEndDate = this.convertDateToTimestamp(newEndDateInDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            List<ResourceAllocationRequest> resourceWiseAllocationRequests = resourceAllocationRequestRepository.findAllByProjectProjectIdAndStatusAndApprovalFlowNotIn(
                    projectId,
                    ResourceAllocationRequest.StatusValues.ACTIVE.value,
                    List.of(ResourceAllocationRequest.ApprovalFlowValues.REJECTED.value, ResourceAllocationRequest.ApprovalFlowValues.APPROVED.value)
            );

            List<ResourceAllocationRequest> resourceWiseAllocationRequestsToRemove = resourceWiseAllocationRequests.stream()
                    .filter(allocationRequest -> {
                        LocalDate allocationStartDate = this.convertDateToTimestamp(allocationRequest.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate allocationEndDate = this.convertDateToTimestamp(allocationRequest.getEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return allocationStartDate.isBefore(newStartDate) || allocationEndDate.isAfter(newEndDate);
                    })
                    .map(allocationRequest -> {
                        allocationRequest.setStatus(ResourceAllocationRequest.StatusValues.DELETED.value);
                        if (allocationRequest.getAllocation() != null) {
                            Allocation allocation = allocationRequest.getAllocation();
                            allocation.setIsEdited(Allocation.IsEditedValues.NOT_EDITED.value);
                            allocationRepository.save(allocation);
                        }
                        return allocationRequest;
                    })
                    .toList();
            List<ResourceSkillWiseAllocationRequest> skillWiseAllocationRequests = resourceSkillWiseAllocationRequestRepository.findAllByProjectProjectIdAndStatusAndApprovalFlowNot(
                    projectId,
                    ResourceSkillWiseAllocationRequest.StatusValues.ACTIVE.value,
                    ResourceSkillWiseAllocationRequest.ApprovalFlowValues.REJECTED.value
            );

            List<ResourceSkillWiseAllocationRequest> skillWiseAllocationRequestsToRemove = skillWiseAllocationRequests.stream()
                    .filter(allocationRequest -> {
                        LocalDate allocationStartDate = this.convertDateToTimestamp(allocationRequest.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate allocationEndDate = this.convertDateToTimestamp(allocationRequest.getEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return allocationStartDate.isBefore(newStartDate) || allocationEndDate.isAfter(newEndDate);
                    })
                    .map(allocationRequest -> {
                        allocationRequest.setStatus(ResourceSkillWiseAllocationRequest.StatusValues.DELETED.value);
                        return allocationRequest;
                    })
                    .toList();

            resourceAllocationRequestRepository.saveAll(resourceWiseAllocationRequestsToRemove);
            resourceSkillWiseAllocationRequestRepository.saveAll(skillWiseAllocationRequestsToRemove);
        } catch (Exception e) {
            LOGGER.error("Error in removing allocation requests on project edit. Error", e);

        }

    }

    public void adjustAllocationDates(Integer projectId, Date newStartDateInDate, Date newEndDateInDate) {
        try {
            LocalDate newStartDate = this.convertDateToTimestamp(newStartDateInDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate newEndDate = this.convertDateToTimestamp(newEndDateInDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            List<Allocation> allocations = allocationRepository.findByProjectProjectIdAndStatus(projectId, Allocation.StatusValues.ACTIVE.value);

            for (Allocation allocation : allocations) {
                LocalDate allocationStartDate = this.convertDateToTimestamp(allocation.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate allocationEndDate = this.convertDateToTimestamp(allocation.getEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate adjustedStartDate = allocationStartDate.isBefore(newStartDate)
                        ? newStartDate
                        : allocationStartDate;

                LocalDate adjustedEndDate = allocationEndDate.isAfter(newEndDate)
                        ? newEndDate
                        : allocationEndDate;

                // If allocation period is after the new project period, remove the allocation
                if (adjustedStartDate.isAfter(newEndDate) || adjustedEndDate.isBefore(newStartDate)) {
                    allocation.setStatus(Allocation.StatusValues.DELETED.value);
                    allocationRepository.save(allocation);
                } else {
                    // Adjust allocation start and end dates
                    Date startDate = Date.from(adjustedStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Date endDate = Date.from(adjustedEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    allocation.setStartDate(startDate);
                    allocation.setEndDate(endDate);
                    allocationRepository.save(allocation);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error in adjusting allocation dates on project edit. Error", e);
        }

    }

    @Async
    public void deleteWrongAllocations() {
        List<Allocation> allocationList = allocationRepository.findAll();
        List<Allocation> allocationListToRemove = allocationList.stream().filter(allocation -> {
            LocalDate allocationStartDate = this.convertDateToTimestamp(allocation.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate allocationEndDate = this.convertDateToTimestamp(allocation.getEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return allocationStartDate.isAfter(allocationEndDate);
        }).map(allocation -> {
            allocation.setStatus(Allocation.StatusValues.DELETED.value);
            return allocation;
        }).toList();
        allocationRepository.saveAll(allocationListToRemove);
    }

    public void updateResourceAllocationStatus() {
        List<Resource> resources = resourceRepository.findAll();
        List<Resource> updatingResources = new ArrayList<>();
        for (Resource resource : resources) {
            List<Allocation> allocationList = allocationRepository.findByResourceIdAndAllocationExpiryInAndStatusAndProjectProjectTypeNot(resource.getId(), List.of(Allocation.AllocationExpiryValues.ON_GOING.value), Allocation.StatusValues.ACTIVE.value, Project.projectTypeValues.SUPPORT.value);
            if (!allocationList.isEmpty()) {
                handleAllocationStatus(allocationList, resource);
            } else {
                resource.setAllocationStatus(Resource.AllocationStatus.BENCH.value);
            }
            updatingResources.add(resource);
        }
        resourceRepository.saveAll(updatingResources);
    }

    public void handleAllocationStatus(List<Allocation> allocationList, Resource resource) {
        int billable = 0;
        int internal = 0;
        for (Allocation allocation : allocationList) {
            Optional<Project> optionalProject = projectRepository.findByProjectIdAndStatus(allocation.getProject().getProjectId(), Project.statusValues.ACTIVE.value);
            if (optionalProject.isPresent()) {
                if (Objects.equals(optionalProject.get().getProjectType(), Project.projectTypeValues.INTERNAL.value)) {
                    internal++;
                } else if (Objects.equals(optionalProject.get().getProjectType(), Project.projectTypeValues.BILLABLE.value)) {
                    billable++;
                }
            }
        }
        if (billable > 0) {
            resource.setAllocationStatus(Resource.AllocationStatus.EXTERNAL.value);
        } else if (internal > 0) {
            resource.setAllocationStatus(Resource.AllocationStatus.INTERNAL.value);
        } else
            resource.setAllocationStatus(Resource.AllocationStatus.BENCH.value);
    }

    // This method is created to fix the issue of with @Transactional and @Async
    public void scheduleUpdateAllocationStatusExpiryAndTeamSize() {
        updateAllocationStatusExpiryAndTeamSize();
    }

    @Async
    public void updateAllocationStatusExpiryAndTeamSize() {
        try {
            List<Project> projects = projectRepository.findAllByStatus(Project.statusValues.ACTIVE.value);
            List<Project> projectList = new ArrayList<>();
            projects.forEach(project -> {
                LocalDate currentDate = LocalDate.now();
                List<Allocation> allocations = new ArrayList<>();
                List<Resource> resources = new ArrayList<>();
                List<Allocation> allocationList = allocationRepository.findByProjectProjectIdAndAllocationExpiryInAndStatus(project.getProjectId(), Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value, Allocation.AllocationExpiryValues.ON_GOING.value), Allocation.StatusValues.ACTIVE.value);
                allocationList.forEach(allocation -> {
                    LocalDate allocationStartDate = this.convertDateToTimestamp(allocation.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate allocationEndDate = this.convertDateToTimestamp(allocation.getEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    Optional<Resource> optionalResource = resourceRepository.findById(allocation.getResource().getId());
                    optionalResource.ifPresent(resource -> {
                        if (allocationEndDate.isBefore(currentDate)) {
                            allocation.setAllocationExpiry(Allocation.AllocationExpiryValues.EXPIRED.value);
                            if (!allocation.getProject().getProjectType().equals(Project.projectTypeValues.SUPPORT.value)) {
                                setLastWorkedProjectDate(resource, allocation, allocationEndDate, resources);
                            }
                            relievedUserCheck(allocation, resource);
                            allocations.add(allocation);
                        } else if (allocationStartDate.isAfter(currentDate)) {
                            handleAllocationStartDateAfterCurrentDate(allocation, resource);
                            resources.add(resource);
                            allocations.add(allocation);
                        } else {
                            allocation.setAllocationExpiry(Allocation.AllocationExpiryValues.ON_GOING.value);
                            handleIfAllocationIsNotSupport(allocation, resource, resources);
                            allocations.add(allocation);
                        }
                    });
                });
                allocationRepository.saveAllAndFlush(allocations);
                resourceRepository.saveAllAndFlush(resources);
                int teamSize = allocationRepository.countDistinctResourceIdByProjectProjectIdAndAllocationExpiryInAndStatus(project.getProjectId(), Arrays.asList(Allocation.AllocationExpiryValues.NOT_STARTED.value, Allocation.AllocationExpiryValues.ON_GOING.value), Allocation.StatusValues.ACTIVE.value);
                project.setTeamSize(teamSize);
                // Determine project state based on project start and end dates
                Project modifiedProject = checkProjectState(project);
                projectList.add(modifiedProject);
            });
            projectRepository.saveAllAndFlush(projectList);
            this.updateResourceAllocationStatus();
        } catch (Exception e) {
            LOGGER.error("Error occurred when updateAllocationStatusExpiryAndTeamSize,Error:", e);
        }

    }

    public void relievedUserCheck(Allocation allocation, Resource resource) {
        if (resource.getStatus() == Resource.Status.INACTIVE.value) {
            allocation.setIsRemoved(Allocation.IsRemoved.YES.value);
        }
    }

    public void setLastWorkedProjectDate(Resource resource, Allocation allocation, LocalDate allocationEndDate, List<Resource> resources) {
        if (resource.getLastWorkedProjectDate() != null) {
            LocalDate lastWorkedProjectDateLocal = this.convertDateToTimestamp(resource.getLastWorkedProjectDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (allocationEndDate.isAfter(lastWorkedProjectDateLocal)) {
                resource.setLastWorkedProjectDate(allocation.getEndDate());
                resource.setUpdatedDate(new Date());
            }
        }
        resources.add(resource);
    }

    public void handleAllocationStartDateAfterCurrentDate(Allocation allocation, Resource resource) {
        allocation.setAllocationExpiry(Allocation.AllocationExpiryValues.NOT_STARTED.value);
        Optional<Allocation> previousAllocation = allocationRepository.findTopByResourceIdAndProjectProjectTypeNotAndAllocationExpiryAndStatusOrderByEndDateDesc(allocation.getResource().getId(), Project.projectTypeValues.SUPPORT.value, Allocation.AllocationExpiryValues.EXPIRED.value, Allocation.StatusValues.ACTIVE.value);
        if (resource.getAllocationStatus().equals(Resource.AllocationStatus.BENCH.value)) {
            if (previousAllocation.isPresent()) {
                resource.setLastWorkedProjectDate(previousAllocation.get().getEndDate());
            } else {
                resource.setLastWorkedProjectDate(resource.getJoiningDate());
            }
        }
    }

    public void handleIfAllocationIsNotSupport(Allocation allocation, Resource resource, List<Resource> resources) {
        if (!allocation.getProject().getProjectType().equals(Project.projectTypeValues.SUPPORT.value)) {
            resource.setLastWorkedProjectDate(null);
            resource.setUpdatedDate(new Date());
            resources.add(resource);
        }
    }

    public Project checkProjectState(Project project) {
        LocalDate currentDate = LocalDate.now();
        LocalDate projectStartDate = this.convertDateToTimestamp(project.getStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate projectEndDate = this.convertDateToTimestamp(project.getEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (projectEndDate.isBefore(currentDate)) {
            project.setProjectState(Project.projectStateValues.COMPLETED.value);
        } else if (projectStartDate.isAfter(currentDate)) {
            project.setProjectState(Project.projectStateValues.NOT_STARTED.value);
        } else {
            project.setProjectState(Project.projectStateValues.IN_PROGRESS.value);
        }
        return project;
    }
}
