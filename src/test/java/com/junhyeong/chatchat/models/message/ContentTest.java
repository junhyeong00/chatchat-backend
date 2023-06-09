package com.junhyeong.chatchat.models.message;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContentTest {
    @Test
    void equality() {
        Content content1 = new Content("내용");
        Content content2 = new Content("내용");
        Content content3 = new Content("안내용");

        assertThat(content1).isEqualTo(content2);
        assertThat(content1).isNotEqualTo(content3);
    }
}
