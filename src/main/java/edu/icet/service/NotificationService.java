package edu.icet.service;

import edu.icet.dto.NotificationDto;
import java.util.List;

public interface NotificationService {
    NotificationDto sendNotification(NotificationDto dto);
    List<NotificationDto> getAllNotifications();
    List<NotificationDto> getNotificationsByUserId(Long userId);
    NotificationDto getNotificationById(Long id);
    void deleteNotification(Long id);

    // NEW METHOD
    NotificationDto updateNotification(Long id, NotificationDto dto);
}
