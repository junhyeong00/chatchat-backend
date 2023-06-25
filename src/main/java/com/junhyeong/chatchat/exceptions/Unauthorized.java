package com.junhyeong.chatchat.exceptions;

public class Unauthorized extends RuntimeException {
    public Unauthorized() {
        super("인증되지 않은 사용자입니다");
    }
}
