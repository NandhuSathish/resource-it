package com.innovature.resourceit.entity.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListByManagerResponseDTO {
    private Integer projectId;
    private String projectName;
    private String projectCode;
}
