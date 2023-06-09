package com.junhyeong.chatchat.models.message;

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

    private Username writer;

    private Content content;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Message() {
    }

    public Message(Long chatroomId, Username writer, Content content) {
        this.chatroomId = chatroomId;
        this.writer = writer;
        this.content = content;
        this.readStatus = readStatus.UNREAD;
    }

    public Long id() {
        return id;
    }

    public Long chatroomId() {
        return chatroomId;
    }

    public Username writer() {
        return writer;
    }

    public Content content() {
        return content;
    }

    public ReadStatus readStatus() {
        return readStatus;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public void setRead() {
        this.readStatus = ReadStatus.READ;
    }
}
