package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.message.Content;
import com.junhyeong.chatchat.models.message.MessageType;

public class MessageRequest {
    private MessageType type;

    private Long chatRoomId;

    private Long userId;

    private Content content;

    private String role;

    public MessageRequest(MessageType type, Long chatRoomId, Long userId, Content content, String role) {
        this.type = type;
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.content = content;
        this.role = role;
    }

    public static MessageRequest of(MessageRequestDto messageRequestDto) {
        return new MessageRequest(
                MessageType.valueOf(messageRequestDto.getType()),
                messageRequestDto.getChatRoomId(),
                messageRequestDto.getUserId(),
                new Content(messageRequestDto.getContent()),
                messageRequestDto.getRole());
    }

    public static MessageRequest fake(Content content, String role) {
        return new MessageRequest(
                MessageType.GENERAL,
                1L,
                1L,
                content,
                role
        );
    }

    public MessageType getType() {
        return type;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public Long getUserId() {
        return userId;
    }

    public Content getContent() {
        return content;
    }

    public String getRole() {
        return role;
    }
}
