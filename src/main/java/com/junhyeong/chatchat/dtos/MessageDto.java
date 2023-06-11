package com.junhyeong.chatchat.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageDto {
    private Long id;
    private Long senderId;
    private String content;
    private String createdAt;

    public MessageDto(Long id, Long senderId,  String content, LocalDateTime createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
