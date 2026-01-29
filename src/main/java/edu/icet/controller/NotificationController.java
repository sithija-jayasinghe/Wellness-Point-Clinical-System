package edu.icet.controller;

import edu.icet.dto.NotificationDto;
import edu.icet.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/send")
    public NotificationDto sendNotification(@RequestBody NotificationDto dto) {
        return service.sendNotification(dto);
    }

    @GetMapping("/get-all")
    public List<NotificationDto> getAllNotifications() {
        return service.getAllNotifications();
    }

    @GetMapping("/user/{userId}")
    public List<NotificationDto> getNotificationsByUserId(@PathVariable Long userId) {
        return service.getNotificationsByUserId(userId);
    }

    @GetMapping("/{id}")
    public NotificationDto getNotificationById(@PathVariable Long id) {
        return service.getNotificationById(id); // Use getNotificationById
    }

    // NEW UPDATE METHOD
    @PutMapping("/update/{id}")
    public NotificationDto updateNotification(@PathVariable Long id, @RequestBody NotificationDto dto) {
        return service.updateNotification(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteNotification(@PathVariable Long id) {
        service.deleteNotification(id);
    }
}
