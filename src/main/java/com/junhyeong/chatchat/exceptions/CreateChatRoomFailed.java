package com.junhyeong.chatchat.exceptions;

public class CreateChatRoomFailed extends RuntimeException {
    public CreateChatRoomFailed(String message) {
        super(message);
    }
}
