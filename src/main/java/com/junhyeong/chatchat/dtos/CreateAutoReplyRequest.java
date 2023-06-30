package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.autoReply.Answer;
import com.junhyeong.chatchat.models.autoReply.Question;

public class CreateAutoReplyRequest {
    private Question question;
    private Answer answer;

    public CreateAutoReplyRequest(Question question, Answer answer) {
        this.question = question;
        this.answer = answer;
    }

    public static CreateAutoReplyRequest of(CreateAutoReplyRequestDto createAutoReplyRequestDto) {
        return new CreateAutoReplyRequest(
                new Question(createAutoReplyRequestDto.question()),
                new Answer(createAutoReplyRequestDto.answer())
        );
    }

    public static CreateAutoReplyRequest fake(Question question) {
        return new CreateAutoReplyRequest(
                question,
                new Answer("답변")
        );
    }

    public Question getQuestion() {
        return question;
    }

    public Answer getAnswer() {
        return answer;
    }
}
