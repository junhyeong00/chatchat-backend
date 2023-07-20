package com.junhyeong.chatchat.models.autoReply;

import com.junhyeong.chatchat.dtos.AutoReplyDto;
import com.junhyeong.chatchat.dtos.AutoReplyQuestionDto;
import com.junhyeong.chatchat.exceptions.NotHaveDeleteAutoReplyAuthority;
import com.junhyeong.chatchat.exceptions.NotHaveEditAutoReplyAuthority;
import com.junhyeong.chatchat.models.commom.Status;
import com.junhyeong.chatchat.models.commom.Username;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class AutoReply {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Username companyUsername;

    @Embedded
    private Question question;

    @Embedded
    private Answer answer;

    @Enumerated(EnumType.STRING)
    private Status status;

    public AutoReply() {
    }

    public AutoReply(Long id, Username companyUsername, Question question, Answer answer) {
        this.id = id;
        this.companyUsername = companyUsername;
        this.question = question;
        this.answer = answer;
        this.status = Status.ACTIVE;
    }

    public AutoReply(Username companyUsername, Question question, Answer answer) {
        this.companyUsername = companyUsername;
        this.question = question;
        this.answer = answer;
        this.status = Status.ACTIVE;
    }

    public static AutoReply fake(Username username) {
        return new AutoReply(1L, username, new Question("질문"), new Answer("답변"));
    }

    public Long id() {
        return id;
    }

    public Username companyUsername() {
        return companyUsername;
    }

    public Question question() {
        return question;
    }

    public Answer answer() {
        return answer;
    }

    public AutoReplyDto toDto() {
        return new AutoReplyDto(id, question.value(), answer.value());
    }

    public AutoReplyQuestionDto toQuestionDto() {
        return new AutoReplyQuestionDto(id, question.value());
    }

    public boolean isWriter(Username username) {
        return Objects.equals(companyUsername, username);
    }

    public void edit(Question question, Answer answer, Username username) {
        if (!isWriter(username)) {
            throw new NotHaveEditAutoReplyAuthority();
        }

        this.question = question;
        this.answer = answer;
    }

    public void delete(Username username) {
        if (!isWriter(username)) {
            throw new NotHaveDeleteAutoReplyAuthority();
        }

        this.status = Status.DELETED;
    }
}
