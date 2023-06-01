package com.junhyeong.chatchat.exceptions;

public class InvalidDescription extends RuntimeException {
    public InvalidDescription() {
        super("설명의 글자수가 초과되었습니다");
    }
}
