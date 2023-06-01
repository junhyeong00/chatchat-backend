package com.junhyeong.chatchat.models.company;

import com.junhyeong.chatchat.exceptions.InvalidDescription;
import com.junhyeong.chatchat.exceptions.InvalidName;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Description {
    @Column(name = "description")
    private String value;

    public Description() {
    }

    public Description(String value) {
        setDescription(value);
    }

    private void setDescription(String value) {
        if (value.length() > 200) {
            throw new InvalidDescription();
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

        Description otherDescription = (Description) object;

        return Objects.equals(value, otherDescription.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
