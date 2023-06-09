package com.junhyeong.chatchat.models.token;

import com.junhyeong.chatchat.models.commom.Username;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenTest {
    @Test
    void equality() {
        Username userName = new Username("test123");

        Token token1 = new Token(userName, "a.a.a");
        Token token2 = new Token(userName, "b.b.b");

        assertThat(token1).isEqualTo(token2);
    }
}
