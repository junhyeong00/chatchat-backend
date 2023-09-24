package com.junhyeong.chatchat.models.commom;

import com.junhyeong.chatchat.exceptions.InvalidName;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
    public static final String UNKNOWN_NAME = "(알 수 없음)";

    @Column(name = "name")
    private String value;

    public Name() {
    }

    public Name(String value) {
        setName(value);
    }

    private void setName(String value) {
        if (value.length() > 20) {
            throw new InvalidName();
        }

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
