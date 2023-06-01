package com.junhyeong.chatchat.models.company;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DescriptionTest {
    @Test
    void equality() {
        Description description1 = new Description("설명");
        Description description2 = new Description("설명");
        Description description3 = new Description("안설명");

        assertThat(description1).isEqualTo(description2);
        assertThat(description1).isNotEqualTo(description3);
    }
}
