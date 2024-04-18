/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.responsedto;

import lombok.Data;

/**
 *
 * @author abdul.fahad
 */
@Data
public class DashboardCountResponseDTO {
    
    private long totalProjectCount;
    
    private long internalProjectCount;
    
    private long billableProjectCount;
    
    private long totalResourceCount;
    
    private long internalResourceCount;
    
    private long billableResourceCount;
    
    private long benchResourceCount;
    
}
