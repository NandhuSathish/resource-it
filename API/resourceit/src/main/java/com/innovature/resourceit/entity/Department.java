/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity;

import com.innovature.resourceit.entity.dto.requestdto.DepartmentRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 *
 * @author abdul.fahad
 */
@Entity
@Table(name = "department")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;
    private String name;
    private Integer displayOrder;

    public Department(DepartmentRequestDTO departmentRequestDTO) {
        this.name = departmentRequestDTO.getName();
        this.displayOrder = departmentRequestDTO.getDisplayOrder();
    }
}
