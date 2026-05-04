package com.example.my_spring_app.services;

import com.example.my_spring_app.User;
import com.example.my_spring_app.models.Booking;
import com.example.my_spring_app.models.Message;
import com.example.my_spring_app.repositories.BookingRepository;
import com.example.my_spring_app.repositories.MessageRepository;
import com.example.my_spring_app.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageManagementService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public MessageManagementService(MessageRepository messageRepository,
                                    UserRepository userRepository,
                                    BookingRepository bookingRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Message> getConversation(String userEmail, String otherEmail) {
        User user = resolveUser(userEmail);
        User other = resolveUser(otherEmail);
        return messageRepository.findBySender_IdAndRecipient_Id(user.getId(), other.getId());
    }

    public List<Message> getInbox(String email) {
        User user = resolveUser(email);
        return messageRepository.findBySender_IdOrRecipient_Id(user.getId(), user.getId());
    }

    public List<Message> getUnread(String email) {
        User user = resolveUser(email);
        return messageRepository.findByRecipient_IdAndIsReadFalse(user.getId());
    }

    public Message sendMessage(String senderEmail, String recipientEmail, Long bookingId,
                               String content, String attachmentUrl) {
        User sender = resolveUser(senderEmail);
        User recipient = resolveUser(recipientEmail);
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setMessageContent(content);
        message.setAttachmentUrl(attachmentUrl);
        message.setIsRead(false);
        message.setCreatedAt(LocalDateTime.now());
        if (bookingId != null) {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            message.setBooking(booking);
        }
        return messageRepository.save(message);
    }

    public Message markRead(String email, Long messageId) {
        User user = resolveUser(email);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        if (!message.getRecipient().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot modify this message");
        }
        message.setIsRead(true);
        message.setReadAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    private User resolveUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
