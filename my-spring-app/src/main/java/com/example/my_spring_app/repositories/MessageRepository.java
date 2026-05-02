package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySender_IdOrRecipient_Id(Long senderId, Long recipientId);
    List<Message> findByBooking_Id(Long bookingId);
    List<Message> findByRecipient_IdAndIsReadFalse(Long recipientId);
    List<Message> findBySender_IdAndRecipient_Id(Long senderId, Long recipientId);
}
