package com.rev.app.repository;

import com.rev.app.entity.Announcement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnnouncementRepositoryTest {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Test
    void testSaveAndFindAllByOrderByCreatedAtDesc_ReturnsPaged() {
        Announcement a1 = new Announcement();
        a1.setTitle("Announcement 1");
        a1.setMessage("First message");
        announcementRepository.save(a1);

        Announcement a2 = new Announcement();
        a2.setTitle("Announcement 2");
        a2.setMessage("Second message");
        announcementRepository.save(a2);

        Page<Announcement> page = announcementRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 10));

        assertTrue(page.getTotalElements() >= 2);
    }

    @Test
    void testDeleteAnnouncement_RemovesFromDB() {
        Announcement a = new Announcement();
        a.setTitle("To Delete");
        a.setMessage("Temporary");
        Announcement saved = announcementRepository.save(a);

        announcementRepository.deleteById(saved.getAnnouncementId());

        assertFalse(announcementRepository.findById(saved.getAnnouncementId()).isPresent());
    }
}
