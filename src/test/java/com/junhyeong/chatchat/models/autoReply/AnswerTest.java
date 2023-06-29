package com.junhyeong.chatchat.models.autoReply;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {
    @Test
    void equality() {
        Answer answer1 = new Answer("답변");
        Answer answer2 = new Answer("답변");
        Answer answer3 = new Answer("안답변");

        assertThat(answer1).isEqualTo(answer2);
        assertThat(answer1).isNotEqualTo(answer3);
    }
}
