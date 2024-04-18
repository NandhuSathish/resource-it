package com.innovature.resourceit.service;

import com.innovature.resourceit.entity.dto.requestdto.BillabilitySummaryRequestDTO;
import com.innovature.resourceit.entity.dto.responsedto.BillabilitySummaryResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.PagedResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.ProjectAllocationResponseDTO;

import java.util.Date;

public interface StatisticsService {

    PagedResponseDTO<BillabilitySummaryResponseDTO> getAllResource(BillabilitySummaryRequestDTO requestDTO);

    BillabilitySummaryResponseDTO getResourceAnalysis(Integer id, Date startDate, Date endDate);

    PagedResponseDTO<ProjectAllocationResponseDTO> getProjectAllocationDetails(Integer id, int page, int size, Date startDate, Date endDate);
}
