package com.example.my_spring_app.services;

import com.example.my_spring_app.User;
import com.example.my_spring_app.models.Notification;
import com.example.my_spring_app.models.NotificationType;
import com.example.my_spring_app.repositories.NotificationRepository;
import com.example.my_spring_app.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationManagementService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationManagementService(NotificationRepository notificationRepository,
                                         UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> getNotifications(String email) {
        User user = resolveUser(email);
        return notificationRepository.findByUser_Id(user.getId());
    }

    public int getUnreadCount(String email) {
        User user = resolveUser(email);
        return notificationRepository.countByUser_IdAndIsReadFalse(user.getId());
    }

    public Notification markRead(String email, Long notificationId) {
        User user = resolveUser(email);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot modify this notification");
        }
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public Notification createNotification(String email, NotificationType type, String title, String message, String relatedUrl) {
        User user = resolveUser(email);
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedUrl(relatedUrl);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    private User resolveUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
