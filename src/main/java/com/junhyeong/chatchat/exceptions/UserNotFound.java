package com.junhyeong.chatchat.exceptions;

public class UserNotFound extends RuntimeException{
    public UserNotFound() {
        super("유저가 존재하지 않습니다");
    }
}
