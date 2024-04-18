/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.innovature.resourceit.service.impli;

import com.innovature.resourceit.entity.HolidayCalendar;
import com.innovature.resourceit.exceptionhandler.BadRequestException;
import com.innovature.resourceit.repository.HolidayCalendarRepository;
import com.innovature.resourceit.service.HolidayCalenderService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author abdul.fahad
 */
@Service
public class HolidayCalenderServiceImpli implements HolidayCalenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayCalenderServiceImpli.class);

    private enum DAY {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HolidayCalendarRepository holidayCalenderRepository;

    @Override
    public void holidayCalenderExcelUpload(MultipartFile file) throws IOException {

        fileMaxSizeChecking(file);

        try (Reader reader = new InputStreamReader(removeBom(file.getInputStream()))) {
            CSVReader csvReader = new CSVReader(reader);
            csvProcessing(csvReader);
            csvReader.close();
        } catch (IOException e) {
            LOGGER.error("Incorrect file");
            throw new IOException(messageSource.getMessage("INCORRECT_FILE", null, Locale.ENGLISH));
        } catch (CsvException ex) {
            java.util.logging.Logger.getLogger(HolidayCalenderServiceImpli.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public List<HolidayCalendar> getAllHolidayCalenderUsingYear(int year) {
        Optional<List<HolidayCalendar>> optionalHoliday = holidayCalenderRepository.findByYear(year, Sort.by(Sort.Direction.ASC, "date"));
        if (optionalHoliday.isPresent()) {
            return optionalHoliday.get();
        }
        return new ArrayList<>();
    }

    @Override
    public List<HolidayCalendar> fetchAll() {
        Optional<List<HolidayCalendar>> holidayCalenderList = holidayCalenderRepository.findDistinctByYear();
        if (holidayCalenderList.isPresent()) {
            return holidayCalenderList.get();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void deleteByYear(Integer year) {
        try {
            holidayCalenderRepository.deleteByYear(year);
        } catch (Exception e) {
            throw new BadRequestException(messageSource.getMessage("CALENDAR_DELETE_FAILED", null, Locale.ENGLISH));
        }
    }

    public void csvProcessing(CSVReader csvReader) throws IOException, CsvException {

        List<String[]> rows = csvReader.readAll();
        int rowCount = 0;
        List<HolidayCalendar> holidayCalenders = new ArrayList<>();
        int year = 0;
        List<String> duplicateChecking = new ArrayList<>();
        if (rows.size() <= 1) {
            throw new BadRequestException(messageSource.getMessage("EMPTY_FILE", null, Locale.ENGLISH));
        }
        for (String[] row : rows) {

            if (rowCount == 0) {
                checkingHeaders(row);
                rowCount++;
                continue;
            }

            HolidayCalendar holidayCalender = new HolidayCalendar();

            validation(row);

            if (duplicateChecking.contains(row[0])) {
                throw new BadRequestException(messageSource.getMessage("DUPLICATE_DATE", null, Locale.ENGLISH));
            }

            if (duplicateChecking.contains(row[2])) {
                throw new BadRequestException(messageSource.getMessage("DUPLICATE_DESCRIPTION", null, Locale.ENGLISH));
            }

            dayValidation(row);

            LocalDate d = validateDateFormat(row[0]);

            year = validateDate(row[0], year);

            holidayCalender.setDate((Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant())));
            holidayCalender.setCreatedDate(new Date());
            holidayCalender.setUpdatedDate(new Date());
            holidayCalender.setYear(year);
            holidayCalender.setName(row[2].trim());
            holidayCalender.setDay(row[1]);
            holidayCalenders.add(holidayCalender);
            duplicateChecking.add(row[0]);
            duplicateChecking.add(row[2]);
        }
        checkHolidayCalenderPresentForYear(year);

        holidayCalenderRepository.saveAll(holidayCalenders);

    }

    public void checkHolidayCalenderPresentForYear(int year) {
        holidayCalenderRepository.findByYear(year, Sort.by(Sort.Direction.ASC, "date")).ifPresent(x -> x.stream().mapToInt(z -> z.getCalenderId()).forEach(id -> holidayCalenderRepository.deleteById(id)));

    }

    public void dayValidation(String[] row) {
        boolean matched = Arrays.stream(DAY.values()).anyMatch(x -> x.name().equalsIgnoreCase(row[1]));
        if (!matched) {
            throw new BadRequestException(messageSource.getMessage("INVALID_WEEKDAY", null, Locale.ENGLISH));
        }
    }

    public void validation(String[] row) {
        if (row.length != 3) {
            throw new BadRequestException(messageSource.getMessage("CONTENT_MISMATCH", null, Locale.ENGLISH));
        }
        if (row[0] == null || row[0].isEmpty() || row[0].trim().equals("")) {
            throw new BadRequestException(messageSource.getMessage("DATE_H_NULL", null, Locale.ENGLISH));
        }
        if (row[1] == null || row[1].isEmpty() || row[1].trim().equals("")) {
            throw new BadRequestException(messageSource.getMessage("DAY_NULL", null, Locale.ENGLISH));
        }

        if (row[2] == null || row[2].isEmpty() || row[2].trim().equals("")) {
            throw new BadRequestException(messageSource.getMessage("DESCRIPTION_NULL", null, Locale.ENGLISH));
        }
    }

    public LocalDate validateDateFormat(String dateString) {
        LocalDate d;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            d = LocalDate.parse(dateString, formatter);

        } catch (DateTimeParseException n) {
            throw new BadRequestException(messageSource.getMessage("DATE_FORMAT_MISMATCH", null, Locale.ENGLISH));
        }
        return d;
    }

    public int validateDate(String dateString, int year) {
        String[] dateArray;
        try {
            dateArray = dateString.split("-");
            LocalDate date = LocalDate.of(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[0]));
            if (year != 0 && year != date.getYear()) {
                throw new BadRequestException(messageSource.getMessage("INVALID_DATE", null, Locale.ENGLISH));
            }
            return date.getYear();
        } catch (java.time.DateTimeException n) {
            throw new BadRequestException("2036-" + n.getMessage());
        }
    }

    public void fileMaxSizeChecking(MultipartFile inputFile) {

        long maxSize = 26214400;
        if (inputFile.getSize() > maxSize) {
            LOGGER.error("Invalid file size error. Received file size is {}", inputFile.getSize());
            throw new BadRequestException(messageSource.getMessage("INVALID_FILE_SIZE", null, Locale.ENGLISH));
        }

    }

    public InputStream removeBom(InputStream inputStream) throws IOException {
        // Check for BOM (Byte Order Mark) and skip it if present
        PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream, 3);
        byte[] bomBytes = new byte[3];
        if (pushbackInputStream.read(bomBytes) >= 3 && bomBytes[0] == (byte) 0xEF && bomBytes[1] == (byte) 0xBB && bomBytes[2] == (byte) 0xBF) {
            // If this is a UTF-8 BOM, skip it
            return pushbackInputStream;
        }
        // No BOM found, reset and return the original input stream
        pushbackInputStream.unread(bomBytes);
        return pushbackInputStream;
    }

    private void checkingHeaders(String[] row) {
            if (!"Date".equals(row[0]) || !"Day".equals(row[1]) || !"Description".equals(row[2])) {
                LOGGER.error("Invalid file headers");
                throw new BadRequestException(messageSource.getMessage("INVALID_HEADER_NAME", null, Locale.ENGLISH));
            }
    }

}
