package com.junhyeong.chatchat.exceptions;

public class ReissueTokenFailed extends RuntimeException {
    public ReissueTokenFailed() {
        super("유효하지 않은 토큰입니다");
    }
}
