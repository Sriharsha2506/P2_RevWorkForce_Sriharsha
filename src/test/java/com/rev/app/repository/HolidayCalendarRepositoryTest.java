package com.rev.app.repository;

import com.rev.app.entity.HolidayCalendar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HolidayCalendarRepositoryTest {

    @Autowired
    private HolidayCalendarRepository holidayCalendarRepository;

    @Test
    void testSaveAndFindAll_ReturnsHolidays() {
        HolidayCalendar h1 = new HolidayCalendar();
        h1.setDescription("Christmas");
        h1.setHolidayDate(LocalDate.of(2025, 12, 25));
        holidayCalendarRepository.save(h1);

        HolidayCalendar h2 = new HolidayCalendar();
        h2.setDescription("New Year");
        h2.setHolidayDate(LocalDate.of(2026, 1, 1));
        holidayCalendarRepository.save(h2);

        List<HolidayCalendar> all = holidayCalendarRepository.findAll();
        assertTrue(all.size() >= 2);
    }

    @Test
    void testFindById_ExistingHoliday_Returns() {
        HolidayCalendar holiday = new HolidayCalendar();
        holiday.setDescription("Republic Day");
        holiday.setHolidayDate(LocalDate.of(2025, 1, 26));
        HolidayCalendar saved = holidayCalendarRepository.save(holiday);

        Optional<HolidayCalendar> found = holidayCalendarRepository.findById(saved.getHolidayId());
        assertTrue(found.isPresent());
        assertEquals("Republic Day", found.get().getDescription());
    }

    @Test
    void testDelete_RemovesHoliday() {
        HolidayCalendar holiday = new HolidayCalendar();
        holiday.setDescription("Temp Holiday");
        holiday.setHolidayDate(LocalDate.of(2025, 5, 5));
        HolidayCalendar saved = holidayCalendarRepository.save(holiday);

        holidayCalendarRepository.deleteById(saved.getHolidayId());

        assertFalse(holidayCalendarRepository.findById(saved.getHolidayId()).isPresent());
    }
}
