package com.example.my_spring_app.services;

import com.example.my_spring_app.models.Message;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MessageManagementService {

    public List<Message> getConversation(String userEmail, String otherEmail) {
        return Collections.emptyList();
    }

    public List<Message> getInbox(String email) {
        return Collections.emptyList();
    }

    public List<Message> getUnread(String email) {
        return Collections.emptyList();
    }

    public Message sendMessage(String senderEmail, String recipientEmail, Long bookingId,
                               String content, String attachmentUrl) {
        throw new UnsupportedOperationException("Messaging is not available with text-file persistence yet.");
    }

    public Message markRead(String email, Long messageId) {
        throw new UnsupportedOperationException("Messaging is not available with text-file persistence yet.");
    }
}
