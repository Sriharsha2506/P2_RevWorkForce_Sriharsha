package com.rev.app.service;

import com.rev.app.entity.Announcement;
import com.rev.app.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository repository;

    public AnnouncementServiceImpl(AnnouncementRepository repository) {
        this.repository = repository;
    }

    @Override
    public Announcement createAnnouncement(Announcement announcement) {
        announcement.setCreatedDate(LocalDate.now());
        return repository.save(announcement);
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        return repository.findAllByOrderByCreatedDateDesc();
    }
}