package com.junhyeong.chatchat.controllers.company;

import com.junhyeong.chatchat.applications.chatRoom.GetCompanyChatRoomService;
import com.junhyeong.chatchat.applications.chatRoom.GetCompanyChatRoomsService;
import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CustomerNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyChatRoomController.class)
class CompanyChatRoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCompanyChatRoomsService getChatRoomsService;

    @MockBean
    private GetCompanyChatRoomService getChatRoomService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void chatRooms() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        Page<ChatRoomDto> page = new PageImpl<>(List.of(ChatRoomDto.fake()));

        given(getChatRoomsService.chatRooms(username, 1))
                .willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/company/chatrooms")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void chatRoomDetail() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        Long chatRoomId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/company/chatrooms/%d", chatRoomId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void chatRoomDetailWithChatRoomNotFound() throws Exception {
        Username username = new Username("xxx");
        String token = jwtUtil.encode(username);

        Long chatRoomId = 1L;

        given(getChatRoomService.chatRoomDetail(username, chatRoomId))
                .willThrow(ChatRoomNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/company/chatrooms/%d", chatRoomId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void chatRoomDetailWithCustomerNotFound() throws Exception {
        Username username = new Username("company123");
        String token = jwtUtil.encode(username);

        Long chatRoomId = 1L;

        given(getChatRoomService.chatRoomDetail(username, chatRoomId))
                .willThrow(CustomerNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/company/chatrooms/%d", chatRoomId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
