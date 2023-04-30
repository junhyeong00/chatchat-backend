package com.junhyeong.chatchat.exceptions;

public class LoginFailed extends RuntimeException {
    public LoginFailed() {
        super("로그인에 실패했습니다");
    }
}
