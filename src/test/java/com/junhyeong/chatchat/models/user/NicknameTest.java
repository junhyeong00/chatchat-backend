package com.junhyeong.chatchat.models.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NicknameTest {
    @Test
    void equality() {
        Nickname nickname1 = new Nickname("김뚜루");
        Nickname nickname2 = new Nickname("김뚜루");
        Nickname nickname3 = new Nickname("안김뚜루");

        assertThat(nickname1).isEqualTo(nickname2);
        assertThat(nickname1).isNotEqualTo(nickname3);
    }
}
