package com.innovature.resourceit.entity.dto.responsedto;

import com.innovature.resourceit.entity.dto.requestdto.ResourceSkillRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResourceResponseDTO {
    private Integer id;
    private Integer employeeId;
    private Integer departmentId;
    private String email;
    private String name;
    private String joiningDate;
    private Integer experience;
    private Integer role;
    private Byte allocationStatus;
    private String createdDate;
    private String updatedDate;
    private Byte status;
    private List<ResourceSkillRequestDTO> skills;
    private int totalExperience;

}

