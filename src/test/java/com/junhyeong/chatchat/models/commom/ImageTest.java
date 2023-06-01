package com.junhyeong.chatchat.models.commom;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageTest {
    @Test
    void equality() {
        Image image1 = new Image("이미지");
        Image image2 = new Image("이미지");
        Image image3 = new Image("안이미지");

        assertThat(image1).isEqualTo(image2);
        assertThat(image1).isNotEqualTo(image3);
    }
}
