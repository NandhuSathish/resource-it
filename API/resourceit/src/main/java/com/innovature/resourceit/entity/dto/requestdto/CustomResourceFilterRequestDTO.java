/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author abdul.fahad
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResourceFilterRequestDTO {
    
    private String name;
    private int lowerExperience;
    
    private int higherExperience;
    
}
