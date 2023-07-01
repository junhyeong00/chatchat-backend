package com.junhyeong.chatchat.controllers.company;

import com.junhyeong.chatchat.applications.autoReply.CreateAutoReplyService;
import com.junhyeong.chatchat.applications.autoReply.DeleteAutoReplyService;
import com.junhyeong.chatchat.applications.autoReply.EditAutoReplyService;
import com.junhyeong.chatchat.applications.autoReply.GetAutoRepliesService;
import com.junhyeong.chatchat.dtos.AutoRepliesDto;
import com.junhyeong.chatchat.dtos.AutoReplyDto;
import com.junhyeong.chatchat.dtos.CreateAutoReplyRequest;
import com.junhyeong.chatchat.dtos.CreateAutoReplyRequestDto;
import com.junhyeong.chatchat.dtos.CreateAutoReplyResultDto;
import com.junhyeong.chatchat.dtos.EditAutoReplyRequest;
import com.junhyeong.chatchat.dtos.EditAutoReplyRequestDto;
import com.junhyeong.chatchat.exceptions.AutoReplyNotFound;
import com.junhyeong.chatchat.exceptions.CreateAutoReplyFailed;
import com.junhyeong.chatchat.exceptions.EditAutoReplyFailed;
import com.junhyeong.chatchat.exceptions.NotHaveDeleteAutoReplyAuthority;
import com.junhyeong.chatchat.exceptions.NotHaveEditAutoReplyAuthority;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final EditAutoReplyService editAutoReplyService;
    private final DeleteAutoReplyService deleteAutoReplyService;

    public AutoReplyController(GetAutoRepliesService getAutoRepliesService,
                               CreateAutoReplyService createAutoReplyService,
                               EditAutoReplyService editAutoReplyService,
                               DeleteAutoReplyService deleteAutoReplyService) {
        this.getAutoRepliesService = getAutoRepliesService;
        this.createAutoReplyService = createAutoReplyService;
        this.editAutoReplyService = editAutoReplyService;
        this.deleteAutoReplyService = deleteAutoReplyService;
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

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(
            @RequestAttribute Username username,
            @PathVariable Long id,
            @Validated @RequestBody EditAutoReplyRequestDto editAutoReplyRequestDto
    ) {
        try {
            EditAutoReplyRequest editAutoReplyRequest = EditAutoReplyRequest.of(id, editAutoReplyRequestDto);

            editAutoReplyService.edit(username, editAutoReplyRequest);
        } catch (Unauthorized e) {
            throw new Unauthorized();
        } catch (AutoReplyNotFound e) {
            throw new AutoReplyNotFound();
        } catch (NotHaveEditAutoReplyAuthority e) {
            throw new NotHaveEditAutoReplyAuthority();
        } catch (Exception exception) {
            throw new EditAutoReplyFailed(exception.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestAttribute Username username,
            @PathVariable Long id
    ) {
        deleteAutoReplyService.delete(username, id);
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(AutoReplyNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String autoReplyNotFound(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotHaveEditAutoReplyAuthority.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String notHaveEditAutoReplyAuthority(Exception exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(NotHaveDeleteAutoReplyAuthority.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String notHaveDeleteAutoReplyAuthority(Exception exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(CreateAutoReplyFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String createAutoReplyFailed(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EditAutoReplyFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String editAutoReplyFailed(Exception e) {
        return e.getMessage();
    }
}
