package com.innovature.resourceit.entity.criteriaquery;

import com.innovature.resourceit.entity.Resource;
import com.innovature.resourceit.entity.dto.requestdto.AllocationRequestResourceFilterRequestDTO;

import java.util.List;

public interface AllocationRepositoryCriteria {

    List<Resource> findFilteredResourceAllocationWithPagination(AllocationRequestResourceFilterRequestDTO dto);
}
