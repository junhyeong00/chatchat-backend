package com.junhyeong.chatchat.exceptions;

public class AuthenticationFailed extends RuntimeException {
    public AuthenticationFailed() {
        super("현재 비밀번호가 맞지 않습니다");
    }
}
