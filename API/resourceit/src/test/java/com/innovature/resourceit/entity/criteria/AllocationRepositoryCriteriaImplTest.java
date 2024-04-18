package com.innovature.resourceit.entity.criteria;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceSkill;
import com.innovature.resourceit.entity.criteriaquery.impl.AllocationRepositoryCriteriaImpl;
import com.innovature.resourceit.entity.dto.requestdto.AllocationRequestResourceFilterRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceFilterSkillAndExperienceRequestDTO;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.security.SecurityUtil;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AllocationRepositoryCriteriaImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Root<Resource> root;

    @Mock
    private CriteriaQuery<?> criteriaQuery;

    @Mock
    private Join<Resource, ResourceSkill> resourceSkillJoin;

    @Mock
    private Path<String> path;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;
    @InjectMocks
    private AllocationRepositoryCriteriaImpl allocationRepositoryCriteria;

    private AllocationRequestResourceFilterRequestDTO dto;

    @BeforeEach
    void setUp() {
        dto = new AllocationRequestResourceFilterRequestDTO();
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    public void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void testFindFilteredResourceAllocationWithPagination() {
        List<Resource> sampleResources = new ArrayList<>();
        when(resourceRepository.findAll(any(Specification.class))).thenReturn(sampleResources);

        List<Resource> result = allocationRepositoryCriteria.findFilteredResourceAllocationWithPagination(dto);

        verify(resourceRepository).findAll(any(Specification.class));
        assertEquals(sampleResources, result);
    }

    @Test
    void testAddJoiningDatePredicate() {
        AllocationRequestResourceFilterRequestDTO dto = new AllocationRequestResourceFilterRequestDTO();
        dto.setAllocationStartDate(new Date());

        List<Predicate> predicates = new ArrayList<>();
        allocationRepositoryCriteria.addJoiningDatePredicate(dto, criteriaBuilder, root, predicates);

        assertEquals(1, predicates.size());
    }

    @Test
    void testAddSkillAndExperiencePredicate_NoSkills() {
        AllocationRequestResourceFilterRequestDTO dto = new AllocationRequestResourceFilterRequestDTO();

        List<Predicate> predicates = new ArrayList<>();
        allocationRepositoryCriteria.addSkillAndExperiencePredicate(dto, criteriaBuilder, root, predicates);

        assertEquals(0, predicates.size());
    }

    @Test
    void testAddSkillAndExperiencePredicate_EmptySkills() {
        AllocationRequestResourceFilterRequestDTO dto = new AllocationRequestResourceFilterRequestDTO();
        dto.setSkillsAndExperiences(List.of(new ResourceFilterSkillAndExperienceRequestDTO()));

        List<Predicate> predicates = new ArrayList<>();
        allocationRepositoryCriteria.addSkillAndExperiencePredicate(dto, criteriaBuilder, root, predicates);

        assertEquals(0, predicates.size());
    }

    @Test
    void testAddSkillAndExperiencePredicate_NullSkills() {
        AllocationRequestResourceFilterRequestDTO dto = new AllocationRequestResourceFilterRequestDTO();
        dto.setSkillsAndExperiences(null);

        List<Predicate> predicates = new ArrayList<>();
        allocationRepositoryCriteria.addSkillAndExperiencePredicate(dto, criteriaBuilder, root, predicates);

        assertEquals(0, predicates.size());
    }
}
