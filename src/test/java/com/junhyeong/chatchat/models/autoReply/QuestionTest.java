package com.junhyeong.chatchat.models.autoReply;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {
    @Test
    void equality() {
        Question question1 = new Question("질문");
        Question question2 = new Question("질문");
        Question question3 = new Question("안질문");

        assertThat(question1).isEqualTo(question2);
        assertThat(question1).isNotEqualTo(question3);
    }
}