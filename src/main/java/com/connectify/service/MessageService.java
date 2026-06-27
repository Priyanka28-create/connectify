package com.connectify.service;

import com.connectify.model.ChatRoom;
import com.connectify.model.Message;
import com.connectify.model.User;
import com.connectify.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(User sender, ChatRoom room, 
                                String content, String messageType) {
        Message message = new Message();
        message.setSender(sender);
        message.setRoom(room);
        message.setContent(content);
        message.setMessageType(messageType);
        return messageRepository.save(message);
    }

    public List<Message> getRoomMessages(ChatRoom room) {
        return messageRepository.findByRoomOrderByCreatedAtAsc(room);
    }

    public void markAsRead(ChatRoom room) {
        List<Message> unread = messageRepository.findByRoomAndReadFalse(room);
        unread.forEach(m -> m.setRead(true));
        messageRepository.saveAll(unread);
    }
}