/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

import lombok.*;

/**
 *
 * @author abdul.fahad
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HolidayCalendar {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int calenderId;
    
    private Date date;
    
    private String name;
    
    private String day;
    
    private int year;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
}
