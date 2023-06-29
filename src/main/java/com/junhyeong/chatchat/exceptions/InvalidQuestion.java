package com.junhyeong.chatchat.exceptions;

public class InvalidQuestion extends RuntimeException {
    public InvalidQuestion() {
        super("질문의 글자수가 초과되었습니다");
    }
}
