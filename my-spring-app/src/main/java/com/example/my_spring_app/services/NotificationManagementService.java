package com.example.my_spring_app.services;

import com.example.my_spring_app.models.Notification;
import com.example.my_spring_app.models.NotificationType;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class NotificationManagementService {

    public List<Notification> getNotifications(String email) {
        return Collections.emptyList();
    }

    public int getUnreadCount(String email) {
        return 0;
    }

    public Notification markRead(String email, Long notificationId) {
        throw new UnsupportedOperationException("Notification management is not available with text-file persistence yet.");
    }

    public Notification createNotification(String email, NotificationType type, String title, String message, String relatedUrl) {
        throw new UnsupportedOperationException("Notification management is not available with text-file persistence yet.");
    }
}
