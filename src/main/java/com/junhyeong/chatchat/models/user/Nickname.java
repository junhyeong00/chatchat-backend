package com.junhyeong.chatchat.models.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Nickname {
    @Column(name = "nickname")
    private String value;

    public Nickname() {
    }


    public Nickname(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Nickname otherNickname = (Nickname) object;

        return Objects.equals(value, otherNickname.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

