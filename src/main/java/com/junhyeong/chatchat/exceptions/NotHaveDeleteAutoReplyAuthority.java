package com.junhyeong.chatchat.exceptions;

public class NotHaveDeleteAutoReplyAuthority extends RuntimeException {
    public NotHaveDeleteAutoReplyAuthority() {
        super("삭제 권한이 없습니다");
    }
}
