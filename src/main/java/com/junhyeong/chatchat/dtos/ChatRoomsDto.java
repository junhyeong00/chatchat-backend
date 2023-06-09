package com.junhyeong.chatchat.dtos;

import java.util.List;

public class ChatRoomsDto {
    private List<ChatRoomDto> chatRooms;
    private PageDto page;

    public ChatRoomsDto(List<ChatRoomDto> chatRooms, PageDto page) {
        this.chatRooms = chatRooms;
        this.page = page;
    }

    public List<ChatRoomDto> getChatRooms() {
        return chatRooms;
    }

    public PageDto getPage() {
        return page;
    }
}
