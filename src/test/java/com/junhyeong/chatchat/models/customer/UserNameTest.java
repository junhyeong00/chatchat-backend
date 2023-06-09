package com.junhyeong.chatchat.models.customer;

import com.junhyeong.chatchat.models.commom.Username;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserNameTest {
    @Test
    void equals() {
        assertThat(new Username("test123"))
                .isEqualTo(new Username("test123"));

        assertThat(new Username("test123"))
                .isNotEqualTo(new Username("test"));

        assertThat(new Username("test123"))
                .isNotEqualTo(null);

        assertThat(new Username("test123"))
                .isNotEqualTo("test123");
    }
}
