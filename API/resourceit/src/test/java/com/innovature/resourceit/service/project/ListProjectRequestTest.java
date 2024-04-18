package com.innovature.resourceit.service.project;

import com.innovature.resourceit.entity.ProjectRequest;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.responsedto.ProjectRequestResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ProjectRequestRepository;
import com.innovature.resourceit.repository.SkillRepository;
import com.innovature.resourceit.service.impli.ProjectServiceImpli;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = ListProjectRequestTest.class)
class ListProjectRequestTest {


    @InjectMocks
    private ProjectServiceImpli projectService;

    @Mock
    private ProjectRequestRepository projectRequestRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private MessageSource messageSource;

    @Test
    void testListProjectRequests() {
        Set<Byte> approvalStatus = new HashSet<>();
        Set<Integer> managerIds = new HashSet<>();
        Set<Byte> projectType = new HashSet<>();
        Pageable pageable = Pageable.unpaged();
        Resource requestedBy = null;

        ProjectRequest projectRequest = new ProjectRequest();  // Create a sample ProjectRequest
        when(projectRequestRepository.findAll((Specification<ProjectRequest>) any(), eq(pageable))).thenReturn(new PageImpl<>(Collections.singletonList(projectRequest)));
        Page<ProjectRequestResponseDTO> result = projectService.listProjectRequests(approvalStatus, managerIds, projectType,requestedBy, pageable);
        verify(projectRequestRepository, times(1)).findAll((Specification<ProjectRequest>) any(), eq(pageable));
    }

    @Test
    void projectListCatch() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            projectService.listProjectRequests(any(), any(), any(), any(),any());
        });
        Assertions.assertNotNull(exception);
    }

    @Test
    void testConvertToProjectRequestResponseDto() {
        ProjectRequest projectRequest = new ProjectRequest();
        ProjectRequestResponseDTO result = projectService.convertToProjectRequestResponseDto(projectRequest);
        assertNotNull(result);
    }


}
