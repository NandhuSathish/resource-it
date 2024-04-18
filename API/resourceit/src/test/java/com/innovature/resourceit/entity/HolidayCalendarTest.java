package com.innovature.resourceit.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = HolidayCalendarTest.class)
 class HolidayCalendarTest {

    @Test
    void testNoArgsConstructor() {
        HolidayCalendar holidayCalender = new HolidayCalendar();
        assertEquals(0, holidayCalender.getCalenderId());
        assertNull(holidayCalender.getDate());
        assertNull(holidayCalender.getName());
        assertNull(holidayCalender.getDay());
        assertEquals(0, holidayCalender.getYear());
        assertNull(holidayCalender.getCreatedDate());
        assertNull(holidayCalender.getUpdatedDate());
    }

    @Test
    void testAllArgsConstructor() {
        Date currentDate = new Date();
        HolidayCalendar holidayCalender = new HolidayCalendar(1, currentDate, "New Year's Day", "Monday", 2023, currentDate, currentDate);
        assertEquals(1, holidayCalender.getCalenderId());
        assertEquals(currentDate, holidayCalender.getDate());
        assertEquals("New Year's Day", holidayCalender.getName());
        assertEquals("Monday", holidayCalender.getDay());
        assertEquals(2023, holidayCalender.getYear());
        assertEquals(currentDate, holidayCalender.getCreatedDate());
        assertEquals(currentDate, holidayCalender.getUpdatedDate());
    }

    @Test
    void testSetterAndGetterForCalenderId() {
        HolidayCalendar holidayCalender = new HolidayCalendar();
        holidayCalender.setCalenderId(1);
        assertEquals(1, holidayCalender.getCalenderId());
    }

    @Test
    void testSetterAndGetterForDate() {
        Date currentDate = new Date();
        HolidayCalendar holidayCalender = new HolidayCalendar();
        holidayCalender.setDate(currentDate);
        assertEquals(currentDate, holidayCalender.getDate());
    }

    @Test
    void testSetterAndGetterForName() {
        HolidayCalendar holidayCalender = new HolidayCalendar();
        holidayCalender.setName("Independence Day");
        assertEquals("Independence Day", holidayCalender.getName());
    }

    @Test
    void testSetterAndGetterForDay() {
        HolidayCalendar holidayCalender = new HolidayCalendar();
        holidayCalender.setDay("Friday");
        assertEquals("Friday", holidayCalender.getDay());
    }

    @Test
    void testSetterAndGetterForYear() {
        HolidayCalendar holidayCalender = new HolidayCalendar();
        holidayCalender.setYear(2023);
        assertEquals(2023, holidayCalender.getYear());
    }

    @Test
    void testSetterAndGetterForCreatedDate() {
        Date currentDate = new Date();
        HolidayCalendar holidayCalender = new HolidayCalendar();
        holidayCalender.setCreatedDate(currentDate);
        assertEquals(currentDate, holidayCalender.getCreatedDate());
    }

    @Test
    void testSetterAndGetterForUpdatedDate() {
        Date currentDate = new Date();
        HolidayCalendar holidayCalender = new HolidayCalendar();
        holidayCalender.setUpdatedDate(currentDate);
        assertEquals(currentDate, holidayCalender.getUpdatedDate());
    }
}
