package com.junhyeong.chatchat.exceptions;

public class RefreshTokenNotFound extends RuntimeException {
    public RefreshTokenNotFound() {
        super("유효하지 않는 토큰입니다");
    }
}
