package com.connectify.controller;

import com.connectify.model.ChatRoom;
import com.connectify.model.Message;
import com.connectify.model.User;
import com.connectify.service.ChatRoomService;
import com.connectify.service.MessageService;
import com.connectify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ChatViewController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        List<ChatRoom> rooms = chatRoomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        model.addAttribute("username", principal.getName());
        return "home";
    }

    @GetMapping("/chat/{roomName}")
    public String chat(@PathVariable String roomName, Model model, Principal principal) {
        Optional<ChatRoom> roomOpt = chatRoomService.findByName(roomName);
        if (roomOpt.isEmpty()) return "redirect:/";

        ChatRoom room = roomOpt.get();
        List<Message> messages = messageService.getRoomMessages(room);
        List<User> onlineUsers = userService.getOnlineUsers();
        User currentUser = userService.findByUsername(principal.getName()).orElse(null);

        model.addAttribute("room", room);
        model.addAttribute("messages", messages);
        model.addAttribute("onlineUsers", onlineUsers);
        model.addAttribute("currentUser", currentUser);
        return "chat";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
public String registerPost(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String displayName,
                           Model model) {
    if (userService.existsByUsername(username)) {
        model.addAttribute("error", "Username already taken!");
        return "register";
    }
    userService.register(username, password, displayName);
    return "redirect:/login?registered";
}

    @PostMapping("/room/create")
    public String createRoom(@RequestParam String name,
                             @RequestParam String description) {
        if (!chatRoomService.existsByName(name)) {
            chatRoomService.createRoom(name, description);
        }
        return "redirect:/chat/" + name;
    }
}