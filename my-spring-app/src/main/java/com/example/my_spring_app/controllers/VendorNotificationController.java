package com.example.my_spring_app.controllers;

import com.example.my_spring_app.models.Notification;
import com.example.my_spring_app.services.NotificationManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class VendorNotificationController {

    private final NotificationManagementService notificationManagementService;

    public VendorNotificationController(NotificationManagementService notificationManagementService) {
        this.notificationManagementService = notificationManagementService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> notifications(@RequestParam String email) {
        return ResponseEntity.ok(notificationManagementService.getNotifications(email));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Integer>> unreadCount(@RequestParam String email) {
        Map<String, Integer> response = new HashMap<>();
        response.put("count", notificationManagementService.getUnreadCount(email));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<?> markRead(@RequestParam String email, @PathVariable Long notificationId) {
        try {
            return ResponseEntity.ok(notificationManagementService.markRead(email, notificationId));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", message);
        return body;
    }
}
