package com.junhyeong.chatchat.models.message;

import com.junhyeong.chatchat.models.commom.Username;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Sender {
    @Column(name = "senderId")
    private Long id;

    @Column(name = "senderUsername")
    private Username username;

    public Sender() {
    }

    public Sender(Long id, Username username) {
        this.id = id;
        this.username = username;
    }

    public Long id() {
        return id;
    }

    public Username username() {
        return username;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Sender otherSender = (Sender) object;

        return Objects.equals(id, otherSender.id)
                && Objects.equals(username, otherSender.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
