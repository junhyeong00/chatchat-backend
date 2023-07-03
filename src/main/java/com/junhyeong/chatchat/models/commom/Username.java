package com.junhyeong.chatchat.models.commom;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Username implements Serializable {
    @Column(name = "user_name")
    private String value;

    public Username() {
    }

    public Username(String value) {
        this.value = value;
    }

    public static Username of(String username) {
        return new Username(username);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return "Username(" + value + ")";
    }

    @Override
    public boolean equals(Object other) {
        return other != null &&
                other.getClass() == Username.class &&
                Objects.equals(this.value, ((Username) other).value);
    }
}
