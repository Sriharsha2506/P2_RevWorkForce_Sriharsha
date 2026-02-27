package com.rev.app.service;

import com.rev.app.entity.Announcement;
import java.util.List;

public interface AnnouncementService {

    Announcement createAnnouncement(Announcement announcement);

    List<Announcement> getAllAnnouncements();
}