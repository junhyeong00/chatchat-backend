package com.junhyeong.chatchat.models.user;

import com.junhyeong.chatchat.models.commom.Name;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NameTest {
    @Test
    void equality() {
        Name name1 = new Name("김뚜루");
        Name name2 = new Name("김뚜루");
        Name name3 = new Name("안김뚜루");

        assertThat(name1).isEqualTo(name2);
        assertThat(name1).isNotEqualTo(name3);
    }
}
