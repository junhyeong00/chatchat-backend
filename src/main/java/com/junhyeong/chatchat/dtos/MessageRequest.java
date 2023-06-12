package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.message.Content;

public class MessageRequest {
    private Long chatRoomId;

    private Long senderId;

    private Content content;

    private String role;

    public MessageRequest( Long chatRoomId, Long senderId, Content content, String role) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.role = role;
    }

    public static MessageRequest of(MessageRequestDto messageRequestDto) {
        return new MessageRequest(
                messageRequestDto.getChatRoomId(),
                messageRequestDto.getSenderId(),
                new Content(messageRequestDto.getContent()),
                messageRequestDto.getRole());
    }

    public static MessageRequest fake(Content content, String role) {
        return new MessageRequest(
                1L,
                1L,
                content,
                role
        );
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Content getContent() {
        return content;
    }

    public String getRole() {
        return role;
    }
}
