package com.junhyeong.chatchat.exceptions;

public class CustomerDeleted extends RuntimeException {
    public CustomerDeleted() {
        super("탈퇴된 계정입니다.");
    }
}
