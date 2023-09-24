package com.junhyeong.chatchat.exceptions;

public class ReceiverDeleted extends RuntimeException {
    public ReceiverDeleted() {
        super("대화가 불가능한 사용자입니다.");
    }
}
