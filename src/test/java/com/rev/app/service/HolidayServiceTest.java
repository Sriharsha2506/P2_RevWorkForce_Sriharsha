package com.rev.app.service;

import com.rev.app.entity.HolidayCalendar;
import com.rev.app.repository.HolidayCalendarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private HolidayCalendarRepository holidayCalendarRepository;

    @InjectMocks
    private HolidayServiceImpl holidayService;

    @Test
    void testGetAllHolidays_ReturnsList() {
        HolidayCalendar h1 = new HolidayCalendar();
        h1.setDescription("Christmas");
        h1.setHolidayDate(LocalDate.of(2025, 12, 25));

        HolidayCalendar h2 = new HolidayCalendar();
        h2.setDescription("Independence Day");
        h2.setHolidayDate(LocalDate.of(2025, 8, 15));

        when(holidayCalendarRepository.findAll()).thenReturn(Arrays.asList(h1, h2));

        List<HolidayCalendar> result = holidayService.getAllHolidays();

        assertEquals(2, result.size());
        assertEquals("Christmas", result.get(0).getDescription());
    }

    @Test
    void testSaveHoliday_PersistsHoliday() {
        HolidayCalendar holiday = new HolidayCalendar();
        holiday.setDescription("Diwali");
        holiday.setHolidayDate(LocalDate.of(2025, 10, 20));

        when(holidayCalendarRepository.save(any(HolidayCalendar.class))).thenReturn(holiday);

        HolidayCalendar result = holidayService.saveHoliday(holiday);

        assertNotNull(result);
        assertEquals("Diwali", result.getDescription());
        verify(holidayCalendarRepository, times(1)).save(holiday);
    }

    @Test
    void testDeleteHoliday_CallsDeleteById() {
        doNothing().when(holidayCalendarRepository).deleteById(1L);

        holidayService.deleteHoliday(1L);

        verify(holidayCalendarRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetHolidayById_ReturnsHoliday() {
        HolidayCalendar holiday = new HolidayCalendar();
        holiday.setDescription("Holi");

        when(holidayCalendarRepository.findById(5L)).thenReturn(Optional.of(holiday));

        Optional<HolidayCalendar> result = holidayService.getHolidayById(5L);

        assertTrue(result.isPresent());
        assertEquals("Holi", result.get().getDescription());
    }
}
