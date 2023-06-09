package com.junhyeong.chatchat.models.message;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Content {
    @Column(name = "content")
    private String value;

    public Content() {
    }

    public Content(String value) {
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

        Content otherContent = (Content) object;

        return Objects.equals(value, otherContent.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
