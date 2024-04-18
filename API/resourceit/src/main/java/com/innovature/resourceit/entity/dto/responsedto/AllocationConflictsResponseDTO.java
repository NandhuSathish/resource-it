package com.innovature.resourceit.entity.dto.responsedto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllocationConflictsResponseDTO {
    Integer resourceId;
    List<AllocationConflictsByResourceResponseDTO> conflicts;
}
