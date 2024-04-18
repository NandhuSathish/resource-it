/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.service;

import com.innovature.resourceit.entity.HolidayCalendar;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author abdul.fahad
 */
public interface HolidayCalenderService {
    
    public void holidayCalenderExcelUpload(MultipartFile file) throws IOException;
    
    public List<HolidayCalendar> getAllHolidayCalenderUsingYear(int year);

    List<HolidayCalendar> fetchAll();

    void deleteByYear(Integer year);
}
