package com.connectify.model;

public class ChatMessage {
    private String sender;
    private String receiver;
    private String content;
    private String room;
    private String fileUrl;
    private MessageType type;
    private String timestamp;
    private String avatarColor;

    public enum MessageType {
        CHAT, JOIN, LEAVE, TYPING, STOP_TYPING, READ, PRIVATE
    }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getAvatarColor() { return avatarColor; }
    public void setAvatarColor(String avatarColor) { this.avatarColor = avatarColor; }
}