package com.junhyeong.chatchat.dtos;

public class MessageRequestDto {
    private Long chatRoomId;

    private Long senderId;

    private String content;

    private String role;

    public MessageRequestDto(Long chatRoomId, Long senderId, String content, String role) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.role = role;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public String getRole() {
        return role;
    }
}
