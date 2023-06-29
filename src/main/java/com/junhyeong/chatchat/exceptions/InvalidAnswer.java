package com.junhyeong.chatchat.exceptions;

public class InvalidAnswer extends RuntimeException {
    public InvalidAnswer() {
        super("답변의 글자수가 초과되었습니다");
    }
}
