package com.junhyeong.chatchat.controllers.common;

import com.junhyeong.chatchat.applications.message.SendMessageService;
import com.junhyeong.chatchat.applications.notification.MessageNotificationService;
import com.junhyeong.chatchat.dtos.MessageRequest;
import com.junhyeong.chatchat.dtos.MessageRequestDto;
import com.junhyeong.chatchat.exceptions.ReceiverDeleted;
import com.junhyeong.chatchat.exceptions.UnknownRole;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final SendMessageService sendMessageService;
    private final MessageNotificationService messageNotificationService;

    public MessageController(SendMessageService sendMessageService,
                             MessageNotificationService messageNotificationService) {
        this.sendMessageService = sendMessageService;
        this.messageNotificationService = messageNotificationService;
    }

    @MessageMapping("/messages")
    public void sendMessage(SimpMessageHeaderAccessor headerAccessor, MessageRequestDto messageRequestDto) {
        MessageRequest messageRequest = MessageRequest.of(messageRequestDto);

        sendMessageService.sendMessage(messageRequest);

        messageNotificationService.sendNotification(messageRequest);

    }

    @ExceptionHandler(UnknownRole.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String unknownRole(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(ReceiverDeleted.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String receiverDeleted(Exception e) {
        return e.getMessage();
    }
}
