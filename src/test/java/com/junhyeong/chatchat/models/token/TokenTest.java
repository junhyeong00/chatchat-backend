package com.junhyeong.chatchat.models.token;

import com.junhyeong.chatchat.models.user.UserName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenTest {
    @Test
    void equality() {
        UserName userName = new UserName("test123");

        Token token1 = new Token(userName, "a.a.a");
        Token token2 = new Token(userName, "b.b.b");

        assertThat(token1).isEqualTo(token2);
    }
}
