package com.example.my_spring_app.controllers;

import com.example.my_spring_app.models.Message;
import com.example.my_spring_app.services.MessageManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class VendorMessageController {

    private final MessageManagementService messageManagementService;

    public VendorMessageController(MessageManagementService messageManagementService) {
        this.messageManagementService = messageManagementService;
    }

    @GetMapping
    public ResponseEntity<List<Message>> inbox(@RequestParam String email) {
        return ResponseEntity.ok(messageManagementService.getInbox(email));
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> conversation(@RequestParam String email, @RequestParam String otherEmail) {
        return ResponseEntity.ok(messageManagementService.getConversation(email, otherEmail));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Message>> unread(@RequestParam String email) {
        return ResponseEntity.ok(messageManagementService.getUnread(email));
    }

    @PostMapping
    public ResponseEntity<?> send(@RequestParam String senderEmail,
                                  @RequestParam String recipientEmail,
                                  @RequestParam(required = false) Long bookingId,
                                  @RequestParam String message,
                                  @RequestParam(required = false) String attachmentUrl) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(messageManagementService.sendMessage(senderEmail, recipientEmail, bookingId, message, attachmentUrl));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @PutMapping("/{messageId}/read")
    public ResponseEntity<?> markRead(@RequestParam String email, @PathVariable Long messageId) {
        try {
            return ResponseEntity.ok(messageManagementService.markRead(email, messageId));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", message);
        return body;
    }
}
