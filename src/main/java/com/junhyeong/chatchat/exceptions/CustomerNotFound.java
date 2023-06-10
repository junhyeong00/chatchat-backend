package com.junhyeong.chatchat.exceptions;

public class CustomerNotFound extends RuntimeException {
    public CustomerNotFound() {
        super("고객이 존재하지 않습니다");
    }
}
