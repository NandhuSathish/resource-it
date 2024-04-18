package com.innovature.resourceit.entity.dto.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ResourceAnalysisRequestDTO {
    private Integer id;
    private Date startDate;
    private Date endDate;

}
