package com.innovature.resourceit.entity.criteriaquery;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.requestdto.BillabilitySummaryRequestDTO;

import java.util.List;

public interface BillabilityRepositoryCriteria {

    List<Resource>findResourceForBillabilityStatistic(BillabilitySummaryRequestDTO requestDTO);
}
