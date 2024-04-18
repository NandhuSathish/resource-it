package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceSkill;
import com.innovature.resourceit.entity.criteriaquery.BillabilityRepositoryCriteria;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.BillabilitySummaryRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.BillabilitySummaryResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ProjectAllocationResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceSkillResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.ResourceSkillRepository;
import com.innovature.resourceit.service.StatisticsService;
import com.innovature.resourceit.util.CommonFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpli implements StatisticsService {

    private static final String TOTAL_BILLING_DAYS = "totalBillingDays";
    private static final String TOTAL_BENCH_DAYS = "totalBenchDays";
    @Autowired
    private BillabilityRepositoryCriteria billabilityRepositoryCriteria;
    @Autowired
    private ParameterValidator parameterValidator;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CommonFunctions commonFunctions;
    @Autowired
    private ResourceServiceImpli resourceServiceImpli;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ResourceSkillRepository resourceSkillRepository;
    @Autowired
    private AllocationRepository allocationRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private CommonServiceForResourceDownloadAndListingImpli listingImpli;

    @Override
    public PagedResponseDTO<BillabilitySummaryResponseDTO> getAllResource(BillabilitySummaryRequestDTO requestDTO) {
        int pageNumber = parameterValidator.isNumber("pageNumber", requestDTO.getPageNumber());
        int pageSize = parameterValidator.isNumber("pageSize", requestDTO.getPageSize());


        String sortKey = requestDTO.getSortKey();
        boolean sortOrder = requestDTO.getSortOrder();

        List<Resource> unsortedResourceList = billabilityRepositoryCriteria.findResourceForBillabilityStatistic(requestDTO);
        List<Resource> resourceList = new ArrayList<>(unsortedResourceList);
        resourceList.sort(Comparator.comparing(Resource::getJoiningDate).reversed());
        List<BillabilitySummaryResponseDTO> billabilitySummaryResponseDTO = new ArrayList<>();
        resourceList.forEach(resource -> {
            BillabilitySummaryResponseDTO filterResponseDTO = new BillabilitySummaryResponseDTO();

            filterResponseDTO.setEmployeeId(resource.getEmployeeId());
            filterResponseDTO.setResourceId(resource.getId());
            filterResponseDTO.setName(resource.getName());
            filterResponseDTO.setDepartmentName(resource.getDepartment().getName());
            filterResponseDTO.setRole(resource.getRole().getName());
            filterResponseDTO.setAllocationStatus(resource.getAllocationStatus());
            filterResponseDTO.setJoiningDate(dateFormatter(resource.getJoiningDate()));
            filterResponseDTO.setStatus(resource.getStatus());
            LocalDate joiningDate = this.commonFunctions.convertDateToTimestamp(resource.getJoiningDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            filterResponseDTO.setWorkSpan((int) Period.between(joiningDate, LocalDate.now()).toTotalMonths());
            filterResponseDTO.setProjectName(null);
            List<ResourceSkill> rs = resourceSkillRepository
                    .findAllByResourceId(resource.getId());
            List<ResourceSkill> sortedResourceSkills = rs.stream()
                    .sorted(Comparator.comparing(ResourceSkill::getExperience).reversed())
                    .toList();

            filterResponseDTO.setResourceSkillResponseDTOs(
                    sortedResourceSkills.stream().map(ResourceSkillResponseDTO::new)
                            .toList());
            Date startDate = requestDTO.getStartDate();
            Date endDate = requestDTO.getEndDate();
            validateDates(startDate, endDate);
            if (startDate == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = calendar.getTime();
            }
            LocalDate localStartDate = commonFunctions.convertDateToTimestamp(startDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (joiningDate.isAfter(localStartDate)) {
                startDate = resource.getJoiningDate();
            }
            if (endDate == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate = calendar.getTime();
            }
            Set<Allocation> allocationSet = new HashSet<>(Stream.of(
                            allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(
                                    resource.getId(), endDate, startDate,
                                    Allocation.StatusValues.ACTIVE.value),
                            allocationRepository.findAllByResourceIdAndEndDateAndStatus(
                                    resource.getId(), startDate,
                                    Allocation.StatusValues.ACTIVE.value),
                            allocationRepository.findAllByResourceIdAndStartDateAndStatus(
                                    resource.getId(), endDate,
                                    Allocation.StatusValues.ACTIVE.value))
                    .flatMap(List::stream)
                    .toList());
            Set<Allocation> allocationSetWithOutSupport = new HashSet<>(Stream.of(
                            allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatusAndProjectProjectTypeNot(
                                    resource.getId(), endDate, startDate,
                                    Allocation.StatusValues.ACTIVE.value, Project.projectTypeValues.SUPPORT.value),
                            allocationRepository.findAllByResourceIdAndEndDateAndStatusAndProjectProjectTypeNot(
                                    resource.getId(), startDate,
                                    Allocation.StatusValues.ACTIVE.value, Project.projectTypeValues.SUPPORT.value),
                            allocationRepository.findAllByResourceIdAndStartDateAndStatusAndProjectProjectTypeNot(
                                    resource.getId(), endDate,
                                    Allocation.StatusValues.ACTIVE.value, Project.projectTypeValues.SUPPORT.value))
                    .flatMap(List::stream)
                    .toList());
            Set<String> projectNameSet = allocationSet.stream()
                    .map(Allocation::getProject)
                    .map(Project::getProjectId)
                    .map(projectId -> projectRepository.findByProjectIdAndStatus(projectId, Project.statusValues.ACTIVE.value))
                    .flatMap(Optional::stream)
                    .map(Project::getName)
                    .collect(Collectors.toSet());

            String projectNames = projectNameSet.isEmpty() ? null : String.join(", ", projectNameSet);
            filterResponseDTO.setProjectName(projectNames);
            Map<String, Integer> result = calculateBillingAndBenchedDays(startDate, endDate, allocationSetWithOutSupport);
            filterResponseDTO.setTotalExperience(listingImpli.getExperienceInStringFormat(resource.getExperience()));
            filterResponseDTO.setBillableDays(result.get(TOTAL_BILLING_DAYS));
            filterResponseDTO.setBenchDays(result.get(TOTAL_BENCH_DAYS));
            billabilitySummaryResponseDTO.add(filterResponseDTO);
        });
        List<BillabilitySummaryResponseDTO> filterBillableDays = billabilitySummaryResponseDTO.stream().filter(resource -> resource.getBillableDays() != 0 || resource.getBenchDays() != 0).toList();
        List<BillabilitySummaryResponseDTO> getSortValue = sortData(filterBillableDays, sortKey, sortOrder);
        int start = pageNumber * pageSize;
        int end = Math.min(start + pageSize, getSortValue.size());
        List<BillabilitySummaryResponseDTO> pagedList = getSortValue.subList(start, end);
        return new PagedResponseDTO<>(
                pagedList,
                (int) Math.ceil((double) getSortValue.size() / pageSize),
                getSortValue.size(),
                pageNumber);

    }

    private void validateDates(Date startDate, Date endDate) {
        if (startDate != null && endDate != null && (startDate.after(endDate))) {
            throw new BadRequestException(messageSource.getMessage("INVALID_START_END_DATE_SUMMARY", null, Locale.ENGLISH));

        }
    }

    @Override
    public BillabilitySummaryResponseDTO getResourceAnalysis(Integer id, Date startDate, Date endDate) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new BadRequestException(
                messageSource.getMessage("USER_NOT_FOUND", null,
                        Locale.ENGLISH)));
        LocalDate joiningDate = this.commonFunctions.convertDateToTimestamp(resource.getJoiningDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (startDate == null) {
            startDate = resource.getJoiningDate();
        }
        if (joiningDate.isAfter(commonFunctions.convertDateToTimestamp(startDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            startDate = resource.getJoiningDate();
        }
        if (endDate == null) {
            endDate = new Date();
        }
        Set<Allocation> allocationSet = new HashSet<>(Stream.of(
                        allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatusAndProjectProjectTypeNot(
                                resource.getId(), endDate, startDate,
                                Allocation.StatusValues.ACTIVE.value, Project.projectTypeValues.SUPPORT.value),
                        allocationRepository.findAllByResourceIdAndEndDateAndStatusAndProjectProjectTypeNot(
                                resource.getId(), startDate,
                                Allocation.StatusValues.ACTIVE.value, Project.projectTypeValues.SUPPORT.value),
                        allocationRepository.findAllByResourceIdAndStartDateAndStatusAndProjectProjectTypeNot(
                                resource.getId(), endDate,
                                Allocation.StatusValues.ACTIVE.value, Project.projectTypeValues.SUPPORT.value))
                .flatMap(List::stream)
                .toList());
        BillabilitySummaryResponseDTO filterResponseDTO = new BillabilitySummaryResponseDTO();

        filterResponseDTO.setEmployeeId(resource.getEmployeeId());
        filterResponseDTO.setResourceId(resource.getId());
        filterResponseDTO.setName(resource.getName());
        filterResponseDTO.setDepartmentName(resource.getDepartment().getName());
        filterResponseDTO.setRole(resource.getRole().getName());
        filterResponseDTO.setAllocationStatus(resource.getAllocationStatus());
        filterResponseDTO.setJoiningDate(dateFormatter(resource.getJoiningDate()));
        filterResponseDTO.setWorkSpan((int) Period.between(joiningDate, LocalDate.now()).toTotalMonths());
        filterResponseDTO.setProjectName(null);
        filterResponseDTO.setStatus(resource.getStatus());
        List<ResourceSkill> rs = resourceSkillRepository
                .findAllByResourceId(resource.getId());
        List<ResourceSkill> sortedResourceSkills = rs.stream()
                .sorted(Comparator.comparing(ResourceSkill::getExperience).reversed())
                .toList();

        filterResponseDTO.setResourceSkillResponseDTOs(
                sortedResourceSkills.stream().map(ResourceSkillResponseDTO::new)
                        .toList());
        Map<String, Integer> result = calculateBillingAndBenchedDays(startDate, endDate, allocationSet);
        filterResponseDTO.setTotalExperience(listingImpli.getExperienceInStringFormat(resource.getExperience()));
        filterResponseDTO.setBillableDays(result.get(TOTAL_BILLING_DAYS));
        filterResponseDTO.setBenchDays(result.get(TOTAL_BENCH_DAYS));
        return filterResponseDTO;
    }

    @Override
    public PagedResponseDTO<ProjectAllocationResponseDTO> getProjectAllocationDetails(Integer id, int page, int size,
                                                                                      Date startDate, Date endDate) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new BadRequestException(
                messageSource.getMessage("USER_NOT_FOUND", null,
                        Locale.ENGLISH)));
        LocalDate joiningDate = this.commonFunctions.convertDateToTimestamp(resource.getJoiningDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        startDate = calculateStartDate(resource, startDate, joiningDate);
        endDate = calculateEndDate(endDate);
        Set<Allocation> allocationSet = new HashSet<>(Stream.of(
                        allocationRepository.findAllByResourceIdAndStartDateBeforeAndEndDateAfterAndStatus(
                                resource.getId(), endDate, startDate,
                                Allocation.StatusValues.ACTIVE.value),
                        allocationRepository.findAllByResourceIdAndEndDateAndStatus(
                                resource.getId(), startDate,
                                Allocation.StatusValues.ACTIVE.value),
                        allocationRepository.findAllByResourceIdAndStartDateAndStatus(
                                resource.getId(), endDate,
                                Allocation.StatusValues.ACTIVE.value))
                .flatMap(List::stream)
                .toList());
        List<Allocation> allocationList = new ArrayList<>(allocationSet);
        allocationList.sort(Comparator.comparing(Allocation::getStartDate));
        List<ProjectAllocationResponseDTO> responseDTOList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date previousEndDate = startDate; // Initialize previousEndDate to startDate
        int count = 0;
        for (Allocation allocation : allocationList) {
            count++;
            Date allocationStartDate = allocation.getStartDate();
            Date allocationEndDate = allocation.getEndDate();
            // Check for gaps between previous allocation and current adjusted allocation
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(allocationStartDate);
            calendar1.add(Calendar.DAY_OF_MONTH, -1);
            Date modifiedStartDate = calendar1.getTime();
            if (previousEndDate.before(modifiedStartDate)) {
                // If there's a gap, denote it as bench
                ProjectAllocationResponseDTO benchDTO = new ProjectAllocationResponseDTO();
                benchDTO.setResourceId(id);
                benchDTO.setEmployeeId(resource.getEmployeeId());
                benchDTO.setResourceName(resource.getName());
                benchDTO.setProjectName("Bench");
                benchDTO.setProjectCode("INV_DLY_BENCH_001");
                benchDTO.setProjectType(Project.projectTypeValues.BENCH.value);
                benchDTO.setStatus(resource.getStatus());
                //for handling overlapping of bench days
                if (count != 1) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(previousEndDate);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    previousEndDate = calendar.getTime();
                }
                benchDTO.setStartDate(dateFormat.format(previousEndDate));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(allocationStartDate);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                Date allocationStartDateModified = calendar.getTime();
                benchDTO.setEndDate(dateFormat.format(allocationStartDateModified));
                responseDTOList.add(benchDTO);
            }

            // Create DTO for the current allocation
            ProjectAllocationResponseDTO dto = new ProjectAllocationResponseDTO();
            dto.setResourceId(id);
            dto.setEmployeeId(resource.getEmployeeId());
            dto.setResourceName(resource.getName());
            dto.setProjectName(allocation.getProject().getName());
            dto.setStatus(resource.getStatus());
            dto.setProjectCode(allocation.getProject().getProjectCode());
            dto.setProjectType(allocation.getProject().getProjectType());
            dto.setStartDate(dateFormat.format(allocationStartDate));
            dto.setEndDate(dateFormat.format(allocationEndDate));
            responseDTOList.add(dto);

            previousEndDate = commonFunctions.convertDateToTimestamp(allocationEndDate); // Update previousEndDate
        }

        // Handle the gap after the last allocation until endDate
        if (previousEndDate.before(endDate) || (count == 0 && previousEndDate.equals(endDate))) {
            ProjectAllocationResponseDTO benchDTO = new ProjectAllocationResponseDTO();
            benchDTO.setResourceId(id);
            benchDTO.setEmployeeId(resource.getEmployeeId());
            benchDTO.setResourceName(resource.getName());
            benchDTO.setProjectName("Bench");
            benchDTO.setStatus(resource.getStatus());

            benchDTO.setProjectCode("INV_DLY_BENCH_001");
            benchDTO.setProjectType(Project.projectTypeValues.BENCH.value);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(previousEndDate);
            if (count != 0) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            previousEndDate = calendar.getTime();
            benchDTO.setStartDate(dateFormat.format(previousEndDate));
            benchDTO.setEndDate(dateFormat.format(endDate));
            responseDTOList.add(benchDTO);
        }
        Collections.reverse(responseDTOList);
        int start = page * size;
        int end = Math.min(start + size, responseDTOList.size());
        List<ProjectAllocationResponseDTO> pagedList = responseDTOList.subList(start, end);

        return new PagedResponseDTO<>(
                pagedList,
                (int) Math.ceil((double) responseDTOList.size() / size),
                responseDTOList.size(),
                page);
    }

    private Date calculateEndDate(Date endDate) {
        if (endDate == null || LocalDate.now().isEqual(commonFunctions.convertDateToTimestamp(endDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            endDate = new Date();
        } else {
            endDate = commonFunctions.convertDateToTimestamp(endDate);

        }
        return endDate;
    }

    private Date calculateStartDate(Resource resource, Date startDate, LocalDate joiningDate) {
        if (startDate == null) {
            startDate = resource.getJoiningDate();
        } else {
            startDate = commonFunctions.convertDateToTimestamp(startDate);
        }
        if (joiningDate.isAfter(commonFunctions.convertDateToTimestamp(startDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            startDate = resource.getJoiningDate();
        }
        return startDate;
    }

    private Map<String, Integer> calculateBillingAndBenchedDays(Date startDate, Date endDate,
                                                                Set<Allocation> billingPeriods) {
        int totalBillingDays = 0;
        int totalBenchedDays = 0;
        for (LocalDate date = CommonFunctions.convertToLocalDate(startDate); date
                .isBefore(CommonFunctions.convertToLocalDate(endDate).plusDays(1));
             date = date.plusDays(1)) {
            boolean isBillingDay = false;
            for (Allocation allocation : billingPeriods) {
                LocalDate startLocalDate = CommonFunctions
                        .convertToLocalDate(commonFunctions.convertDateToTimestamp(allocation.getStartDate()));
                LocalDate endLocalDate = CommonFunctions
                        .convertToLocalDate(commonFunctions.convertDateToTimestamp(allocation.getEndDate()));
                if (startLocalDate != null && endLocalDate != null
                        && (!date.isBefore(startLocalDate) && !date.isAfter(endLocalDate))) {
                    isBillingDay = true;
                    break;
                }
            }

            if (isBillingDay) {
                totalBillingDays++;
            } else {
                totalBenchedDays++;
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put(TOTAL_BILLING_DAYS, totalBillingDays);
        result.put(TOTAL_BENCH_DAYS, totalBenchedDays);
        return result;
    }

    public List<BillabilitySummaryResponseDTO> sortData(List<BillabilitySummaryResponseDTO> unSortedList, String
            sortValue, Boolean sortOrder) {
        List<BillabilitySummaryResponseDTO> list = new ArrayList<>(unSortedList);
        if (sortValue == null) {
            return list;
        }
        if (sortValue.equalsIgnoreCase("billableDays")) {
            list.sort(Comparator.comparing(BillabilitySummaryResponseDTO::getBillableDays));
            if (Boolean.FALSE.equals(sortOrder)) {
                Collections.reverse(list);
            }
            return list;
        } else if (sortValue.equalsIgnoreCase("benchDays")) {
            list.sort(Comparator.comparing(BillabilitySummaryResponseDTO::getBenchDays));
            if (Boolean.FALSE.equals(sortOrder)) {
                Collections.reverse(list);
            }
            return list;
        } else {
            return list;
        }
    }

    public String dateFormatter(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

}
