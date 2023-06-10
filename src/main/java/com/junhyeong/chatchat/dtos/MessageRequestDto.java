package com.junhyeong.chatchat.dtos;

public class MessageRequestDto {
    private String type;

    private Long chatRoomId;

    private Long userId;

    private String content;

    private String role;

    public MessageRequestDto(String type, Long chatRoomId, Long userId, String content, String role) {
        this.type = type;
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.content = content;
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getRole() {
        return role;
    }
}
