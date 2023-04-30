package com.junhyeong.chatchat.exceptions;

public class InvalidPassword extends RuntimeException {
    public InvalidPassword() {
        super("비밀번호 양식이 잘못 되었습니다");
    }
}
