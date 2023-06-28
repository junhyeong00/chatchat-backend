package com.junhyeong.chatchat.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3Component {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Component() {
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
