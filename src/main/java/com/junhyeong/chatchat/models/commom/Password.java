package com.junhyeong.chatchat.models.commom;

import com.junhyeong.chatchat.exceptions.InvalidPassword;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Password {
    @Column(name = "password")
    private String value;

    public Password() {
    }

    public Password(String value) {
        setValue(value);
    }

    public Password(String value, String encode) {
        this.value = value;
    }

    private void setValue(String value) {
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d~!@#$%^&*()-_=+]{8,40}$");
        Matcher matcher = pattern.matcher(value);

        if (!matcher.find()) {
            throw new InvalidPassword();
        }

        this.value = value;
    }

    public static Password of(String password, String encode) {
        return new Password(password, encode);
    }

    public String getValue() {
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

        Password otherPassword = (Password) object;

        return Objects.equals(value, otherPassword.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
