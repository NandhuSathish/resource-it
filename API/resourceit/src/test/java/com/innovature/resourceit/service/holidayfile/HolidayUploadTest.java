package com.innovature.resourceit.service.holidayfile;

import com.innovature.resourceit.entity.HolidayCalendar;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.HolidayCalendarRepository;
import com.innovature.resourceit.service.impli.HolidayCalenderServiceImpli;
import com.opencsv.CSVReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * @author abdul.fahad
 */
public class HolidayUploadTest {

    @Mock
    CSVReader csvReader;

    @Mock
    MessageSource messageSource;

    @Mock
    HolidayCalendarRepository holidayCalenderRepository;

    @InjectMocks
    HolidayCalenderServiceImpli holidayCalenderServiceImpli;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRemoveBomWithBomPresent() throws Exception {

        byte[] bomBytes = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] contentBytes = {1, 2, 3};
        byte[] combinedBytes = new byte[bomBytes.length + contentBytes.length];
        System.arraycopy(bomBytes, 0, combinedBytes, 0, bomBytes.length);
        System.arraycopy(contentBytes, 0, combinedBytes, bomBytes.length, contentBytes.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(combinedBytes);

        InputStream result = holidayCalenderServiceImpli.removeBom(inputStream);

        byte[] resultBytes = new byte[contentBytes.length];
        result.read(resultBytes);

        assertEquals(contentBytes[0], resultBytes[0]);
        assertEquals(contentBytes[1], resultBytes[1]);
        assertEquals(contentBytes[2], resultBytes[2]);

        assertTrue(bomBytes[0] == (byte) 0xEF && bomBytes[1] == (byte) 0xBB && bomBytes[2] == (byte) 0xBF);

        int bytesRead;
        byte[] readBytes = new byte[3];
        bytesRead = result.read(readBytes);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testRemoveBomWithNoBomPresent() throws Exception {
        // Arrange
        byte[] contentBytes = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(contentBytes);

        InputStream result = holidayCalenderServiceImpli.removeBom(inputStream);

        // Assert
        byte[] resultBytes = new byte[contentBytes.length];
        result.read(resultBytes);

        assertEquals(contentBytes[0], resultBytes[0]);
        assertEquals(contentBytes[1], resultBytes[1]);
        assertEquals(contentBytes[2], resultBytes[2]);
    }

    @Test
    public void testHolidayUploadFileWithValidCSV() throws IOException, Exception {

        String csvContent = "Date,Day,Description\n28-11-2024,Thursday,Republic Day\n";
        MockMultipartFile inputFile = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        holidayCalenderServiceImpli.holidayCalenderExcelUpload(inputFile);

    }

    @Test
    public void testHolidayUploadFileWithValidCSVInvalid() throws IOException, Exception {

        String csvContent = "Date,Day,Description\n";
        MockMultipartFile inputFile = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        when(messageSource.getMessage(eq("EMPTY_FILE"), eq(null), any()))
                .thenReturn("2039-Empty file");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.holidayCalenderExcelUpload(inputFile);
        });

