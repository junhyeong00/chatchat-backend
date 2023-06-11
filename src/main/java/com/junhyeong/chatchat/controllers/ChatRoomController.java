package com.junhyeong.chatchat.controllers;

import com.junhyeong.chatchat.applications.chatRoom.GetChatRoomsService;
import com.junhyeong.chatchat.applications.chatroom.GetChatRoomService;
import com.junhyeong.chatchat.dtos.ChatRoomDetailDto;
import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.dtos.ChatRoomsDto;
import com.junhyeong.chatchat.dtos.PageDto;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CustomerNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("company/chatrooms")
public class ChatRoomController {
    private final GetChatRoomsService getChatRoomsService;
    private final GetChatRoomService getChatRoomService;

    public ChatRoomController(GetChatRoomsService getChatRoomsService,
                              GetChatRoomService getChatRoomService) {
        this.getChatRoomsService = getChatRoomsService;
        this.getChatRoomService = getChatRoomService;
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

    @GetMapping("{id}")
    public ChatRoomDetailDto chatRoomDetail(
            @RequestAttribute Username username,
            @PathVariable("id") Long chatRoomId
    ) {
        return getChatRoomService.chatRoomDetail(username, chatRoomId);
    }

    @ExceptionHandler(ChatRoomNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String chatRoomNotFound(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(CustomerNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String customerNotFound(Exception e) {
        return e.getMessage();
    }
}
