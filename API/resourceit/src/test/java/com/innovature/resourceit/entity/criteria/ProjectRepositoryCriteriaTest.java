/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.criteria;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.ResourceSkillWiseAllocationRequest;
import com.innovature.resourceit.entity.criteriaquery.impl.ProjectRepositoryCriteriaImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = ProjectRepositoryCriteriaTest.class)
public class ProjectRepositoryCriteriaTest {

    @Mock
    EntityManager entityManager;

    @InjectMocks
    ProjectRepositoryCriteriaImpl projectRepositoryCriteriaImpl;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Project> criteriaQuery;
    
    @Mock
    private CriteriaQuery<ResourceSkillWiseAllocationRequest> criteriaQuerySkill;

    @Mock
    private CriteriaBuilder criteriaBuilderForCount;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Project> root;

    @Mock
    private Root<Project> rootForCount;
    
    @Mock
    private Root<ResourceSkillWiseAllocationRequest> rootSkill;

    @Mock
    private Root<ResourceSkillWiseAllocationRequest> rootSkillForCount;

    @Mock
    private TypedQuery<Project> typedQuery;
    
    @Mock
    private TypedQuery<ResourceSkillWiseAllocationRequest> typedQuerySkill;

    @Mock
    private TypedQuery<Long> typedCountQuery;

    @Test
    public void getProjectListUsingPagination() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Boolean sortOrder = false;
        String sortKey = "projectId";
        String startDate = "01-01-2022";
        String endDate = "01-02-2022";

        Project p = new Project(1);
        p.setStartDate(sdf.parse(startDate));
        p.setEndDate(sdf.parse(endDate));
        p.setCreatedDate(sdf.parse(startDate));
        p.setUpdatedDate(sdf.parse(startDate));

        List<Project> projectList = new ArrayList<>();
        projectList.add(p);
        long count = 10L;

