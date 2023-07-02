package com.junhyeong.chatchat.exceptions;

public class ExceededNumberAutoReply extends RuntimeException {
    public ExceededNumberAutoReply() {
        super("최대 등록 개수 초과");
    }
}
