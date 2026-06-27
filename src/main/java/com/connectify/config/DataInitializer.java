package com.connectify.config;

import com.connectify.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ChatRoomService chatRoomService;

    @Override
    public void run(String... args) {
        if (!chatRoomService.existsByName("General")) {
            chatRoomService.createRoom("General", "General chat for everyone");
        }
        if (!chatRoomService.existsByName("Tech Talk")) {
            chatRoomService.createRoom("Tech Talk", "Discuss all things tech");
        }
        if (!chatRoomService.existsByName("Random")) {
            chatRoomService.createRoom("Random", "Random conversations");
        }
    }
}