package com.junhyeong.chatchat.controllers;

import com.junhyeong.chatchat.applications.message.SendMessageService;
import com.junhyeong.chatchat.dtos.MessageRequest;
import com.junhyeong.chatchat.dtos.MessageRequestDto;
import com.junhyeong.chatchat.exceptions.UnknownRole;
import com.junhyeong.chatchat.models.message.Message;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final SendMessageService sendMessageService;

    public MessageController(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @MessageMapping("/stomp/chat")
    public void sendMessage(MessageRequestDto messageRequestDto) {
        MessageRequest messageRequest = MessageRequest.of(messageRequestDto);

        sendMessageService.sendMessage(messageRequest);
    }

    @ExceptionHandler(UnknownRole.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String unknownRole(Exception e) {
        return e.getMessage();
    }
}
