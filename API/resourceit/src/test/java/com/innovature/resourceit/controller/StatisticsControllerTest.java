package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.dto.requestdto.BillabilitySummaryRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceAnalysisRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.BillabilitySummaryResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ProjectAllocationResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
import com.innovature.resourceit.service.StatisticsDownloadService;
import com.innovature.resourceit.service.StatisticsService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = StatisticsControllerTest.class)
public class StatisticsControllerTest {
    @Mock
    MessageSource messageSource;
    @Mock
    private HttpServletResponse response;

    @Mock
    private StatisticsDownloadService statisticsDownloadService;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void downloadResourceData() {
        doNothing().when(statisticsDownloadService).billingResourceExcelDownload(any(HttpServletResponse.class), any(BillabilitySummaryRequestDTO.class));

        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        ResponseEntity<Object> responseEntity = statisticsController.downloadResourceData(response, requestDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully downloaded", ((SuccessResponseDTO) responseEntity.getBody()).getSuccessMessage());

        // Verify that setContentDisposition is called with the correct argument
        verify(response).setHeader(eq(HttpHeaders.CONTENT_DISPOSITION), eq("attachment; filename=\"billing_summary_report.xlsx\""));

    }

    @Test
    void testListResourcesForStatistics() {
        BillabilitySummaryRequestDTO requestDTO = new BillabilitySummaryRequestDTO();
        PagedResponseDTO<BillabilitySummaryResponseDTO> pagedResponseDTO = new PagedResponseDTO<>();
        BillabilitySummaryResponseDTO billabilitySummaryResponseDTO = new BillabilitySummaryResponseDTO();
        billabilitySummaryResponseDTO.setEmployeeId(1);
        List<BillabilitySummaryResponseDTO> responseDTOList = new ArrayList<>();
        pagedResponseDTO.setCurrentPage(1);
        pagedResponseDTO.setTotalPages(1);
        pagedResponseDTO.setItems(responseDTOList);
        pagedResponseDTO.setTotalItems(5);

        // Use any() without saving it to a variable, to ensure the same instance is used
        when(statisticsService.getAllResource(any(BillabilitySummaryRequestDTO.class))).thenReturn(pagedResponseDTO);

        ResponseEntity<Object> responseEntity = statisticsController.listResourcesForStatistics(requestDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pagedResponseDTO, responseEntity.getBody());

    }

    @Test
    void testGetProjectAllocationsForStatistics() {
        PagedResponseDTO<ProjectAllocationResponseDTO> pagedResponseDTO = new PagedResponseDTO<>();
        List<ProjectAllocationResponseDTO> responseDTOList = new ArrayList<>();
        pagedResponseDTO.setCurrentPage(1);
        pagedResponseDTO.setTotalPages(1);
        pagedResponseDTO.setItems(responseDTOList);
        pagedResponseDTO.setTotalItems(5);

        // Use any() without saving it to a variable, to ensure the same instance is used
        when(statisticsService.getProjectAllocationDetails(anyInt(), anyInt(), anyInt(), any(), any())).thenReturn(pagedResponseDTO);

        ResponseEntity<Object> responseEntity = statisticsController.getProjectAllocationDetails(anyInt(), anyInt(), anyInt(), any(), any());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pagedResponseDTO, responseEntity.getBody());

    }

    @Test
    void testGetResourceAnalysis() {
        BillabilitySummaryResponseDTO responseDTO = new BillabilitySummaryResponseDTO();

        when(statisticsService.getResourceAnalysis(anyInt(), any(), any())).thenReturn(responseDTO);

        ResponseEntity<Object> responseEntity = statisticsController.getResourceAnalysis(anyInt(), any(), any());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseDTO, responseEntity.getBody());

    }

    @Test
    void  testDownloadProjectAllocationDetails() throws ParseException {
        ResourceAnalysisRequestDTO requestDTO=new ResourceAnalysisRequestDTO();
        requestDTO.setId(1);
        requestDTO.setStartDate(new Date());
        requestDTO.setEndDate(new Date());
        doNothing().when(statisticsDownloadService).getProjectAllocationDetailsExcelDownload(any(HttpServletResponse.class),any());

        ResponseEntity<Object> responseEntity = statisticsController.downloadProjectAllocationDetails(response, requestDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully downloaded", ((SuccessResponseDTO) responseEntity.getBody()).getSuccessMessage());

        // Verify that setContentDisposition is called with the correct argument
        verify(response).setHeader(eq(HttpHeaders.CONTENT_DISPOSITION), eq("attachment; filename=\"project_allocation_Detail_report.xlsx\""));

    }
    @Test
    void  testDownloadAllProjectAllocationDetails() throws ParseException {
        doNothing().when(statisticsDownloadService).getAllProjectAllocationDetailsExcelDownload(any(HttpServletResponse.class));

        ResponseEntity<Object> responseEntity = statisticsController.downloadAllProjectAllocationDetails(response);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully downloaded", ((SuccessResponseDTO) responseEntity.getBody()).getSuccessMessage());

        // Verify that setContentDisposition is called with the correct argument
        verify(response).setHeader(eq(HttpHeaders.CONTENT_DISPOSITION), eq("attachment; filename=\"project_allocation_report.xlsx\""));

    }
}
