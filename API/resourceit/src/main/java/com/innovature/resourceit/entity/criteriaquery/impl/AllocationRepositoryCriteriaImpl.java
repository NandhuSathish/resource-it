package com.innovature.resourceit.entity.criteriaquery.impl;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceSkill;
import com.innovature.resourceit.entity.criteriaquery.AllocationRepositoryCriteria;
import com.innovature.resourceit.entity.dto.requestdto.AllocationRequestResourceFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterSkillAndExperienceRequestDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Repository
public class AllocationRepositoryCriteriaImpl implements AllocationRepositoryCriteria {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private MessageSource messageSource;
    private static final String EXPERIENCE = "experience";
    private static final String JOINING_DATE = "joiningDate";
    private static final Logger LOGGER = LoggerFactory.getLogger(AllocationRepositoryCriteriaImpl.class);

    @Override
    public List<Resource> findFilteredResourceAllocationWithPagination(
            AllocationRequestResourceFilterRequestDTO dto) {
        return resourceRepository.findAll(
                (Root<Resource> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (dto.getResourceName() != null) {
                        predicates.add(
                                cb.like(
                                        cb.lower(root.get("name")), "%" + dto.getResourceName().toLowerCase() + "%"));
                    }
                    if (dto.getExperienceMinValue() != null) {
                        predicates.add(
                                cb.greaterThanOrEqualTo(root.get(EXPERIENCE), dto.getExperienceMinValue() * 12));
                    }

                    if (dto.getExperienceMaxValue() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get(EXPERIENCE), dto.getExperienceMaxValue() * 12));
                    }

                    predicates.add(
                            root.get("role")
                                    .get("id")
                                    .in(List.of(Resource.Roles.RESOURCE.getId(), Resource.Roles.RM.getId())));
                    predicates.add(cb.equal(root.get("status"), Resource.Status.ACTIVE.value));
                    addJoiningDatePredicate(dto, cb, root, predicates);
                    addSkillAndExperiencePredicate(dto, cb, root, predicates);
                    addDepartmentPredicate(dto, root, predicates);
                    String queryString = query.toString();
                    LOGGER.debug("Generated Query: {}", queryString);
                    return cb.and(predicates.toArray(new Predicate[0]));
                });
    }

    public void addDepartmentPredicate(
            AllocationRequestResourceFilterRequestDTO dto,
            Root<Resource> root,
            List<Predicate> predicates) {
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage("RESOURCE_NOT_FOUND", null, Locale.ENGLISH)));
        List<Integer> departmentIds = dto.getDepartmentIds();
        if (currentUser.getRole().getId().equals(Resource.Roles.PM.getId())
                || currentUser.getRole().getId().equals(Resource.Roles.DH.getId())) {
            departmentIds.clear();
            departmentIds.add(currentUser.getDepartment().getDepartmentId());
        }
        if (dto.getDepartmentIds() != null && !dto.getDepartmentIds().isEmpty()) {
            predicates.add(root.get("department").get("departmentId").in(departmentIds));
        }
    }

    public void addJoiningDatePredicate(
            AllocationRequestResourceFilterRequestDTO dto,
            CriteriaBuilder cb,
            Root<Resource> root,
            List<Predicate> predicates) {
        Date comparisonDate = (dto.getAllocationStartDate() != null) ? dto.getAllocationStartDate() : new Date();
        predicates.add(cb.lessThanOrEqualTo(root.get(JOINING_DATE), comparisonDate));
    }

    public void addSkillAndExperiencePredicate(
            AllocationRequestResourceFilterRequestDTO dto,
            CriteriaBuilder cb,
            Root<Resource> root,
            List<Predicate> predicates) {
        if (dto.getSkillsAndExperiences() != null && !dto.getSkillsAndExperiences().isEmpty()) {
            for (ResourceFilterSkillAndExperienceRequestDTO skillAndExp : dto.getSkillsAndExperiences()) {
                if (skillAndExp.getSkillId() != null && !skillAndExp.getSkillId().isEmpty()) {
                    Join<Resource, ResourceSkill> resourceSkillJoin = root.join("resourceSkills", JoinType.LEFT);
                    Predicate skillPredicate = cb.equal(resourceSkillJoin.get("skill").get("id"),
                            skillAndExp.getSkillId());
                    Predicate minExpPredicate = cb.greaterThanOrEqualTo(resourceSkillJoin.get(EXPERIENCE),
                            Integer.parseInt(skillAndExp.getSkillMinValue()) * 12);
                    Predicate maxExpPredicate = cb.lessThanOrEqualTo(resourceSkillJoin.get(EXPERIENCE),
                            Integer.parseInt(skillAndExp.getSkillMaxValue()) * 12);
                    if ((skillAndExp.getProficiency() != null) && !skillAndExp.getProficiency().isEmpty()) {
                        Predicate proficiencyPredicate = resourceSkillJoin.get("proficiency")
                                .in(skillAndExp.getProficiency());
                        predicates.add(cb.and(skillPredicate, minExpPredicate, maxExpPredicate, proficiencyPredicate));
                    } else {
                        predicates.add(cb.and(skillPredicate, minExpPredicate, maxExpPredicate));
                    }
                }
            }
        }
    }
}
