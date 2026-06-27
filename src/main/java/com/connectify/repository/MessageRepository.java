package com.connectify.repository;

import com.connectify.model.Message;
import com.connectify.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomOrderByCreatedAtAsc(ChatRoom room);
    List<Message> findByRoomAndReadFalse(ChatRoom room);
}