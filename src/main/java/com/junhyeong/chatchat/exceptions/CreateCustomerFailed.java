package com.junhyeong.chatchat.exceptions;

public class CreateCustomerFailed extends RuntimeException {
    public CreateCustomerFailed(String message) {
        super(message);
    }
}
