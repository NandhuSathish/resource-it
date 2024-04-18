package com.innovature.resourceit.service;

import com.innovature.resourceit.entity.dto.requestdto.BillabilitySummaryRequestDTO;
import com.innovature.resourceit.entity.dto.requestdto.ResourceAnalysisRequestDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.util.Date;

public interface StatisticsDownloadService {
    public void billingResourceExcelDownload(HttpServletResponse response, BillabilitySummaryRequestDTO requestDTO);

    public void getProjectAllocationDetailsExcelDownload(HttpServletResponse response, ResourceAnalysisRequestDTO requestDTO) throws ParseException;
    public void getAllProjectAllocationDetailsExcelDownload(HttpServletResponse response) throws ParseException;
}
