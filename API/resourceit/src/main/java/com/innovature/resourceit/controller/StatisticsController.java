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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private static final String SUCCESSFULLY_DOWNLOAD = "Successfully downloaded";
    @Autowired
    private StatisticsDownloadService statisticsDownloadService;
    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/billability/list")
    public ResponseEntity<Object> listResourcesForStatistics(
            @Valid @RequestBody BillabilitySummaryRequestDTO requestListDTO) {
        PagedResponseDTO<BillabilitySummaryResponseDTO> pagedResponseDTO = statisticsService
                .getAllResource(requestListDTO);
        return new ResponseEntity<>(pagedResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/resource-analysis/{id}")
    public ResponseEntity<Object> getResourceAnalysis(
            @PathVariable("id") Integer id,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        BillabilitySummaryResponseDTO billabilitySummaryResponseDTO = statisticsService
                .getResourceAnalysis(id, startDate, endDate);
        return new ResponseEntity<>(billabilitySummaryResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/resource-analysis/allocations/{id}")
    public ResponseEntity<Object> getProjectAllocationDetails(
            @PathVariable("id") Integer id,
            @RequestParam(name = "pageNumber", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "20") int size,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        PagedResponseDTO<ProjectAllocationResponseDTO> projectAllocationResponseDTOPagedResponseDTO = statisticsService
                .getProjectAllocationDetails(id, page, size, startDate, endDate);
        return new ResponseEntity<>(projectAllocationResponseDTOPagedResponseDTO, HttpStatus.OK);
    }


    @PostMapping("/billability/download")
    public ResponseEntity<Object> downloadResourceData(HttpServletResponse response, @RequestBody BillabilitySummaryRequestDTO requestDTO) {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"billing_summary_report.xlsx\"");
        statisticsDownloadService.billingResourceExcelDownload(response, requestDTO);

        return new ResponseEntity<>(new SuccessResponseDTO("200", SUCCESSFULLY_DOWNLOAD), HttpStatus.OK);
    }

    @PostMapping("/allocation/details/download")
    public ResponseEntity<Object> downloadProjectAllocationDetails(HttpServletResponse response, @RequestBody ResourceAnalysisRequestDTO requestDTO) throws ParseException {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"project_allocation_Detail_report.xlsx\"");
        statisticsDownloadService.getProjectAllocationDetailsExcelDownload(response,requestDTO);

        return new ResponseEntity<>(new SuccessResponseDTO("200", SUCCESSFULLY_DOWNLOAD), HttpStatus.OK);
    }

    @PostMapping("/allocation/download")
    public ResponseEntity<Object> downloadAllProjectAllocationDetails(HttpServletResponse response) throws ParseException {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"project_allocation_report.xlsx\"");
        statisticsDownloadService.getAllProjectAllocationDetailsExcelDownload(response);

        return new ResponseEntity<>(new SuccessResponseDTO("200", SUCCESSFULLY_DOWNLOAD), HttpStatus.OK);
    }

}
