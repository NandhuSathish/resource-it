package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.*;
import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterSkillAndExperienceRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceSkillRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.ManagerListResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceListingResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ResourceResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.*;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.ResourceService;
import com.innovature.resourceit.util.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ResourceServiceImpli implements ResourceService {

    private static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    @Autowired
    ParameterValidator parameterValidator;
    @Autowired
    MessageSource messageSource;
    @Autowired
    CommonServiceForResourceDownloadAndListingImpli listingImpli;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    AllocationRepository allocationRepository;
    @Autowired
    ResourceAllocationRequestRepository resourceAllocationRequestRepository;
    @Autowired
    CommonFunctions commonFunctions;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ResourceSkillRepository resourceSkillRepository;

    public ResourceServiceImpli() {

    }

    public ResourceServiceImpli(MessageSource messageSource, ResourceRepository resourceRepository, SkillRepository skillRepository, ResourceSkillRepository resourceSkillRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository) {
        this.messageSource = messageSource;
        this.resourceRepository = resourceRepository;
        this.skillRepository = skillRepository;
        this.resourceSkillRepository = resourceSkillRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
    }

    public ResourceServiceImpli(MessageSource messageSource, ResourceRepository resourceRepository, RoleRepository roleRepository, ParameterValidator parameterValidator, CommonServiceForResourceDownloadAndListingImpli listingImpli) {
        this.messageSource = messageSource;
        this.resourceRepository = resourceRepository;
        this.roleRepository = roleRepository;
        this.parameterValidator = parameterValidator;
        this.listingImpli = listingImpli;
    }

    @Override
    @Transactional
    public void add(ResourceRequestDTO dto) {

        Department department = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(() -> new BadRequestException(messageSource.getMessage("DEPARTMENT_NOT_FOUND", null, Locale.ENGLISH)));
        Role role = roleRepository.findById(dto.getRole()).orElseThrow(() -> new BadRequestException(messageSource.getMessage("ROLE_NOT_FOUND", null, Locale.ENGLISH)));
        Optional<Resource> email = resourceRepository.findByEmailIgnoreCase(dto.getEmail());
        if (email.isPresent()) {
            throw new BadRequestException(messageSource.getMessage("EMAIL_ARE_SAME", null, Locale.ENGLISH));

        }
        Optional<Resource> optionalEmployee = resourceRepository.findByEmployeeId(dto.getEmployeeId());
        if (optionalEmployee.isPresent()) {
            throw new BadRequestException(messageSource.getMessage("DUPLICATE_EMPLOYEEIDS", null, Locale.ENGLISH));
        }
        Resource resource = resourceRepository.save(new Resource(dto, role, department));
        dto.getSkills().forEach(t -> {
            ResourceSkill resourceSkill = new ResourceSkill();
            resourceSkill.setResource(resource);
            resourceSkill.setSkill(skillRepository.findById(t.getSkillId()).orElseThrow(() -> new BadRequestException(messageSource.getMessage("INVALID_SKILL_ID", null, Locale.ENGLISH))));
            resourceSkill.setExperience(t.getExperience());
            resourceSkill.setProficiency(t.getProficiency());
            resourceSkillRepository.save(resourceSkill);
        });
    }

    @Override
    public PagedResponseDTO<ResourceListingResponseDTO> getResources(ResourceFilterRequestDTO requestDTO) {

        List<Integer> projectIdsInt = parameterValidator.isNumbersNum("projectId", requestDTO.getProjectIds());

        int pageNumber = parameterValidator.isNumber("pageNumber", requestDTO.getPageNumber());
        int pageSize = parameterValidator.isNumber("pageSize", requestDTO.getPageSize());


        String sortKey = getSortKey(requestDTO.getSortKey());
        boolean sortOrder = requestDTO.getSortOrder();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Boolean.FALSE.equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortKey));


        int numSkills = 5;
        int[] skills = new int[numSkills];
        int[] lowExperiences = new int[numSkills];
        int[] highExperiences = new int[numSkills];
        List<Byte>[] proficiencyStatusArray = new List[numSkills];


        processSkillAndExperience(requestDTO.getSkillAndExperiences(), skills, lowExperiences, highExperiences, proficiencyStatusArray);

        Page<Object[]> demoDTOs = getDemoDTOs(requestDTO, projectIdsInt, pageable, skills, lowExperiences, highExperiences, proficiencyStatusArray);

        return getResourceListResponseDTO(demoDTOs);
    }

    public void processSkillAndExperience(List<ResourceFilterSkillAndExperienceRequestDTO> modifiedSkillAndExperiences, int[] skills, int[] lowExperiences, int[] highExperiences, List<Byte>[] proficiencyStatusArray) {
        if (modifiedSkillAndExperiences == null || modifiedSkillAndExperiences.isEmpty()) {
            return;
        }

        int index = 0;

        for (ResourceFilterSkillAndExperienceRequestDTO newSkillAndExperience : modifiedSkillAndExperiences) {
            String skillIdStr = newSkillAndExperience.getSkillId();

            if (isEmptyOrUndefined(skillIdStr)) {
                continue;
            }

            int skillId = Integer.parseInt(skillIdStr);
            skills[index] = skillId;

            String skillMinValueStr = newSkillAndExperience.getSkillMinValue();
            String skillMaxValueStr = newSkillAndExperience.getSkillMaxValue();
            List<Byte> proficiencyStatus = newSkillAndExperience.getProficiency();
            if (proficiencyStatus == null || proficiencyStatus.isEmpty()) {
                proficiencyStatus = Arrays.asList(
                        ResourceSkill.proficiencyValues.BEGINNER.value,
                        ResourceSkill.proficiencyValues.INTERMEDIATE.value,
                        ResourceSkill.proficiencyValues.ADVANCED.value
                );
            }
            proficiencyStatusArray[index] = proficiencyStatusArray[index] != null ? proficiencyStatusArray[index] : proficiencyStatus;

            if (isNotEmpty(skillMinValueStr) && isNotEmpty(skillMaxValueStr)) {
                int skillMinValue = Integer.parseInt(skillMinValueStr);
                int skillMaxValue = Integer.parseInt(skillMaxValueStr);
                lowExperiences[index] = skillMinValue * 12;
                highExperiences[index] = (12 * skillMaxValue) ;
            } else {
                lowExperiences[index] = 0;
                highExperiences[index] = 1300;
            }
            index++;
        }
    }

    private boolean isEmptyOrUndefined(String str) {
        return str == null || str.isEmpty() || str.equals("undefined");
    }


    private boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public Page<Object[]> getDemoDTOs(ResourceFilterRequestDTO requestDTO, List<Integer> projectIdsInt, Pageable pageable, int[] skills, int[] lowExperiences, int[] highExperiences, List<Byte>[] proficiencyStatusArray) {
        int employeeId = parameterValidator.isNumber("employeeId", requestDTO.getEmployeeId());
        List<Integer> roleIdInt = parameterValidator.isNumbersNum("roleId", requestDTO.getRoleIds());
        List<Integer> departmentIdInt =
                parameterValidator.isNumbersNum("departmentId", requestDTO.getDepartmentIds());

        List<Integer> allocationStatusInt = parameterValidator.isNumbersNum("allocationStatus", requestDTO.getAllocationStatus());
        int lowerExp = parameterValidator.isExperienceNumber("lowExperience", requestDTO.getLowerExperience());
        int highExp = parameterValidator.isExperienceNumber("highExperience", requestDTO.getHighExperience());
        int status = parameterValidator.isNumber("status", requestDTO.getStatus());
        List<Integer> roleIds = getRoles(roleIdInt);

        listingImpli.checkLowerExpLessThanHighExp(lowerExp, highExp);
        for (int i = 0; i < proficiencyStatusArray.length; i++) {
            List<Byte> proficiencyStatusList = proficiencyStatusArray[i];

            if (proficiencyStatusList == null) {
                proficiencyStatusList = Arrays.asList(
                        ResourceSkill.proficiencyValues.BEGINNER.value,
                        ResourceSkill.proficiencyValues.INTERMEDIATE.value,
                        ResourceSkill.proficiencyValues.ADVANCED.value
                );
                proficiencyStatusArray[i] = proficiencyStatusList;
            }
        }

        Page<Object[]> demoDTOs;
        if (requestDTO.getSkillAndExperiences() == null || requestDTO.getSkillAndExperiences().isEmpty()) {
            if (projectIdsInt.isEmpty()) {
                demoDTOs = resourceRepository.findAllByAllFilters(requestDTO.getName(), departmentIdInt, employeeId, roleIds, lowerExp, highExp, (byte) status, allocationStatusInt, pageable);
            } else {
                demoDTOs = resourceRepository.findAllByAllFiltersWithProjectName(requestDTO.getName(), departmentIdInt, employeeId, roleIds, lowerExp, highExp, (byte) status, allocationStatusInt, projectIdsInt, pageable);
            }
        } else {
            if (projectIdsInt.isEmpty()) {
                demoDTOs = resourceRepository.findAllByAllFiltersWithSkillsAndExperience(requestDTO.getName(), departmentIdInt, employeeId, roleIds, lowerExp, highExp, (byte) status, allocationStatusInt, skills[0], lowExperiences[0], highExperiences[0], skills[1], lowExperiences[1], highExperiences[1], skills[2], lowExperiences[2], highExperiences[2], skills[3], lowExperiences[3], highExperiences[3], skills[4], lowExperiences[4], highExperiences[4], proficiencyStatusArray[0], proficiencyStatusArray[1], proficiencyStatusArray[2], proficiencyStatusArray[3], proficiencyStatusArray[4], pageable);
            } else {
                demoDTOs = resourceRepository.findAllByAllFiltersWithProjectNameSkillsAndExperience(requestDTO.getName(), departmentIdInt, employeeId, roleIds, lowerExp, highExp, (byte) status, allocationStatusInt, skills[0], lowExperiences[0], highExperiences[0], skills[1], lowExperiences[1], highExperiences[1], skills[2], lowExperiences[2], highExperiences[2], skills[3], lowExperiences[3], highExperiences[3], skills[4], lowExperiences[4], highExperiences[4], projectIdsInt, proficiencyStatusArray[0], proficiencyStatusArray[1], proficiencyStatusArray[2], proficiencyStatusArray[3], proficiencyStatusArray[4], pageable);
            }
        }

        return demoDTOs;
    }


    public List<Integer> getRoles(List<Integer> roleIdInt) {
        List<Integer> roleIds = new ArrayList<>();
        List<Role> roles = roleRepository.findAll();
        if (roleIdInt == null || roleIdInt.isEmpty()) {
            roleIds = roles.stream().filter(x -> !x.getName().equals(Resource.Roles.ADMIN.getValue())).map(Role::getId).toList();
        } else {
            roleIds.addAll(roleIdInt);
        }
        return roleIds;
    }

    public String getSortKey(String sortKey) {
        if (sortKey == null) {
            sortKey = "joining_date";
        } else {
            switch (sortKey) {
                case "empId":
                    sortKey = "employee_id";
                    break;
                case "resourceName":
                    sortKey = "name";
                    break;
                default:
                    sortKey = "joining_date";
            }
        }

        return sortKey;
    }

    public PagedResponseDTO<ResourceListingResponseDTO> getResourceListResponseDTO(Page<Object[]> demoDTOs) {

        List<ResourceListingResponseDTO> lists = new ArrayList<>();
        for (Object[] result : demoDTOs.getContent()) {
            ResourceListingResponseDTO filterRequestDTO = new ResourceListingResponseDTO();
            filterRequestDTO.setDepartmentId((int) result[12]);
            filterRequestDTO.setRole((int) result[11]);
            filterRequestDTO.setAllocationStatus(result[7] + "");
            filterRequestDTO.setProjectName(result[3] + "");
            filterRequestDTO.setDepartmentName(result[2] + "");
            filterRequestDTO.setEmail(result[6] + "");
            filterRequestDTO.setResourceSkillResponseDTOs(listingImpli.setResourceSkillResponse((int) result[0]));
            filterRequestDTO.setRoleName(result[8] + "");
            filterRequestDTO.setEmployeeId((int) result[10]);
            filterRequestDTO.setId((int) result[0]);
            Object lastWorkedDate = result[13];


            if (lastWorkedDate == null) {
                filterRequestDTO.setAging(0);
            } else {
                LocalDate dateFromResult14 = ((Date) lastWorkedDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int aging = (int) ChronoUnit.DAYS.between(dateFromResult14, LocalDate.now());
                filterRequestDTO.setAging(aging > 0 && (result[9].equals((byte) 1)) ? aging : 0);
            }
            try {
                filterRequestDTO.setJoiningDate(listingImpli.joiningDateStringFormat(result[5]));
            } catch (ParseException ex) {
                throw new BadRequestException(messageSource.getMessage("DATE_FORMAT_ERROR", null, Locale.ENGLISH));
            }
            filterRequestDTO.setName(result[1] + "");
            filterRequestDTO.setTotalExperience(listingImpli.getExperienceInStringFormat((int) result[4]));
            filterRequestDTO.setStatus((result[9].equals((byte) 1)) ? Resource.Status.ACTIVE.toString() : Resource.Status.INACTIVE.toString());
            lists.add(filterRequestDTO);
        }

        return new PagedResponseDTO<>(lists, demoDTOs.getTotalPages(), demoDTOs.getTotalElements(), demoDTOs.getNumber());
    }

    @Override
    public ResourceResponseDTO getResourceById(Integer id) {
        Resource resource = resourceRepository.findByIdAndStatus(id, Resource.Status.ACTIVE.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(RESOURCE_NOT_FOUND, null, Locale.ENGLISH)));
        return this.mapResourceToDTO(resource);
    }

    @Override
    @Transactional
    public void updateResource(Integer id, ResourceRequestDTO dto) {
        Resource resource = resourceRepository.findByIdAndStatus(id, Resource.Status.ACTIVE.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(RESOURCE_NOT_FOUND, null, Locale.ENGLISH)));
        Optional<Resource> email = resourceRepository.findByEmailIgnoreCase(dto.getEmail());
        Department department = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(() -> new BadRequestException(messageSource.getMessage("DEPARTMENT_NOT_FOUND", null, Locale.ENGLISH)));
        Optional<Resource> optionalEmployee = resourceRepository.findByEmployeeId(dto.getEmployeeId());
        Role role = roleRepository.findById(dto.getRole()).orElseThrow(() -> new BadRequestException(messageSource.getMessage("ROLE_NOT_FOUND", null, Locale.ENGLISH)));
        if (email.isPresent() && !Objects.equals(resource.getEmail(), dto.getEmail().toLowerCase())) {
            throw new BadRequestException(messageSource.getMessage("EMAIL_ARE_SAME", null, Locale.ENGLISH));
        } else if (optionalEmployee.isPresent() && !Objects.equals(resource.getEmployeeId(), dto.getEmployeeId())) {
            throw new BadRequestException(messageSource.getMessage("DUPLICATE_EMPLOYEEIDS", null, Locale.ENGLISH));
        }
        if (!resource.getRole().getName().equals(role.getName()) && !allocationRepository.findByResourceIdAndStatusAndAllocationExpiryNot(id, Allocation.StatusValues.ACTIVE.value, Allocation.AllocationExpiryValues.EXPIRED.value).isEmpty()) {
            throw new BadRequestException(messageSource.getMessage("CANNOT_UPDATE_ROLE", null, Locale.ENGLISH));
        }
        LocalDate localDate1 = resource.getJoiningDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = dto.getJoiningDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (!localDate1.equals(localDate2)) {
            if (!allocationRepository.findByResourceIdAndStatus(id, Resource.Status.ACTIVE.value).isEmpty()) {
                throw new BadRequestException(messageSource.getMessage("CANNOT_CHANGE_JOINING_DATE", null, Locale.ENGLISH));
            } else {
                resource.setLastWorkedProjectDate(dto.getJoiningDate());
            }
        }
        resource.setEmployeeId(dto.getEmployeeId());
        resource.setName(dto.getName());
        resource.setEmail(dto.getEmail());
        resource.setPrevExperience(dto.getExperience());
        resource.setJoiningDate(dto.getJoiningDate());
        resource.setRole(role);
        resource.setDepartment(department);
        resource.setStatus(Resource.Status.ACTIVE.value);
        resource.setUpdatedDate(new Date());
        resourceRepository.save(resource);
        if (!dto.getSkills().isEmpty()) {
            resourceSkillRepository.deleteAllByResourceId(id);
            dto.getSkills().forEach(t -> {
                ResourceSkill resourceSkill = new ResourceSkill();
                resourceSkill.setResource(resource);
                resourceSkill.setSkill(skillRepository.findById(t.getSkillId()).orElseThrow(() -> new BadRequestException(messageSource.getMessage("INVALID_SKILL_ID", null, Locale.ENGLISH))));
                resourceSkill.setExperience(t.getExperience());
                resourceSkill.setProficiency(t.getProficiency());
                resourceSkillRepository.save(resourceSkill);
            });
        } else
            resourceSkillRepository.deleteAllByResourceId(id);
    }

    @Override
    public void deleteResource(Integer id) {
        Byte[] allocationExpiryStatus = {Allocation.AllocationExpiryValues.ON_GOING.value, Allocation.AllocationExpiryValues.NOT_STARTED.value};
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value).orElseThrow(
                        () -> new BadRequestException(messageSource.getMessage("USER_NOT_FOUND", null, Locale.ENGLISH)));
        Resource deleteResource = resourceRepository.findByIdAndStatus(id, Resource.Status.ACTIVE.value).orElseThrow(() -> new BadRequestException(messageSource.getMessage(RESOURCE_NOT_FOUND, null, Locale.ENGLISH)));
        if (List.of(Resource.Roles.HOD.getId(), Resource.Roles.HR.getId(), Resource.Roles.PM.getId()).contains(deleteResource.getRole().getId())) {
            Allocation allocation = allocationRepository.findTopByResourceIdAndStatusAndAllocationExpiryIn(id, Allocation.StatusValues.ACTIVE.value, allocationExpiryStatus);
            if (allocation != null) {
                throw new BadRequestException(messageSource.getMessage("CANNOT_DELETE_ALLOCATED_RESOURCE", null, Locale.ENGLISH));
            }
        }
        if (currentUser.getRole().getName().equals(Resource.Roles.ADMIN.getValue())) {
            this.commonFunctions.resourceDelete(deleteResource);
        } else if ((currentUser.getRole().getName().equals(Resource.Roles.HOD.getValue())
                || currentUser.getRole().getName().equals(Resource.Roles.HR.getValue())
                || currentUser.getRole().getName().equals(Resource.Roles.RM.getValue()))
                && (deleteResource.getRole().getName().equals(Resource.Roles.PM.getValue())
                || deleteResource.getRole().getName().equals(Resource.Roles.RESOURCE.getValue())
                || deleteResource.getRole().getName().equals(Resource.Roles.DH.getValue()))
                || ((currentUser.getRole().getName().equals(Resource.Roles.HR.getValue())
                && deleteResource.getRole().getName().equals(Resource.Roles.RM.getValue()))
                || (currentUser.getRole().getName().equals(Resource.Roles.HOD.getValue())
                && deleteResource.getRole().getName().equals(Resource.Roles.RM.getValue())))) {
            this.commonFunctions.resourceDelete(deleteResource);
        } else {
            throw new BadRequestException(messageSource.getMessage("ACCESS_DENIED", null, Locale.ENGLISH));
        }
        deleteResource.setStatus(Resource.Status.INACTIVE.value);
        resourceRepository.save(deleteResource);

    }

    @Override
    public List<ManagerListResponseDTO> getManagers() {
        List<Resource> resourceList = resourceRepository.findByRoleIdInAndStatus(List.of(2, 3, 5, 4), (byte) 1);
        return resourceList.stream().map(resource -> {
            ManagerListResponseDTO dto = new ManagerListResponseDTO();
            dto.setId(resource.getId());
            dto.setName(resource.getName());
            return dto;
        }).toList();
    }

    public ResourceResponseDTO mapResourceToDTO(Resource resource) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ResourceResponseDTO resourceDTO = new ResourceResponseDTO();
        resourceDTO.setId(resource.getId());
        resourceDTO.setEmployeeId(resource.getEmployeeId());
        if (resource.getDepartment() != null) {
            resourceDTO.setDepartmentId(resource.getDepartment().getDepartmentId());

        } else {
            resourceDTO.setDepartmentId(null);
        }
        resourceDTO.setEmail(resource.getEmail());
        resourceDTO.setName(resource.getName());
        if (resource.getJoiningDate() != null) {
            resourceDTO.setJoiningDate(dateFormat.format(resource.getJoiningDate()));
        } else {
            resourceDTO.setJoiningDate(null);
        }
        resourceDTO.setExperience(resource.getPrevExperience());
        resourceDTO.setTotalExperience(resource.getExperience());
        if (resource.getRole() != null) {
            resourceDTO.setRole(resource.getRole().getId());
        } else {
            resourceDTO.setRole(null);
        }
        resourceDTO.setAllocationStatus(resource.getAllocationStatus());

        if (resource.getCreatedDate() != null) {
            resourceDTO.setCreatedDate(dateFormat.format(resource.getCreatedDate()));
        } else {
            resourceDTO.setCreatedDate(null);
        }

        if (resource.getUpdatedDate() != null) {
            resourceDTO.setUpdatedDate(dateFormat.format(resource.getUpdatedDate()));
        } else {
            resourceDTO.setUpdatedDate(null);
        }
        resourceDTO.setStatus(resource.getStatus());
        List<ResourceSkillRequestDTO> resourceSkillDTOs = new ArrayList<>();
        List<ResourceSkill> resourceSkillList = resourceSkillRepository.findAllByResourceId(resource.getId());
        for (ResourceSkill resourceSkill : resourceSkillList) {
            ResourceSkillRequestDTO resourceSkillDTO = new ResourceSkillRequestDTO();
            resourceSkillDTO.setSkillId(resourceSkill.getSkill().getId());
            resourceSkillDTO.setExperience(resourceSkill.getExperience());
            resourceSkillDTO.setProficiency(resourceSkill.getProficiency());
            resourceSkillDTOs.add(resourceSkillDTO);
        }
        resourceDTO.setSkills(resourceSkillDTOs);

        return resourceDTO;
    }

}
