/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.innovature.resourceit.repository;

import com.innovature.resourceit.entity.HolidayCalendar;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author abdul.fahad
 */
public interface HolidayCalendarRepository extends JpaRepository<HolidayCalendar, Integer> {
    Optional<List<HolidayCalendar>> findByYear(int year, Sort sort);

    void deleteAllByYear(int year);

    @Query("SELECT h1 FROM HolidayCalendar h1 WHERE h1.calenderId = (SELECT h2.calenderId FROM HolidayCalendar h2 WHERE h2.year = h1.year ORDER BY h2.updatedDate DESC, h2.calenderId DESC FETCH FIRST 1 ROW ONLY)")
    Optional<List<HolidayCalendar>> findDistinctByYear();

    void deleteByYear(Integer year);
}
