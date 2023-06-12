package com.junhyeong.chatchat.exceptions;

public class ChatRoomNotFound extends RuntimeException {
    public ChatRoomNotFound() {
        super("채팅방이 존재하지 않습니다");
    }
}
