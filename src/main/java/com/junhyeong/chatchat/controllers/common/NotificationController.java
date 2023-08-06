package com.junhyeong.chatchat.controllers.common;

import com.junhyeong.chatchat.applications.notification.MessageNotificationService;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class NotificationController {
    private final MessageNotificationService messageNotificationService;

    public NotificationController(MessageNotificationService messageNotificationService) {
        this.messageNotificationService = messageNotificationService;
    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(
            @RequestAttribute Username username,
            @RequestParam("userType") String userType
    ) {
        return messageNotificationService.subscribe(username, userType);
    }
}
