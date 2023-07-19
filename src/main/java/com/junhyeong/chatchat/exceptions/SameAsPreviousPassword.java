package com.junhyeong.chatchat.exceptions;

public class SameAsPreviousPassword extends RuntimeException {
    public SameAsPreviousPassword() {
        super("기존과 동일한 비밀번호로 변경할 수 없습니다");
    }
}
