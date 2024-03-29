package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.chatRoom.CreateChatRoomService;
import com.junhyeong.chatchat.applications.chatRoom.GetCustomerChatRoomService;
import com.junhyeong.chatchat.applications.chatRoom.GetCustomerChatRoomsService;
import com.junhyeong.chatchat.dtos.ChatRoomDetailDto;
import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.dtos.ChatRoomsDto;
import com.junhyeong.chatchat.dtos.CreateChatRoomRequestDto;
import com.junhyeong.chatchat.dtos.CreateChatRoomResultDto;
import com.junhyeong.chatchat.dtos.CreateCompanyRequestDto;
import com.junhyeong.chatchat.dtos.PageDto;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.CreateChatRoomFailed;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("customer/chatrooms")
public class CustomerChatRoomController {
    private final GetCustomerChatRoomsService getChatRoomsService;
    private final GetCustomerChatRoomService getChatRoomService;
    private final CreateChatRoomService createChatRoomService;

    public CustomerChatRoomController(GetCustomerChatRoomsService getChatRoomsService,
                                      GetCustomerChatRoomService getChatRoomService,
                                      CreateChatRoomService createChatRoomService) {
        this.getChatRoomsService = getChatRoomsService;
        this.getChatRoomService = getChatRoomService;
        this.createChatRoomService = createChatRoomService;
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

    @PostMapping
    public CreateChatRoomResultDto create(
            @RequestAttribute Username username,
            @Validated @RequestBody CreateChatRoomRequestDto createChatRoomRequest,
            HttpServletResponse response
            ) {
        try {
            Long chatRoomId = createChatRoomService.getCharRoomId(username, createChatRoomRequest.companyId());

            return new CreateChatRoomResultDto(chatRoomId);
        } catch (ChatRoomNotFound e) {
            response.setStatus(201);
            Long chatRoomId = createChatRoomService.create(username, createChatRoomRequest.companyId());

            return new CreateChatRoomResultDto(chatRoomId);
        } catch (Exception e) {
            throw new CreateChatRoomFailed(e.getMessage());
        }
    }


    @GetMapping("{id}")
    public ChatRoomDetailDto chatRoomDetail(
            @RequestAttribute Username username,
            @PathVariable("id") Long chatRoomId,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        return getChatRoomService.chatRoomDetail(username, chatRoomId, page);
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(ChatRoomNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String chatRoomNotFound(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(CompanyNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String companyNotFound(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(CreateChatRoomFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String createChatRoomFailed(Exception e) {
        return e.getMessage();
    }
}
