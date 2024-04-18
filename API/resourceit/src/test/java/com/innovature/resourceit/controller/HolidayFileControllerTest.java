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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author abdul.fahad
 */
@SpringBootTest
@ContextConfiguration(classes = HolidayFileControllerTest.class)
public class HolidayFileControllerTest {

    @Mock
    MessageSource messageSource;

    @Mock
    HolidayCalenderService holidayCalenderService;

    @InjectMocks
    HolidayFileController holidayFileController;

    @Mock
    HttpServletResponse response;

    @Mock
    PrintWriter writer;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void holidayUploadValid() throws Exception {
        String csvData = "Date,Day,Description\nval1,val2,val3\n";
        InputStream is = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        MultipartFile mockFile = new MockMultipartFile("data", "filename.csv", "text/csv", is);

        doNothing().when(holidayCalenderService).holidayCalenderExcelUpload(mockFile);

        ResponseEntity<Object> response = holidayFileController.uploadHolidayCalenderData(mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
     void holidayUploadThrowsBadRequestOnInvalidFileType() throws Exception {
        String csvData = "Date,Day,Description\nval1,val2,val3\n";
        InputStream is = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        MultipartFile mockFile = new MockMultipartFile("data", "filename.xlsx", "text/xlsx", is);

        doNothing().when(holidayCalenderService).holidayCalenderExcelUpload(mockFile);
        Assertions.assertThrows(BadRequestException.class, () ->holidayFileController.uploadHolidayCalenderData(mockFile));

    }

    @Test
    public void hilidayUploadInValid() throws Exception {

        when(messageSource.getMessage(eq("FILE_REQUIRED"), eq(null), any()))
                .thenReturn("2000-File required");
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "",
                "text/plain",
                new byte[0]
        );

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayFileController.uploadHolidayCalenderData(emptyFile);
        });

        assertNotNull(exception);
        assertEquals("2000-File required", exception.getBody().getDetail());
    }

    @Test
    public void holidayDownloadValid() throws Exception {
        List<HolidayCalendar> holidayCalenders = Arrays.asList(
                new HolidayCalendar(1, new Date(), "Sunday", "New Year's Day", 2023, new Date(), new Date()),
                new HolidayCalendar(2, new Date(), "Monday", "Labor Day", 2023, new Date(), new Date())
                // Add more holidayCalenders as needed
        );

        doReturn(holidayCalenders).when(holidayCalenderService).getAllHolidayCalenderUsingYear(2023);

        when(response.getWriter()).thenReturn(writer);

        holidayFileController.downloadHolidayCalenderData("2023", response);

        // Verify that the response has the expected behavior
        verify(response).setContentType("text/csv");
        verify(response).setHeader("Content-Disposition", "attachment; filename=holiday.csv");
        verify(response.getWriter()).write("Date,Day,Description\n");
    }
    @Test
     void holidayDownloadInvalidYear() throws Exception {
        List<HolidayCalendar> holidayCalenders = Arrays.asList(
                new HolidayCalendar(1, new Date(), "Sunday", "New Year's Day", 2023, new Date(), new Date()),
                new HolidayCalendar(2, new Date(), "Monday", "Labor Day", 2023, new Date(), new Date())
                // Add more holidayCalenders as needed
        );

        doReturn(holidayCalenders).when(holidayCalenderService).getAllHolidayCalenderUsingYear(2023);

        when(response.getWriter()).thenReturn(writer);
        ResponseEntity<Object> errorResponseDTO= holidayFileController.downloadHolidayCalenderData("abc", response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponseDTO.getStatusCode().value());

    }
    @Test
    void testFetchAll() {
        List<HolidayCalendar> holidayCalenderList = Arrays.asList(
                new HolidayCalendar(1, new Date(), "Sunday", "New Year's Day", 2023, new Date(), new Date()),
                new HolidayCalendar(2, new Date(), "Monday", "Labor Day", 2023, new Date(), new Date())
        );

        MockitoAnnotations.openMocks(this);
        doReturn(holidayCalenderList).when(holidayCalenderService).fetchAll();

        ResponseEntity<List<HolidayCalendarResponseDTO>> responseEntity = holidayFileController.fetchAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<HolidayCalendarResponseDTO> responseBody = responseEntity.getBody();
        assertEquals(2, responseBody.size());

        assertEquals(1, responseBody.get(0).getId());
        assertEquals(2023, responseBody.get(0).getYear());

    }
    
    @Test
    void testDeleteByYear_Success() {
        // Given
        String year = "2023";

        // When
        ResponseEntity<Object> responseEntity = holidayFileController.deleteByYear(year);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(SuccessResponseDTO.class, responseEntity.getBody().getClass());
        // Verify that deleteByYear method of holidayCalenderService was called with the correct argument
        verify(holidayCalenderService, times(1)).deleteByYear(eq(2023));
    }

    @Test
    void testDeleteByYear_InvalidYear() {
        // Given
        String invalidYear = "invalid";

        // When
        ResponseEntity<Object> responseEntity = holidayFileController.deleteByYear(invalidYear);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(ErrorResponseDTO.class, responseEntity.getBody().getClass());
        // Verify that deleteByYear method of holidayCalenderService was not called
        verify(holidayCalenderService, never()).deleteByYear(anyInt());
    }
}
