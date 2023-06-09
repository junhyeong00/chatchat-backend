package com.junhyeong.chatchat.controllers;

import com.junhyeong.chatchat.applications.chatRoom.GetChatRoomsService;
import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.dtos.ChatRoomsDto;
import com.junhyeong.chatchat.dtos.PageDto;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("company/chatrooms")
public class ChatRoomController {
    private final GetChatRoomsService getChatRoomsService;

    public ChatRoomController(GetChatRoomsService getChatRoomsService) {
        this.getChatRoomsService = getChatRoomsService;
    }

    @GetMapping
    public ChatRoomsDto chatRooms(
            @RequestAttribute Username username,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        Page<ChatRoomDto> found = getChatRoomsService.chatRooms(username, page);

        List<ChatRoomDto> chatRooms = found.stream().toList();

        PageDto pageDto = new PageDto(page, found.getTotalPages());

        return new ChatRoomsDto(chatRooms, pageDto);
    }
}
