package com.junhyeong.chatchat.controllers.company;

import com.junhyeong.chatchat.applications.autoReply.GetAutoRepliesService;
import com.junhyeong.chatchat.dtos.AutoRepliesDto;
import com.junhyeong.chatchat.dtos.AutoReplyDto;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("auto-replies")
public class AutoReplyController {
    private final GetAutoRepliesService getAutoRepliesService;

    public AutoReplyController(GetAutoRepliesService getAutoRepliesService) {
        this.getAutoRepliesService = getAutoRepliesService;
    }

    @GetMapping
    public AutoRepliesDto autoReplies(
            @RequestAttribute Username username
    ) {
        List<AutoReply> autoReplies = getAutoRepliesService.autoReplies(username);

        List<AutoReplyDto> autoReplyDtos = autoReplies.stream().map(AutoReply::toDto).toList();

        return new AutoRepliesDto(autoReplyDtos);
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }
}
