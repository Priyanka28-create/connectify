package com.connectify.service;

import com.connectify.model.User;
import com.connectify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    private String[] avatarColors = {
        "#3478f6", "#7c3aed", "#ef4444", "#f59e0b",
        "#10b981", "#ec4899", "#06b6d4", "#f97316"
    };

    public User register(String username, String password, String displayName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setDisplayName(displayName);
        user.setAvatarColor(avatarColors[(int)(Math.random() * avatarColors.length)]);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<User> getOnlineUsers() {
        return userRepository.findByOnlineTrue();
    }

    public void setOnlineStatus(String username, boolean online) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setOnline(online);
            userRepository.save(user);
        });
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateProfile(String username, String displayName, 
                               String bio, String avatarColor) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDisplayName(displayName);
        user.setBio(bio);
        if (avatarColor != null && !avatarColor.isEmpty()) {
            user.setAvatarColor(avatarColor);
        }
        return userRepository.save(user);
    }
}