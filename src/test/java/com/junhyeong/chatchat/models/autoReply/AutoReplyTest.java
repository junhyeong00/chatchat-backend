package com.junhyeong.chatchat.models.autoReply;

import com.junhyeong.chatchat.exceptions.NotHaveEditAutoReplyAuthority;
import com.junhyeong.chatchat.models.commom.Username;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutoReplyTest {
    @Test
    void isWriter() {
        Username username = new Username("company123");

        AutoReply autoReply = AutoReply.fake(username);

        assertThat(autoReply.isWriter(username)).isTrue();
    }

    @Test
    void isWriterWithNotHaveEditAutoReplyAuthority() {
        Username username = new Username("company123");

        Username another = new Username("another");
        AutoReply autoReply = AutoReply.fake(another);

        assertThat(autoReply.isWriter(username)).isFalse();
    }
}
