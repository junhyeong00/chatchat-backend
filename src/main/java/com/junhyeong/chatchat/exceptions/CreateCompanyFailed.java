package com.junhyeong.chatchat.exceptions;

public class CreateCompanyFailed extends RuntimeException {
    public CreateCompanyFailed(String message) {
        super(message);
    }
}
