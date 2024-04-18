package com.innovature.resourceit.entity.criteriaquery.impl;

import com.innovature.resourceit.entity.Allocation;
import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceSkill;
import com.innovature.resourceit.entity.criteriaquery.BillabilityRepositoryCriteria;
import com.innovature.resourceit.entity.dto.requestdto.BillabilitySummaryRequestDTO;
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
import java.util.List;
import java.util.Locale;

@Repository
public class BillabilityRepositoryCriteriaImpl implements BillabilityRepositoryCriteria {


    private static final String EXPERIENCE = "experience";
    private static final String STATUS = "status";
    private static final Logger LOGGER = LoggerFactory.getLogger(BillabilityRepositoryCriteriaImpl.class);
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    private MessageSource messageSource;

    @Override
    public List<Resource> findResourceForBillabilityStatistic(BillabilitySummaryRequestDTO requestDTO) {
        return resourceRepository.findAll(
                (Root<Resource> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (requestDTO.getName() != null) {
                        predicates.add(
                                cb.like(
                                        cb.lower(root.get("name")), "%" + requestDTO.getName().toLowerCase() + "%"));
                    }
                    if (requestDTO.getLowerExperience() != null) {
                        predicates.add(
                                cb.greaterThanOrEqualTo(root.get(EXPERIENCE), requestDTO.getLowerExperience() * 12));
                    }

                    if (requestDTO.getHighExperience() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get(EXPERIENCE), requestDTO.getHighExperience() * 12));
                    }

                    predicates.add(
                            root.get("role")
                                    .get("id")
                                    .in(List.of(Resource.Roles.RESOURCE.getId(), Resource.Roles.RM.getId(), Resource.Roles.DH.getId(), Resource.Roles.PM.getId(), Resource.Roles.HOD.getId(), Resource.Roles.HR.getId())));
                    if (requestDTO.getStatus() == null || requestDTO.getStatus() == 1) {
                        predicates.add(cb.equal(root.get(STATUS), Resource.Status.ACTIVE.value));
                    } else {
                        predicates.add(cb.equal(root.get(STATUS), Resource.Status.INACTIVE.value));
                    }
                    addSkillAndExperiencePredicate(requestDTO, cb, root, predicates);
                    addDepartmentPredicate(requestDTO, root, predicates);
                    addProjectCodeFilterPredicate(requestDTO, cb, root, predicates);
                    String queryString = query.toString();
                    LOGGER.debug("Generated Query: {}", queryString);
                    return cb.and(predicates.toArray(new Predicate[0]));
                });
    }

    public void addDepartmentPredicate(
            BillabilitySummaryRequestDTO requestDTO,
            Root<Resource> root,
            List<Predicate> predicates) {
        Resource currentUser = resourceRepository
                .findByEmailAndStatus(SecurityUtil.getCurrentUserEmail(), Resource.Status.ACTIVE.value)
                .orElseThrow(
                        () -> new BadRequestException(
                                messageSource.getMessage("RESOURCE_NOT_FOUND", null, Locale.ENGLISH)));
        if (currentUser != null) {
            List<Integer> departmentIds = requestDTO.getDepartmentIds();
            if ((requestDTO.getDepartmentIds() != null && !requestDTO.getDepartmentIds().isEmpty())) {
                predicates.add(root.get("department").get("departmentId").in(departmentIds));
            }
        }
    }


    public void addSkillAndExperiencePredicate(
            BillabilitySummaryRequestDTO dto,
            CriteriaBuilder cb,
            Root<Resource> root,
            List<Predicate> predicates) {
        if (dto.getSkillAndExperiences() != null && !dto.getSkillAndExperiences().isEmpty()) {
            for (ResourceFilterSkillAndExperienceRequestDTO skillAndExp : dto.getSkillAndExperiences()) {
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

    public void addProjectCodeFilterPredicate(BillabilitySummaryRequestDTO dto,
                                              CriteriaBuilder cb,
                                              Root<Resource> root,
                                              List<Predicate> predicates) {
        if (dto.getProjectIds() != null && !dto.getProjectIds().isEmpty()) {
            CriteriaQuery<Integer> criteria = cb.createQuery(Integer.class);
            Subquery<Integer> subquery = criteria.subquery(Integer.class);
            Root<Allocation> allocationRoot = subquery.from(Allocation.class);
            Join<Allocation, Project> projectJoin = allocationRoot.join("project", JoinType.LEFT);

            subquery.select(allocationRoot.get("resource").get("id"))
                    .where(cb.and(projectJoin.get("id").in(dto.getProjectIds()), cb.equal(allocationRoot.get(STATUS), Allocation.StatusValues.ACTIVE.value)));

            predicates.add(root.get("id").in(subquery));
        }
    }
}
