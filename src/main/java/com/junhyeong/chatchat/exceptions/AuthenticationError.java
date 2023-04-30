package com.junhyeong.chatchat.exceptions;

public class AuthenticationError extends RuntimeException {
    public AuthenticationError() {
        super("Authentication error");
    }
}