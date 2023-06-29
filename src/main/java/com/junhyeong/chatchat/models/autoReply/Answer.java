package com.junhyeong.chatchat.models.autoReply;

import com.junhyeong.chatchat.exceptions.InvalidAnswer;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Answer {
    @Column(name = "answer")
    private String value;

    public Answer() {
    }

    public Answer(String value) {
        setAnswer(value);
    }

    private void setAnswer(String value) {
        if (value.length() > 200) {
            throw new InvalidAnswer();
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

        Answer otherAnswer = (Answer) object;

        return Objects.equals(value, otherAnswer.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
