package com.junhyeong.chatchat.exceptions;

public class InvalidUsername extends RuntimeException {
    public InvalidUsername() {
        super("아이디의 글자수가 초과되었습니다");
    }
}
