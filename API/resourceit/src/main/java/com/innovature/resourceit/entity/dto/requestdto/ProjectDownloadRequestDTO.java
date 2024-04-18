/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.requestdto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author abdul.fahad
 */
@Getter
@Setter
@NoArgsConstructor
public class ProjectDownloadRequestDTO {
    
    private String projectName;

    private String startDate;

    private String endDate;

    private List<String> projectState;

    private List<String> managerId;

    private List<String> projectType;
    
}
