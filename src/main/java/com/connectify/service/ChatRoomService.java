package com.connectify.service;

import com.connectify.model.ChatRoom;
import com.connectify.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public ChatRoom createRoom(String name, String description) {
        ChatRoom room = new ChatRoom();
        room.setName(name);
        room.setDescription(description);
        return chatRoomRepository.save(room);
    }

    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    public Optional<ChatRoom> findByName(String name) {
        return chatRoomRepository.findByName(name);
    }

    public Optional<ChatRoom> findById(Long id) {
        return chatRoomRepository.findById(id);
    }

    public boolean existsByName(String name) {
        return chatRoomRepository.existsByName(name);
    }

    public ChatRoom save(ChatRoom room) {
        return chatRoomRepository.save(room);
    }
}