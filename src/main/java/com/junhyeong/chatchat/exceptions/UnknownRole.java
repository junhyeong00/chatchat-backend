package com.junhyeong.chatchat.exceptions;

public class UnknownRole extends RuntimeException {
    public UnknownRole() {
        super("알 수 없는 역할입니다.");
    }
}
