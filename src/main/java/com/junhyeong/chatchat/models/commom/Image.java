package com.junhyeong.chatchat.models.commom;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Image {
    public static final String DEFAULT_PROFILE_IMAGE
            = "https://chatchat-bucket.s3.ap-northeast-2.amazonaws.com/default-profile-image.png";

    @Column(name = "image")
    private String value;

    public Image() {
    }

    public Image(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Image otherImage = (Image) object;

        return Objects.equals(value, otherImage.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
