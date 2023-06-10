package com.junhyeong.chatchat.models.message;

import com.junhyeong.chatchat.dtos.MessageDto;
import com.junhyeong.chatchat.models.commom.Username;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    private Long chatroomId;

    private Username sender;

    private Content content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Message() {
    }

    public Message(Long id, Long chatroomId, Username sender,
                   Content content, MessageType type, ReadStatus readStatus) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.readStatus = readStatus;
    }

    public Message(Long chatroomId, Username sender, Content content, MessageType type) {
        this.chatroomId = chatroomId;
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.readStatus = readStatus.UNREAD;
    }

    public static Message fake(Username username, Content content) {
        return new Message(
                1L, 1L,
                username,
                content,
                MessageType.GENERAL,
                ReadStatus.UNREAD
        );
    }

    public Long id() {
        return id;
    }

    public Long chatroomId() {
        return chatroomId;
    }

    public Username sender() {
        return sender;
    }

    public Content content() {
        return content;
    }

    public ReadStatus readStatus() {
        return readStatus;
    }

    public MessageType type() {
        return type;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public void read() {
        this.readStatus = ReadStatus.READ;
    }

    public MessageDto toDto(Long userId) {
        return new MessageDto(
                this.id,
                userId,
                this.sender.value(),
                this.content.value(),
                this.createdAt
        );
    }
}
