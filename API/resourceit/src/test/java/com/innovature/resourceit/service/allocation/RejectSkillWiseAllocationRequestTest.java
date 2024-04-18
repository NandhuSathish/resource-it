package com.innovature.resourceit.service.allocation;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.ResourceSkillWiseAllocationRequest;
import com.innovature.resourceit.entity.Role;
import com.innovature.resourceit.entity.dto.requestdto.RejectionRequestDTO;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.repository.ResourceSkillWiseAllocationRequestRepository;
import com.innovature.resourceit.security.SecurityUtil;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = RejectSkillWiseAllocationRequestTest.class)
class RejectSkillWiseAllocationRequestTest {

    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private EmailUtils emailUtils;
    @Mock
    private ResourceSkillWiseAllocationRequestRepository skillWiseAllocationRequestRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AllocationServiceImpli allocationService;

    private MockedStatic<SecurityUtil> mockedSecurityUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@gmail.com");
    }

    @AfterEach
    void tearDown() {
        // Close the static mock
        mockedSecurityUtil.close();
    }

    @Test
    void testRejectSkillWiseAllocationRequest() {
        // Mock the required objects
        Integer requestId = 123;
        Resource currentUser = createMockResource(Resource.Roles.HR.getValue());

        ResourceSkillWiseAllocationRequest request = createMockSkillWiseAllocationRequest(requestId);
        request.setRequestedBy(currentUser);
        RejectionRequestDTO dto = createMockRejectionRequestDTO();

        when(resourceRepository.findByEmailAndStatus(anyString(), anyByte())).thenReturn(Optional.of(currentUser));
        when(skillWiseAllocationRequestRepository.findByIdAndApprovalFlowNotInAndStatus(anyInt(), anyList(), anyByte())).thenReturn(request);

        // Test the method
        allocationService.rejectSkillWiseAllocationRequest(requestId, dto);

        // Verify the method is called and the request is saved
        verify(skillWiseAllocationRequestRepository, times(1)).save(request);
    }

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
        // Set other required properties
        return request;
    }

    private RejectionRequestDTO createMockRejectionRequestDTO() {
        // Create and return a mock RejectionRequestDTO
        RejectionRequestDTO dto = new RejectionRequestDTO();
        // Set properties for the rejection DTO
        return dto;
    }
}
