package com.junhyeong.chatchat.exceptions;

public class UsernameAlreadyInUse extends RuntimeException {
    public UsernameAlreadyInUse() {
        super("해당 아이디는 사용할 수 없습니다");
    }
}
