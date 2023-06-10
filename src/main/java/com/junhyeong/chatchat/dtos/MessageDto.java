package com.junhyeong.chatchat.dtos;

import java.time.LocalDateTime;

public class MessageDto {
    private Long id;
    private Long userId;
    private String sender;
    private String content;
    private LocalDateTime createdAt;

    public MessageDto(Long id, Long userId, String sender, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.sender = sender;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