        assertNotNull(exception);
        assertEquals("2039-Empty file", exception.getBody().getDetail());

    }

    @Test
    public void testHolidayUploadFileWithValidCSVInvalidDuplicateDate() throws IOException, Exception {

        String csvContent = "Date,Day,Description\n28-11-2024,Thursday,Republic Day\n28-11-2024,Thursday,New Date\n";
        MockMultipartFile inputFile = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        when(messageSource.getMessage(eq("DUPLICATE_DATE"), eq(null), any()))
                .thenReturn("2037-Unique date is needed");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.holidayCalenderExcelUpload(inputFile);
        });

        assertNotNull(exception);
        assertEquals("2037-Unique date is needed", exception.getBody().getDetail());

    }

    @Test
    public void testHolidayUploadFileWithValidCSVInvalidDuplicateDescription() throws IOException, Exception {

        String csvContent = "Date,Day,Description\n28-11-2024,Thursday,Republic Day\n29-11-2024,Thursday,Republic Day\n";
        MockMultipartFile inputFile = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        when(messageSource.getMessage(eq("DUPLICATE_DESCRIPTION"), eq(null), any()))
                .thenReturn("2038-Unique description is needed");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.holidayCalenderExcelUpload(inputFile);
        });

        assertNotNull(exception);
        assertEquals("2038-Unique description is needed", exception.getBody().getDetail());

    }

    @Test
    public void testFileMaxSizeChecking() {

        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", new byte[26214401]);

        MockMultipartFile inputFileWithinLimit = new MockMultipartFile("file", "test.csv", "text/csv", new byte[26214400]);

        assertThrows(BadRequestException.class, () -> holidayCalenderServiceImpli.holidayCalenderExcelUpload(file));

        assertDoesNotThrow(() -> holidayCalenderServiceImpli.fileMaxSizeChecking(inputFileWithinLimit));
    }

    @Test
    public void testGetAllHolidayCalenderUsingYear() {
        int year = 2023;
        List<HolidayCalendar> expectedHolidayCalenders = new ArrayList<>();
        expectedHolidayCalenders.add(new HolidayCalendar(1, new Date(), "name", "day", 2023, new Date(), new Date()));
        Optional<List<HolidayCalendar>> optionalHoliday = Optional.of(expectedHolidayCalenders);

        when(holidayCalenderRepository.findByYear(year, Sort.by(Sort.Direction.ASC, "date"))).thenReturn(optionalHoliday);

        List<HolidayCalendar> result = holidayCalenderServiceImpli.getAllHolidayCalenderUsingYear(year);

        assertEquals(expectedHolidayCalenders, result);
    }

    @Test
    public void testGetEmptyHolidayCalenderUsingYear() {
        int year = 2023;
        Optional<List<HolidayCalendar>> optionalHoliday = Optional.empty();
        when(holidayCalenderRepository.findByYear(year, Sort.by(Sort.Direction.ASC, "date"))).thenReturn(optionalHoliday);

        List<HolidayCalendar> result = holidayCalenderServiceImpli.getAllHolidayCalenderUsingYear(year);

        assertEquals(0, result.size());
    }

    @Test
    public void testValidateDay() {

        String[] row = {"21-08-2001", "error", "descrption"};
        when(messageSource.getMessage(eq("INVALID_WEEKDAY"), eq(null), any()))
                .thenReturn("2033-Invalid week day");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.dayValidation(row);
        });

        assertNotNull(exception);
        assertEquals("2033-Invalid week day", exception.getBody().getDetail());
    }

    @Test
    public void testRowvalidationLength() {
        String[] row = {"21-08-2001", "error"};
        when(messageSource.getMessage(eq("CONTENT_MISMATCH"), eq(null), any()))
                .thenReturn("2002-Field Mismatch");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.validation(row);
        });

        assertNotNull(exception);
        assertEquals("2002-Field Mismatch", exception.getBody().getDetail());
    }

    @Test
    public void testRowValidationDateNull() {
        String[] row = {"", "Monday", "descrption"};
        when(messageSource.getMessage(eq("DATE_H_NULL"), eq(null), any()))
                .thenReturn("2032-Date required");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.validation(row);
        });

        assertNotNull(exception);
        assertEquals("2032-Date required", exception.getBody().getDetail());
    }

    @Test
    public void testRowValidationDayNull() {
        String[] row = {"21-08-2001", "", "descrption"};
        when(messageSource.getMessage(eq("DAY_NULL"), eq(null), any()))
                .thenReturn("2031-Day required");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.validation(row);
        });

        assertNotNull(exception);
        assertEquals("2031-Day required", exception.getBody().getDetail());
    }

    @Test
    public void testRowValidationDiscriptionNull() {
        String[] row = {"21-08-2001", "Monday", ""};
        when(messageSource.getMessage(eq("DESCRIPTION_NULL"), eq(null), any()))
                .thenReturn("2030-Description required");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.validation(row);
        });

        assertNotNull(exception);
        assertEquals("2030-Description required", exception.getBody().getDetail());
    }

    @Test
    public void testValidateDateFormatInValid() {
        String d = "21/05/2002";
        when(messageSource.getMessage(eq("DATE_FORMAT_MISMATCH"), eq(null), any()))
                .thenReturn("2009-Invalid date format");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.validateDateFormat(d);
        });

        assertNotNull(exception);
        assertEquals("2009-Invalid date format", exception.getBody().getDetail());
    }

    @Test
    public void testValidateDateInValid() {

        when(messageSource.getMessage(eq("INVALID_DATE"), eq(null), any()))
                .thenReturn("2034-Invalid date");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.validateDate("21-05-2022", 2023);
        });

        assertNotNull(exception);
        assertEquals("2034-Invalid date", exception.getBody().getDetail());

    }

    @Test
    public void testFetchAll() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        HolidayCalendar holidayCalender = new HolidayCalendar();
        holidayCalender.setCalenderId(1);
        holidayCalender.setDate(sdf.parse("22-01-2024"));
        holidayCalender.setName("Function");
        holidayCalender.setYear(2024);

        HolidayCalendar holidayCalender1 = new HolidayCalendar();
        holidayCalender1.setCalenderId(2);
        holidayCalender1.setDate(sdf.parse("22-01-2023"));
        holidayCalender1.setName("Function");
        holidayCalender1.setYear(2023);

        List<HolidayCalendar> holidayCalenders = Arrays.asList(holidayCalender, holidayCalender1);

        when(holidayCalenderRepository.findDistinctByYear()).thenReturn(Optional.of(holidayCalenders));

        List<HolidayCalendar> holidayCalenders1 = holidayCalenderServiceImpli.fetchAll();

        assertEquals(holidayCalenders.size(), holidayCalenders1.size());
    }

    @Test
    public void testFetchAllNullCase() throws ParseException {

        when(holidayCalenderRepository.findDistinctByYear()).thenReturn(Optional.empty());

        List<HolidayCalendar> holidayCalenders1 = holidayCalenderServiceImpli.fetchAll();

        assertEquals(0, holidayCalenders1.size());
    }

    @Test
    public void testDeleteByYearValidCase() {
        int yearToDelete = 2023;

        // Mock the behavior of holidayCalenderRepository.deleteByYear to succeed
        doNothing().when(holidayCalenderRepository).deleteByYear(yearToDelete);

        // Call the method to be tested
        holidayCalenderServiceImpli.deleteByYear(yearToDelete);

        // Verify that deleteByYear is called with the correct argument
        verify(holidayCalenderRepository, times(1)).deleteByYear(yearToDelete);
    }

    @Test
    public void testDeleteByYearInvalidCase() {
        int yearToDelete = 2023;

        when(messageSource.getMessage(eq("CALENDAR_DELETE_FAILED"), eq(null), any()))
                .thenReturn("1202-Holiday calender deletion failed.");

        doThrow(new BadRequestException()).when(holidayCalenderRepository).deleteByYear(yearToDelete);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            holidayCalenderServiceImpli.deleteByYear(yearToDelete);
        });

        assertNotNull(exception);
        assertEquals("1202-Holiday calender deletion failed.", exception.getBody().getDetail());
    }

}
