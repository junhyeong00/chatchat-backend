package com.junhyeong.chatchat.exceptions;

public class NotMatchPassword extends RuntimeException {
    public NotMatchPassword() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
