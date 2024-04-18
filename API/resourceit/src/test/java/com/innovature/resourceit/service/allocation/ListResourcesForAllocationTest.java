//package com.innovature.resourceit.service.allocation;
//
//import com.innovature.resourceit.entity.Resource;
//import com.innovature.resourceit.entity.criteriaquery.AllocationRepositoryCriteria;
//import com.innovature.resourceit.entity.customvalidator.ParameterValidator;
//import com.innovature.resourceit.entity.dto.requestdto.AllocationRequestResourceFilterRequestDTO;
//import com.innovature.resourceit.entity.dto.responsedto.AllocationRequestResourceFilterResponseDTO;
//import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
//import com.innovature.resourceit.exceptionhandler.BadRequestException;
//import com.innovature.resourceit.repository.ResourceRepository;
//import com.innovature.resourceit.repository.ResourceSkillRepository;
//import com.innovature.resourceit.service.impli.AllocationServiceImpli;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.MessageSource;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@ContextConfiguration(classes = ListResourcesForAllocationTest.class)
//class ListResourcesForAllocationTest {
//
//    @InjectMocks
//    private AllocationServiceImpli allocationService;
//
//    @Mock
//    private AllocationRepositoryCriteria allocationRepositoryCriteria;
//
//    @Mock
//    private ResourceRepository resourceRepository;
//
//    @Mock
//    private ResourceSkillRepository resourceSkillRepository;
//
//    @Mock
//    private MessageSource messageSource;
//
//    @Mock
//    private ParameterValidator parameterValidator;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void shouldListResourcesForAllocation() {
//        // Mocking the necessary dependencies and data
//        when(parameterValidator.isNumber(anyString(), anyString())).thenReturn(1);
//        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(new ArrayList<>());
//        when(allocationRepositoryCriteria.findFilteredResourceAllocationWithPagination(
//                anyList(), anyList(), anyList(), any(), anyString(), anyString(), any(Pageable.class)))
//                .thenReturn(new PageImpl<>(List.of(new Resource())));
//        AllocationRequestResourceFilterRequestDTO dto = new AllocationRequestResourceFilterRequestDTO();
//        dto.setPageNumber("0");
//        dto.setPageSize("10");
//
//        // Test the method
//        PagedResponseDTO<AllocationRequestResourceFilterResponseDTO> result =
//                allocationService.listResourcesForAllocation(dto);
//
//        // Verify the interactions and results
//        assertEquals(1, result.getItems().size());
//        verify(allocationRepositoryCriteria, times(1))
//                .findFilteredResourceAllocationWithPagination(anyList(), anyList(), anyList(), any(), anyString(), anyString(), any(Pageable.class));
//    }
//
//    @Test
//    void shouldThrowBadRequestExceptionWhenParsingDateFails() {
//        // Mocking the necessary dependencies and data
//        when(parameterValidator.isNumber(anyString(), anyString())).thenReturn(0);
//        when(parameterValidator.isNumbersNum(anyString(), anyList())).thenReturn(new ArrayList<>());
//
//
//        // Test the method and expect an exception
//        assertThrows(BadRequestException.class,
//                () -> allocationService.listResourcesForAllocation(new AllocationRequestResourceFilterRequestDTO()));
//
//        // Verify the interactions and results
//        verify(allocationRepositoryCriteria, never())
//                .findFilteredResourceAllocationWithPagination(anyList(), anyList(), anyList(), any(), anyString(), anyString(), any(Pageable.class));
//    }
//
//    // Add more test methods for additional scenarios
//}
