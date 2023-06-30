package com.junhyeong.chatchat.exceptions;

public class NotHaveEditAutoReplyAuthority extends RuntimeException {
    public NotHaveEditAutoReplyAuthority() {
        super("해당 권한이 없습니다");
    }
}
