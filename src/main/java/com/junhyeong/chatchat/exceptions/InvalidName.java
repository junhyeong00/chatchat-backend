package com.junhyeong.chatchat.exceptions;

public class InvalidName extends RuntimeException {
    public InvalidName() {
        super("이름의 글자수가 초과되었습니다");
    }
}