        List<Integer> projectStateInt = Arrays.asList(1);
        List<Integer> projectTypeInt = Arrays.asList(1);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Project.class)).thenReturn(criteriaQuery);
        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        when(criteriaQuery.from(Project.class)).thenReturn(root);
        when(countQuery.from(Project.class)).thenReturn(rootForCount);

        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(entityManager.createQuery(countQuery)).thenReturn(typedCountQuery);

        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(projectList);

        Predicate predicate = mock(Predicate.class);

        when(entityManager.getCriteriaBuilder().conjunction()).thenReturn(predicate);

        Predicate predicateForCount = mock(Predicate.class);

        when(entityManager.getCriteriaBuilder().conjunction()).thenReturn(predicateForCount);

        Path<Object> projectStatePath = mock(Path.class);
        when(root.get("projectState")).thenReturn(projectStatePath);
        when(projectStatePath.in(projectStateInt)).thenReturn(predicate);

        Path<Object> projectStatePathCount = mock(Path.class);
        when(rootForCount.get("projectState")).thenReturn(projectStatePathCount);
        when(projectStatePathCount.in(projectStateInt)).thenReturn(predicateForCount);

        Path<Object> projectTypePath = mock(Path.class);
        when(root.get("projectType")).thenReturn(projectTypePath);
        when(projectTypePath.in(projectTypeInt)).thenReturn(predicate);

        Path<Object> projectTypePathCount = mock(Path.class);
        when(rootForCount.get("projectType")).thenReturn(projectTypePathCount);
        when(projectTypePathCount.in(projectTypeInt)).thenReturn(predicateForCount);

        when(typedCountQuery.getSingleResult()).thenReturn(count);

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Boolean.FALSE.equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortKey));
        Page<Project> resultPage = projectRepositoryCriteriaImpl.findFilteredProjectsWithPagination(
                "projectName", sdf.parse(startDate), sdf.parse(endDate), projectStateInt, new ArrayList<>(),
                projectTypeInt, pageable);

        assertEquals(projectList.size(), resultPage.getContent().size());

    }

    @Test
    public void getProjectListTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String startDate = "01-01-2022";
        String endDate = "01-02-2022";

        Project p = new Project(1);
        p.setStartDate(sdf.parse(startDate));
        p.setEndDate(sdf.parse(endDate));
        p.setCreatedDate(sdf.parse(startDate));
        p.setUpdatedDate(sdf.parse(startDate));

        List<Project> projectList = new ArrayList<>();
        projectList.add(p);

        List<Integer> projectStateInt = Arrays.asList(1);
        List<Integer> projectTypeInt = Arrays.asList(1);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Project.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Project.class)).thenReturn(root);

        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(projectList);

        Predicate predicate = mock(Predicate.class);

        when(entityManager.getCriteriaBuilder().conjunction()).thenReturn(predicate);

        Path<Object> projectStatePath = mock(Path.class);
        when(root.get("projectState")).thenReturn(projectStatePath);
        when(projectStatePath.in(projectStateInt)).thenReturn(predicate);

        Path<Object> projectTypePath = mock(Path.class);
        when(root.get("projectType")).thenReturn(projectTypePath);
        when(projectTypePath.in(projectTypeInt)).thenReturn(predicate);

        List<Project> resultPage = projectRepositoryCriteriaImpl.findFilteredProjectsForDownload(
                "projectName", sdf.parse(startDate), sdf.parse(endDate), projectStateInt, new ArrayList<>(),
                projectTypeInt);

        assertEquals(projectList.size(), resultPage.size());
    }
    
    @Test
    public void getResourceSkillWiseAllocationListTest() throws ParseException {
        
        ResourceSkillWiseAllocationRequest resourceSkillWiseAllocationRequest = new ResourceSkillWiseAllocationRequest();
        
        resourceSkillWiseAllocationRequest.setId(1);
        
        List<ResourceSkillWiseAllocationRequest> resultList = Arrays.asList(resourceSkillWiseAllocationRequest);
        
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(ResourceSkillWiseAllocationRequest.class)).thenReturn(criteriaQuerySkill);
        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        when(criteriaQuerySkill.from(ResourceSkillWiseAllocationRequest.class)).thenReturn(rootSkill);
        when(countQuery.from(ResourceSkillWiseAllocationRequest.class)).thenReturn(rootSkillForCount);

        when(entityManager.createQuery(criteriaQuerySkill)).thenReturn(typedQuerySkill);
        when(entityManager.createQuery(countQuery)).thenReturn(typedCountQuery);

        when(typedQuerySkill.setFirstResult(anyInt())).thenReturn(typedQuerySkill);
        when(typedQuerySkill.setMaxResults(anyInt())).thenReturn(typedQuerySkill);
        when(typedQuerySkill.getResultList()).thenReturn(resultList);

        Predicate predicate = mock(Predicate.class);

        when(entityManager.getCriteriaBuilder().conjunction()).thenReturn(predicate);

        Predicate predicateForCount = mock(Predicate.class);

        when(entityManager.getCriteriaBuilder().conjunction()).thenReturn(predicateForCount);
        
        List<Integer> approvalStatusInt = Arrays.asList(1);
        long count = 10L;
        
        Path<Object> approvalStatusPath = mock(Path.class);
        when(rootSkill.get("approvalFlow")).thenReturn(approvalStatusPath);
        when(approvalStatusPath.in(approvalStatusInt)).thenReturn(predicate);

        Path<Object> approvalStatusPathCount = mock(Path.class);
        when(rootSkillForCount.get("approvalFlow")).thenReturn(approvalStatusPathCount);
        when(approvalStatusPathCount.in(approvalStatusInt)).thenReturn(predicateForCount);

        when(typedCountQuery.getSingleResult()).thenReturn(count);

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ResourceSkillWiseAllocationRequest> resultPage = projectRepositoryCriteriaImpl.findFilteredResourceSkillWiseAllocationWithPagination(approvalStatusInt, 
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), pageable);

        assertEquals(resultList.size(), resultPage.getContent().size());
    }
}
