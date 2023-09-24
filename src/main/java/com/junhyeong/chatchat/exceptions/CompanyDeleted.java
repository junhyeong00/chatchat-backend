package com.junhyeong.chatchat.exceptions;

public class CompanyDeleted extends RuntimeException {
    public CompanyDeleted() {
        super("탈퇴된 계정입니다.");
    }
}
