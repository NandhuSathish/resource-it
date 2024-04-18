/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.controller;

import com.innovature.resourceit.entity.HolidayCalendar;
import com.innovature.resourceit.entity.dto.responsedto.ErrorResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.HolidayCalendarResponseDTO;
import com.innovature.resourceit.entity.dto.responsedto.SuccessResponseDTO;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.service.HolidayCalenderService;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author abdul.fahad
 */
@RestController
@RequestMapping("/api/v1/holiday")
public class HolidayFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayFileController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HolidayCalenderService holidayCalenderService;

    private static final String YEARNUMBER = "Year is a number";

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadHolidayCalenderData(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty() && file.getOriginalFilename() != null) {
            if ( !file.getOriginalFilename().endsWith(".csv")) {
                LOGGER.error("Incorrect file");
                throw new BadRequestException(messageSource.getMessage("INCORRECT_FILE", null, Locale.ENGLISH));
            }
            holidayCalenderService.holidayCalenderExcelUpload(file);
        } else {
            LOGGER.error("No file uploaded");
            throw new BadRequestException(messageSource.getMessage("FILE_REQUIRED", null, Locale.ENGLISH));
        }
        return new ResponseEntity<>(new SuccessResponseDTO("200", "Successfully uploaded"), HttpStatus.OK);
    }

    @GetMapping("/download/{year}")
    public ResponseEntity<Object> downloadHolidayCalenderData(@PathVariable String year, HttpServletResponse response) throws IOException {

        int yearValue = 0;
        try {
            yearValue = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            LOGGER.error(YEARNUMBER);
            return new ResponseEntity<>(new ErrorResponseDTO(YEARNUMBER, "2008"), HttpStatus.BAD_REQUEST);
        }
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=holiday.csv");

        PrintWriter writer = response.getWriter();

        writer.write("Date,Day,Description\n");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        List<HolidayCalendar> holidayCalenders = holidayCalenderService.getAllHolidayCalenderUsingYear(yearValue);
        holidayCalenders.stream().forEach(x -> {
            String date = sdf.format(x.getDate());
            writer.write(date + "," + x.getDay() + "," + x.getName() + "\n");
        });
        writer.close();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<HolidayCalendarResponseDTO>> fetchAll() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        List<HolidayCalendar> holidayCalenderList = holidayCalenderService.fetchAll();
        List<HolidayCalendarResponseDTO> list = new ArrayList<>();
        int counter = 1;
        for (HolidayCalendar holidayCalender : holidayCalenderList) {
            HolidayCalendarResponseDTO holidayCalendarResponseDTO = new HolidayCalendarResponseDTO();
            holidayCalendarResponseDTO.setId(counter);
            holidayCalendarResponseDTO.setYear(holidayCalender.getYear());
            holidayCalendarResponseDTO.setUploadedDate(dateFormat.format(holidayCalender.getCreatedDate()));
            list.add(holidayCalendarResponseDTO);
            counter++;
        }
        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    @DeleteMapping("/{year}")
    public ResponseEntity<Object> deleteByYear(@PathVariable String year) {
        int yearValue = 0;
        try {
            yearValue = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            LOGGER.error(YEARNUMBER);
            return new ResponseEntity<>(new ErrorResponseDTO(YEARNUMBER, "2008"), HttpStatus.BAD_REQUEST);
        }

        holidayCalenderService.deleteByYear(yearValue);
        return new ResponseEntity<>(new SuccessResponseDTO("200", "Successfully deleted"), HttpStatus.OK);

    }
}
