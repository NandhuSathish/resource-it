/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.criteriaquery.impl;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.ResourceSkillWiseAllocationRequest;
import com.innovature.resourceit.entity.criteriaquery.ProjectRepositoryCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * @author abdul.fahad
 */
@Repository
public class ProjectRepositoryCriteriaImpl implements ProjectRepositoryCriteria {

    private final EntityManager entityManager;

    private static final String NAME = "name";
    private static final String PROJECTCODE = "projectCode";
    private static final String STARTDATE = "startDate";
    private static final String ENDDATE = "endDate";
    private static final String PROJECTSTATE = "projectState";
    private static final String MANAGER = "manager";
    private static final String PROJECT = "project";
    private static final String DEPARTMENT = "department";
    private static final String ID = "id";
    private static final String PROJECT_ID = "projectId";
    private static final String DEPARTMENT_ID = "departmentId";
    private static final String PROJECTTYPE = "projectType";
    private static final String APPROVAL_STATUS = "approvalFlow";
    private static final String STATUS = "status";
    private static final String REQUESTEDBY = "requestedBy";

    public ProjectRepositoryCriteriaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Page<Project> findFilteredProjectsWithPagination(String projectName, Date startDate, Date endDate, List<Integer> projectState, List<Integer> managerId, List<Integer> projectType, Pageable pageable) {

        // Project listing
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = criteriaQuery.from(Project.class);

        // Project count
        CriteriaBuilder criteriaBuilderForCount = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilderForCount.createQuery(Long.class);
        Root<Project> rootForCount = countQuery.from(Project.class);

        Predicate predicate = criteriaBuilder.conjunction();
        Predicate predicateForCount = criteriaBuilderForCount.conjunction();

        if (projectName != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(NAME)),
                            criteriaBuilder.like(root.get(NAME), "%" + projectName + "%"),
                            criteriaBuilder.like(root.get(PROJECTCODE), "%" + projectName + "%")
                    )
            );

            predicateForCount = criteriaBuilderForCount.and(
                    predicateForCount,
                    criteriaBuilderForCount.or(
                            criteriaBuilderForCount.isNull(rootForCount.get(NAME)),
                            criteriaBuilderForCount.like(rootForCount.get(NAME), "%" + projectName + "%"),
                            criteriaBuilderForCount.like(rootForCount.get(PROJECTCODE), "%" + projectName + "%")
                    )
            );
        }

        if (startDate != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(STARTDATE)),
                            criteriaBuilder.greaterThanOrEqualTo(root.get(STARTDATE), startDate)
                    )
            );

            predicateForCount = criteriaBuilderForCount.and(
                    predicateForCount,
                    criteriaBuilderForCount.or(
                            criteriaBuilderForCount.isNull(rootForCount.get(STARTDATE)),
                            criteriaBuilderForCount.greaterThanOrEqualTo(rootForCount.get(STARTDATE), startDate)
                    )
            );
        }

        if (endDate != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(ENDDATE)),
                            criteriaBuilder.lessThanOrEqualTo(root.get(ENDDATE), endDate)
                    )
            );

            predicateForCount = criteriaBuilderForCount.and(
                    predicateForCount,
                    criteriaBuilderForCount.or(
                            criteriaBuilderForCount.isNull(rootForCount.get(ENDDATE)),
                            criteriaBuilderForCount.lessThanOrEqualTo(rootForCount.get(ENDDATE), endDate)
                    )
            );
        }

        if (projectState != null && !projectState.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(PROJECTSTATE)),
                            root.get(PROJECTSTATE).in(projectState)
                    )
            );

            predicateForCount = criteriaBuilderForCount.and(
                    predicateForCount,
                    criteriaBuilderForCount.or(
                            criteriaBuilderForCount.isNull(rootForCount.get(PROJECTSTATE)),
                            rootForCount.get(PROJECTSTATE).in(projectState)
                    )
            );
        }

        if (managerId != null && !managerId.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.join(MANAGER, JoinType.LEFT).get(ID)),
                            root.join(MANAGER, JoinType.LEFT).get(ID).in(managerId)
                    )
            );

            predicateForCount = criteriaBuilderForCount.and(
                    predicateForCount,
                    criteriaBuilderForCount.or(
                            criteriaBuilderForCount.isNull(rootForCount.join(MANAGER, JoinType.LEFT).get(ID)),
                            rootForCount.join(MANAGER, JoinType.LEFT).get(ID).in(managerId)
                    )
            );
        }

        if (projectType != null && !projectType.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(PROJECTTYPE)),
                            root.get(PROJECTTYPE).in(projectType)
                    )
            );
            predicateForCount = criteriaBuilderForCount.and(
                    predicateForCount,
                    criteriaBuilderForCount.or(
                            criteriaBuilderForCount.isNull(rootForCount.get(PROJECTTYPE)),
                            rootForCount.get(PROJECTTYPE).in(projectType)
                    )
            );
        }

        predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(STATUS), Project.statusValues.DELETED.value));

        predicateForCount = criteriaBuilderForCount.and(predicateForCount, criteriaBuilderForCount.notEqual(rootForCount.get(STATUS), Project.statusValues.DELETED.value));

        criteriaQuery.where(predicate);
        if (pageable.getSort().isSorted()) {
            criteriaQuery.orderBy(pageable.getSort().stream()
                    .map(order -> order.isAscending()
                            ? criteriaBuilder.asc(root.get(order.getProperty()))
                            : criteriaBuilder.desc(root.get(order.getProperty())))
                    .toArray(jakarta.persistence.criteria.Order[]::new));
        }

        List<Project> resultList = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        countQuery.select(criteriaBuilderForCount.count(rootForCount.get(PROJECT_ID)));
        countQuery.where(predicateForCount);

        TypedQuery<Long> typedCountQuery = entityManager.createQuery(countQuery);
        long count = typedCountQuery.getSingleResult();

        return new PageImpl<>(resultList, pageable, count);

    }

    @Override
    public List<Project> findFilteredProjectsForDownload(String projectName, Date startDate, Date endDate, List<Integer> projectState, List<Integer> managerId, List<Integer> projectType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = criteriaQuery.from(Project.class);

        Predicate predicate = criteriaBuilder.conjunction();

        if (projectName != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(NAME)),
                            criteriaBuilder.like(root.get(NAME), "%" + projectName + "%"),
                            criteriaBuilder.like(root.get(PROJECTCODE), "%" + projectName + "%")
                    )
            );
        }

        if (startDate != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(STARTDATE)),
                            criteriaBuilder.greaterThanOrEqualTo(root.get(STARTDATE), startDate)
                    )
            );
        }

        if (endDate != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(ENDDATE)),
                            criteriaBuilder.lessThanOrEqualTo(root.get(ENDDATE), endDate)
                    )
            );
        }

        if (projectState != null && !projectState.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(PROJECTSTATE)),
                            root.get(PROJECTSTATE).in(projectState)
                    )
            );
        }

        if (managerId != null && !managerId.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.join(MANAGER, JoinType.LEFT).get(ID)),
                            root.join(MANAGER, JoinType.LEFT).get(ID).in(managerId)
                    )
            );
        }

        if (projectType != null && !projectType.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(PROJECTTYPE)),
                            root.get(PROJECTTYPE).in(projectType)
                    )
            );
        }

        predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get(STATUS), 1),
                        criteriaBuilder.equal(root.get(STATUS), 2)
                )
        );
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Page<ResourceSkillWiseAllocationRequest> findFilteredResourceSkillWiseAllocationWithPagination(List<Integer> approvalStatus, List<Integer> projectIds, List<Integer> departmentIds, List<Integer> managerIds, Pageable pageable) {
        // listing
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ResourceSkillWiseAllocationRequest> criteriaQuery = criteriaBuilder.createQuery(ResourceSkillWiseAllocationRequest.class);
        Root<ResourceSkillWiseAllocationRequest> root = criteriaQuery.from(ResourceSkillWiseAllocationRequest.class);

        // count
        CriteriaBuilder criteriaBuilderForCount = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilderForCount.createQuery(Long.class);
        Root<ResourceSkillWiseAllocationRequest> rootForCount = countQuery.from(ResourceSkillWiseAllocationRequest.class);

        Predicate predicate = criteriaBuilder.conjunction();
        Predicate predicateForCount = criteriaBuilderForCount.conjunction();

        if (projectIds != null && !projectIds.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.join(PROJECT, JoinType.LEFT).get(PROJECT_ID)),
                            root.join(PROJECT, JoinType.LEFT).get(PROJECT_ID).in(projectIds)
                    )
            );

            predicateForCount = criteriaBuilderForCount.and(
                    predicateForCount,
                    criteriaBuilderForCount.or(
                            criteriaBuilderForCount.isNull(rootForCount.join(PROJECT, JoinType.LEFT).get(PROJECT_ID)),
                            rootForCount.join(PROJECT, JoinType.LEFT).get(PROJECT_ID).in(projectIds)
                    )
            );
        }

        if (departmentIds != null && !departmentIds.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.join(DEPARTMENT, JoinType.LEFT).get(DEPARTMENT_ID)),
                            root.join(DEPARTMENT, JoinType.LEFT).get(DEPARTMENT_ID).in(departmentIds)
                    )
            );

            predicateForCount = criteriaBuilderForCount.and(
                    predicateForCount,
                    criteriaBuilderForCount.or(
                            criteriaBuilderForCount.isNull(rootForCount.join(DEPARTMENT, JoinType.LEFT).get(DEPARTMENT_ID)),
                            rootForCount.join(DEPARTMENT, JoinType.LEFT).get(DEPARTMENT_ID).in(departmentIds)
                    )
            );
        }
        if (managerIds != null && !managerIds.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.join(REQUESTEDBY, JoinType.LEFT).get(ID)),
                            root.join(REQUESTEDBY, JoinType.LEFT).get(ID).in(managerIds)
                    )
            );

            predicateForCount = criteriaBuilderForCount.and(
                    predicateForCount,
                    criteriaBuilderForCount.or(
                            criteriaBuilderForCount.isNull(rootForCount.join(REQUESTEDBY, JoinType.LEFT).get(ID)),
                            rootForCount.join(REQUESTEDBY, JoinType.LEFT).get(ID).in(managerIds)
                    )
            );
        }

        if (approvalStatus != null && !approvalStatus.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.isNull(root.get(APPROVAL_STATUS)),
                            root.get(APPROVAL_STATUS).in(approvalStatus)
                    )
            );
            predicateForCount = criteriaBuilderForCount.and(
                    predicateForCount,
                    criteriaBuilderForCount.or(
                            criteriaBuilderForCount.isNull(rootForCount.get(APPROVAL_STATUS)),
                            rootForCount.get(APPROVAL_STATUS).in(approvalStatus)
                    )
            );
        }

        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(STATUS), 1));

        predicateForCount = criteriaBuilderForCount.and(predicateForCount, criteriaBuilderForCount.equal(rootForCount.get(STATUS), 1));

        criteriaQuery.where(predicate);
        if (pageable.getSort().isSorted()) {
            criteriaQuery.orderBy(pageable.getSort().stream()
                    .map(order -> order.isAscending()
                            ? criteriaBuilder.asc(root.get(order.getProperty()))
                            : criteriaBuilder.desc(root.get(order.getProperty())))
                    .toArray(jakarta.persistence.criteria.Order[]::new));
        }

        List<ResourceSkillWiseAllocationRequest> resultList = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        countQuery.select(criteriaBuilderForCount.count(rootForCount.get(ID)));
        countQuery.where(predicateForCount);

        TypedQuery<Long> typedCountQuery = entityManager.createQuery(countQuery);
        long count = typedCountQuery.getSingleResult();

        return new PageImpl<>(resultList, pageable, count);
    }

}
