package edu.icet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.NotificationDto;
import edu.icet.entity.Notification;
import edu.icet.repository.NotificationRepository;
import edu.icet.repository.UserRepository;
import edu.icet.service.EmailService;
import edu.icet.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ObjectMapper mapper;

    @Override
    public NotificationDto sendNotification(NotificationDto dto) {
        Notification notification = mapper.convertValue(dto, Notification.class);
        if (notification.getSentAt() == null) {
            notification.setSentAt(LocalDateTime.now());
        }
        Notification saved = notificationRepository.save(notification);

        // Attempt to send email
        if (dto.getUserId() != null) {
            userRepository.findById(dto.getUserId()).ifPresent(user -> {
                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    emailService.sendSimpleEmail(
                            user.getEmail(),
                            "New Notification - Wellness Point",
                            dto.getMessage()
                    );
                }
            });
        }

        return mapper.convertValue(saved, NotificationDto.class);
    }

    @Override
    public List<NotificationDto> getAllNotifications() {
        List<Notification> all = notificationRepository.findAll();
        List<NotificationDto> dtos = new ArrayList<>();
        all.forEach(n -> dtos.add(mapper.convertValue(n, NotificationDto.class)));
        return dtos;
    }

    @Override
    public List<NotificationDto> getNotificationsByUserId(Long userId) {
        List<Notification> byUser = notificationRepository.findByUserId(userId);
        List<NotificationDto> dtos = new ArrayList<>();
        byUser.forEach(n -> dtos.add(mapper.convertValue(n, NotificationDto.class)));
        return dtos;
    }

    @Override
    public NotificationDto getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .map(n -> mapper.convertValue(n, NotificationDto.class))
                .orElse(null);
    }

    @Override
    public void deleteNotification(Long id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
        }
    }

    // NEW IMPLEMENTATION
    @Override
    public NotificationDto updateNotification(Long id, NotificationDto dto) {
        Optional<Notification> existingOpt = notificationRepository.findById(id);

        if (existingOpt.isPresent()) {
            Notification existing = existingOpt.get();

            // Update fields (ensure don't overwrite with nulls if that's not intended)
            existing.setMessage(dto.getMessage());

            // Optional update timestamp or other fields
            if (dto.getSentAt() != null) {
                existing.setSentAt(dto.getSentAt());
            }
            if (dto.getUserId() != null) {
                existing.setUserId(dto.getUserId());
            }

            Notification saved = notificationRepository.save(existing);
            return mapper.convertValue(saved, NotificationDto.class);
        }
        return null; // Or throw exception
    }
}
