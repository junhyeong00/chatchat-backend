package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.autoReply.Answer;
import com.junhyeong.chatchat.models.autoReply.Question;

public class EditAutoReplyRequest {
    private Long autoReplyId;
    private Question question;
    private Answer answer;

    public EditAutoReplyRequest(Long autoReplyId, Question question, Answer answer) {
        this.autoReplyId = autoReplyId;
        this.question = question;
        this.answer = answer;
    }

    public static EditAutoReplyRequest of(Long autoReplyId, EditAutoReplyRequestDto editAutoReplyRequestDto) {
        return new EditAutoReplyRequest(
                autoReplyId,
                new Question(editAutoReplyRequestDto.question()),
                new Answer(editAutoReplyRequestDto.answer())
        );
    }

    public static EditAutoReplyRequest fake(Long autoReplyId) {
        return new EditAutoReplyRequest(
                autoReplyId,
                new Question("수정된 질문"),
                new Answer("수정된 답변")
        );
    }

    public Long getAutoReplyId() {
        return autoReplyId;
    }

    public Question getQuestion() {
        return question;
    }

    public Answer getAnswer() {
        return answer;
    }
}
