package com.junhyeong.chatchat.controllers.company;

import com.junhyeong.chatchat.applications.autoReply.CreateAutoReplyService;
import com.junhyeong.chatchat.applications.autoReply.GetAutoRepliesService;
import com.junhyeong.chatchat.dtos.AutoRepliesDto;
import com.junhyeong.chatchat.dtos.AutoReplyDto;
import com.junhyeong.chatchat.dtos.CreateAutoReplyRequest;
import com.junhyeong.chatchat.dtos.CreateAutoReplyRequestDto;
import com.junhyeong.chatchat.dtos.CreateAutoReplyResultDto;
import com.junhyeong.chatchat.exceptions.CreateAutoReplyFailed;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("auto-replies")
public class AutoReplyController {
    private final GetAutoRepliesService getAutoRepliesService;
    private final CreateAutoReplyService createAutoReplyService;

    public AutoReplyController(GetAutoRepliesService getAutoRepliesService,
                               CreateAutoReplyService createAutoReplyService) {
        this.getAutoRepliesService = getAutoRepliesService;
        this.createAutoReplyService = createAutoReplyService;
    }

    @GetMapping
    public AutoRepliesDto autoReplies(
            @RequestAttribute Username username
    ) {
        List<AutoReply> autoReplies = getAutoRepliesService.autoReplies(username);

        List<AutoReplyDto> autoReplyDtos = autoReplies.stream().map(AutoReply::toDto).toList();

        return new AutoRepliesDto(autoReplyDtos);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAutoReplyResultDto create(
            @RequestAttribute Username username,
            @Validated @RequestBody CreateAutoReplyRequestDto createAutoReplyRequestDto
    ) {
        try {
            CreateAutoReplyRequest createAutoReplyRequest = CreateAutoReplyRequest.of(createAutoReplyRequestDto);

            Long created = createAutoReplyService.create(username, createAutoReplyRequest);

            return new CreateAutoReplyResultDto(created);
        } catch (Unauthorized unauthorized) {
            throw new Unauthorized();
        } catch (Exception exception) {
            throw new CreateAutoReplyFailed(exception.getMessage());
        }
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(CreateAutoReplyFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String createAutoReplyFailed(Exception e) {
        return e.getMessage();
    }
}
