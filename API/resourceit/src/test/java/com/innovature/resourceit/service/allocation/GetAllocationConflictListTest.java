package com.innovature.resourceit.service.allocation;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.responsedto.AllocationConflictsByResourceResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.AllocationConflictsResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.ResourceRepository;
import com.innovature.resourceit.service.impli.AllocationServiceImpli;
import com.innovature.resourceit.util.CommonFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = GetAllocationConflictListTest.class)
class GetAllocationConflictListTest {

    @InjectMocks
    private AllocationServiceImpli allocationService;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private CommonFunctions commonFunctions;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllocationConflictList() {
        // Mock the required objects
        List<Integer> resourceIdList = Arrays.asList(1, 2, 3);
        Date allocationStartDate = new Date();
        Date allocationEndDate = new Date();

        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.of(createMockResource()));
        when(commonFunctions.checkAllocationConflicts(anyList(), any(), any(), anyInt())).thenReturn(Arrays.asList(createMockConflictResponseDTO()));

        // Test the method
        List<AllocationConflictsByResourceResponseDTO> result = allocationService.getAllocationConflictList(resourceIdList, allocationStartDate, allocationEndDate, 1);

        // Verify the result
        assertEquals(1, result.size());
        // Add more verifications as needed
    }

    @Test
    void testGetAllocationConflictListResourceNotFound() {
        // Mock the required objects
        List<Integer> resourceIdList = Arrays.asList(1, 2, 3);
        Date allocationStartDate = new Date();
        Date allocationEndDate = new Date();

        when(resourceRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(Optional.empty());

        // Test the method and expect BadRequestException
        assertThrows(BadRequestException.class, () -> allocationService.getAllocationConflictList(resourceIdList, allocationStartDate, allocationEndDate, null));

        // Verify that the checkAllocationConflicts method is not called
        verify(commonFunctions, never()).checkAllocationConflicts(anyList(), any(), any(), anyInt());
    }

    private Resource createMockResource() {
        // Create and return a mock Resource
        Resource resource = new Resource();
        resource.setId(1);
        return resource;
    }

    private AllocationConflictsByResourceResponseDTO createMockConflictResponseDTO() {
        // Create and return a mock AllocationConflictsResponseDTO
        return new AllocationConflictsByResourceResponseDTO();
    }
}
