package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.autoReply.GetAutoReplyQuestionsService;
import com.junhyeong.chatchat.applications.autoReply.SendAutoReplyService;
import com.junhyeong.chatchat.dtos.AutoReplyQuestionDto;
import com.junhyeong.chatchat.dtos.AutoReplyQuestionsDto;
import com.junhyeong.chatchat.dtos.AutoReplyRequestDto;
import com.junhyeong.chatchat.exceptions.AutoReplyNotFound;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.exceptions.SendAutoReplyFailed;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("auto-replies")
public class CustomerAutoReplyController {
    private final GetAutoReplyQuestionsService getAutoReplyQuestionsService;
    private final SendAutoReplyService sendAutoReplyService;

    public CustomerAutoReplyController(GetAutoReplyQuestionsService getAutoReplyQuestionsService,
                                       SendAutoReplyService sendAutoReplyService) {
        this.getAutoReplyQuestionsService = getAutoReplyQuestionsService;
        this.sendAutoReplyService = sendAutoReplyService;
    }

    @GetMapping
    public AutoReplyQuestionsDto autoReplyQuestions(
            @RequestAttribute Username username,
            @RequestParam Long companyId
    ) {
        List<AutoReplyQuestionDto> autoReplyQuestionDtos
                = getAutoReplyQuestionsService.questions(username, companyId);

        return new AutoReplyQuestionsDto(autoReplyQuestionDtos);
    }

    @PostMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendAutoReply(
            @RequestAttribute Username username,
            @PathVariable Long id,
            @RequestBody AutoReplyRequestDto autoReplyRequestDto
    ) {
        try {
            sendAutoReplyService.send(username, id, autoReplyRequestDto.chatRoomId());
        } catch (Unauthorized e) {
            throw new Unauthorized();
        } catch (ChatRoomNotFound e) {
            throw new ChatRoomNotFound();
        } catch (Exception e) {
            throw new SendAutoReplyFailed(e.getMessage());
        }
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(CompanyNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String companyNotFound(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(ChatRoomNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String chatRoomNotFound(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(AutoReplyNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String autoReplyNotFound(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(SendAutoReplyFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String sendAutoReplyFailed(Exception e) {
        return e.getMessage();
    }
}
