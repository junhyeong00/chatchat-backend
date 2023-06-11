package com.junhyeong.chatchat.models.chatRoom;

import com.junhyeong.chatchat.models.commom.Username;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class ChatRoom {
    @Id
    @GeneratedValue
    private Long id;

    @AttributeOverride(name = "value", column = @Column(name = "customer_username"))
    private Username customer;

    @AttributeOverride(name = "value", column = @Column(name = "company_username"))
    private Username company;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public ChatRoom() {
    }

    public ChatRoom(Username customer, Username company) {
        this.customer = customer;
        this.company = company;
    }

    public ChatRoom(Long id, Username customer, Username company) {
        this.id = id;
        this.customer = customer;
        this.company = company;
    }

    public static ChatRoom fake(Long chatRoomId) {
        return new ChatRoom(
                chatRoomId,
                new Username("customer"),
                new Username("company")
        );
    }

    public Long id() {
        return id;
    }

    public Username customer() {
        return customer;
    }

    public Username company() {
        return company;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }
}
