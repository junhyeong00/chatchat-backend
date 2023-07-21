package com.junhyeong.chatchat.exceptions;

public class SendAutoReplyFailed extends RuntimeException {
    public SendAutoReplyFailed(String message) {
        super(message);
    }
}
