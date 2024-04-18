package com.innovature.resourceit.service.allocation;

import com.innovature.resourceit.entity.Project;
import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceSkillWiseAllocationRequest;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.dto.requestdto.ResourceAllocationRequestDTO;
import com.innovature.resourceit.repository.AllocationRepository;
import com.innovature.resourceit.repository.ProjectRepository;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.ResourceSkillWiseAllocationRequestRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import com.innovature.resourceit.util.EmailUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = ApproveByHRSkillWiseAllocationRequestTest.class)
class ApproveByHRSkillWiseAllocationRequestTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceSkillWiseAllocationRequestRepository skillWiseAllocationRequestRepository;

    @Mock
    private AllocationRepository allocationRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private CommonFunctions commonFunctions;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AllocationServiceImpli allocationService;
    @Mock
    private EmailUtils emailUtils;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
        when(projectRepository.save(new Project())).thenReturn(new Project(1));

    }

    @AfterEach
    void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void testApproveByHRSkillWiseAllocationRequest() {
        // Mock the required objects
        Integer requestId = 123;
        Resource currentUser = createMockResource(Resource.Roles.HR.getValue());
        Project project = new Project(1);
        project.setTeamSize(1);
        ResourceSkillWiseAllocationRequest request = createMockSkillWiseAllocationRequest(requestId);
        request.setRequestedBy(currentUser);
        request.setProject(project);
        List<ResourceAllocationRequestDTO> dtoList = createMockAllocationRequestDTOList();

        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        when(skillWiseAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), anyList(), anyByte())).thenReturn(request);
        when(commonFunctions.checkAllocationConflicts(anyList(), any(), any(), anyInt())).thenReturn(new ArrayList<>());

        // Test the method
        allocationService.approveByHRSkillWiseAllocationRequest(requestId, dtoList);

        // Verify the method is called and the request is saved
        verify(allocationRepository, times(dtoList.size())).saveAll(any());
        verify(skillWiseAllocationRequestRepository, times(1)).save(request);
    }

    // Other test methods...

    private Resource createMockResource(String role) {
        // Create and return a mock Resource with the specified role
        Resource resource = new Resource(1);
        resource.setRole(new Role(role));
        return resource;
    }

    private ResourceSkillWiseAllocationRequest createMockSkillWiseAllocationRequest(Integer requestId) {
        // Create and return a mock ResourceSkillWiseAllocationRequest
        ResourceSkillWiseAllocationRequest request = new ResourceSkillWiseAllocationRequest();
        request.setId(requestId);
        request.setApprovalFlow(ResourceSkillWiseAllocationRequest.ApprovalFlowValues.PENDING.value);
        request.setStartDate(new Date());
        request.setEndDate(new Date());
        return request;
    }

    private List<ResourceAllocationRequestDTO> createMockAllocationRequestDTOList() {
        // Create and return a mock list of ResourceAllocationRequestDTO
        List<ResourceAllocationRequestDTO> dtoList = new ArrayList<>();
        ResourceAllocationRequestDTO dto = new ResourceAllocationRequestDTO();
        dtoList.add(dto);
        return dtoList;
    }
}
