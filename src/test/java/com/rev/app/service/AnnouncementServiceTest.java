package com.rev.app.service;

import com.rev.app.dto.AnnouncementDto;
import com.rev.app.entity.Announcement;
import com.rev.app.entity.Employee;
import com.rev.app.exceptions.ResourceNotFoundException;
import com.rev.app.mapper.DTOMapper;
import com.rev.app.repository.AnnouncementRepository;
import com.rev.app.repository.EmployeeRepository;
import com.rev.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnouncementServiceTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AnnouncementServiceImpl announcementService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(announcementService, "dtoMapper", new DTOMapper());
    }

    @Test
    void testCreateAnnouncement_ByEmpId_SavesAnnouncement() {
        AnnouncementDto dto = new AnnouncementDto();
        dto.setTitle("Company Picnic");
        dto.setMessage("Annual picnic on Friday");
        dto.setCreatedBy("EMP001"); // empId, not email

        Employee emp = new Employee();
        emp.setEmpId("EMP001");

        Announcement saved = new Announcement();
        saved.setTitle("Company Picnic");
        saved.setMessage("Annual picnic on Friday");
        saved.setCreatedBy(emp);

        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(emp));
        when(announcementRepository.save(any(Announcement.class))).thenReturn(saved);

        AnnouncementDto result = announcementService.createAnnouncement(dto);

        assertNotNull(result);
        verify(announcementRepository, times(1)).save(any(Announcement.class));
    }

    @Test
    void testCreateAnnouncement_NoCreatedBy_SavesWithNullEmployee() {
        AnnouncementDto dto = new AnnouncementDto();
        dto.setTitle("General Notice");
        dto.setMessage("System maintenance tonight");
        dto.setCreatedBy(null);

        Announcement saved = new Announcement();
        saved.setTitle("General Notice");

        when(announcementRepository.save(any(Announcement.class))).thenReturn(saved);

        AnnouncementDto result = announcementService.createAnnouncement(dto);

        assertNotNull(result);
        verifyNoInteractions(employeeRepository, userRepository);
    }

    @Test
    void testDeleteAnnouncement_CallsDeleteById() {
        doNothing().when(announcementRepository).deleteById(10L);

        announcementService.deleteAnnouncement(10L);

        verify(announcementRepository, times(1)).deleteById(10L);
    }

    @Test
    void testUpdateAnnouncement_NotFound_ThrowsException() {
        AnnouncementDto dto = new AnnouncementDto();
        dto.setAnnouncementId(999L);
        dto.setTitle("Updated Title");

        when(announcementRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> announcementService.updateAnnouncement(dto));
    }
}
