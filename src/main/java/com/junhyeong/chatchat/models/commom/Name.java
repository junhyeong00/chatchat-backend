package com.junhyeong.chatchat.models.commom;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
    @Column(name = "name")
    private String value;

    public Name() {
    }

    public Name(String value) {
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

        Name otherName = (Name) object;

        return Objects.equals(value, otherName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
