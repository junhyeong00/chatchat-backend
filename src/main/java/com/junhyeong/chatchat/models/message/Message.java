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
import java.util.Objects;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    private Long chatRoomId;

    private Sender sender;

    private Content content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Message() {
    }

    public Message(Long id, Long chatroomId, Sender sender,
                   Content content, MessageType type, ReadStatus readStatus) {
        this.id = id;
        this.chatRoomId = chatroomId;
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.readStatus = readStatus;
        this.createdAt = LocalDateTime.now();
    }

    public Message(Long chatroomId, Sender sender, Content content, MessageType type) {
        this.chatRoomId = chatroomId;
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.readStatus = readStatus.UNREAD;
        this.createdAt = LocalDateTime.now();
    }

    public static Message fake(Username username, Content content) {
        return new Message(
                1L, 1L,
                new Sender(1L, username),
                content,
                MessageType.GENERAL,
                ReadStatus.UNREAD
        );
    }

    public Long id() {
        return id;
    }

    public Long chatRoomId() {
        return chatRoomId;
    }

    public Sender sender() {
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

    public MessageDto toDto() {
        return new MessageDto(
                this.id,
                this.sender.id(),
                this.content.value(),
                this.createdAt
        );
    }

    public boolean isSender(Username username) {
        return Objects.equals(this.sender.username(), username);
    }

    public boolean isRead() {
        return Objects.equals(this.readStatus, ReadStatus.READ);
    }
}
