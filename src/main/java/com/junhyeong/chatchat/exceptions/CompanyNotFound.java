package com.junhyeong.chatchat.exceptions;

public class CompanyNotFound extends RuntimeException {
    public CompanyNotFound() {
        super("기업이 존재하지 않습니다");
    }
}
