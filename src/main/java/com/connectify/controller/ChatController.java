package com.connectify.controller;

import com.connectify.model.ChatMessage;
import com.connectify.model.ChatRoom;
import com.connectify.model.User;
import com.connectify.service.ChatRoomService;
import com.connectify.service.MessageService;
import com.connectify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MessageService messageService;

    private static final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("hh:mm a");

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage, Principal principal) {
        String username = principal.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        Optional<ChatRoom> roomOpt = chatRoomService.findByName(chatMessage.getRoom());

        if (userOpt.isPresent() && roomOpt.isPresent()) {
            User user = userOpt.get();
            ChatRoom room = roomOpt.get();

            messageService.saveMessage(user, room, chatMessage.getContent(), "TEXT");

            chatMessage.setSender(user.getDisplayName());
            chatMessage.setAvatarColor(user.getAvatarColor());
            chatMessage.setTimestamp(LocalDateTime.now().format(formatter));
            chatMessage.setType(ChatMessage.MessageType.CHAT);

            messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoom(), chatMessage);
        }
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage,
                        SimpMessageHeaderAccessor headerAccessor,
                        Principal principal) {
        String username = principal.getName();
        headerAccessor.getSessionAttributes().put("username", username);
        headerAccessor.getSessionAttributes().put("room", chatMessage.getRoom());

        userService.setOnlineStatus(username, true);

        userService.findByUsername(username).ifPresent(user -> {
            chatMessage.setSender(user.getDisplayName());
            chatMessage.setAvatarColor(user.getAvatarColor());
            chatMessage.setType(ChatMessage.MessageType.JOIN);
            chatMessage.setTimestamp(LocalDateTime.now().format(formatter));
            messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoom(), chatMessage);
        });
    }

    @MessageMapping("/chat.typing")
    public void typing(@Payload ChatMessage chatMessage, Principal principal) {
        String username = principal.getName();
        userService.findByUsername(username).ifPresent(user -> {
            chatMessage.setSender(user.getDisplayName());
            messagingTemplate.convertAndSend(
                "/topic/" + chatMessage.getRoom() + ".typing", chatMessage);
        });
    }

    @MessageMapping("/chat.private")
    public void privateMessage(@Payload ChatMessage chatMessage, Principal principal) {
        String username = principal.getName();
        userService.findByUsername(username).ifPresent(user -> {
            chatMessage.setSender(user.getDisplayName());
            chatMessage.setAvatarColor(user.getAvatarColor());
            chatMessage.setTimestamp(LocalDateTime.now().format(formatter));
            chatMessage.setType(ChatMessage.MessageType.CHAT);

            // Send to receiver
            messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiver(),
                "/queue/private",
                chatMessage
            );

            // Send back to sender
            messagingTemplate.convertAndSendToUser(
                username,
                "/queue/private",
                chatMessage
            );
        });
    }
}