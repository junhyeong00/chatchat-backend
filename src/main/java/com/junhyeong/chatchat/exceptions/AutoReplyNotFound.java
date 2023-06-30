package com.junhyeong.chatchat.exceptions;

public class AutoReplyNotFound extends RuntimeException {
    public AutoReplyNotFound() {
        super("자동 질문답변이 존재하지 않습니다");
    }
}
