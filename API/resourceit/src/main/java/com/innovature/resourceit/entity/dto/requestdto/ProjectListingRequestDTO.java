/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.requestdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import lombok.Data;

/**
 * @author abdul.fahad
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class ProjectListingRequestDTO {

    private String pageNumber;

    private String pageSize;

    private String projectName;

    private String startDate;

    private String endDate;

    private List<String> projectState;

    private List<String> managerId;

    private List<String> projectType;

    private Boolean sortOrder = false;

    private String sortKey;
}
