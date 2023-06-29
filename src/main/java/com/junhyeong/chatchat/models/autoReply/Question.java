package com.junhyeong.chatchat.models.autoReply;

import com.junhyeong.chatchat.exceptions.InvalidQuestion;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Question {
    @Column(name = "question")
    private String value;

    public Question() {
    }

    public Question(String value) {
        setQuestion(value);
    }

    private void setQuestion(String value) {
        if (value.length() > 60) {
            throw new InvalidQuestion();
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

        Question otherQuestion = (Question) object;

        return Objects.equals(value, otherQuestion.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
