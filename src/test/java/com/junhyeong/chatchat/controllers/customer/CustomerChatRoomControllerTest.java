package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.chatRoom.CreateChatRoomService;
import com.junhyeong.chatchat.applications.chatRoom.GetCustomerChatRoomService;
import com.junhyeong.chatchat.applications.chatRoom.GetCustomerChatRoomsService;
import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerChatRoomController.class)
class CustomerChatRoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCustomerChatRoomsService getChatRoomsService;

    @MockBean
    private GetCustomerChatRoomService getChatRoomService;

    @MockBean
    private CreateChatRoomService createChatRoomService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void chatRooms() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        Page<ChatRoomDto> page = new PageImpl<>(List.of(ChatRoomDto.fake()));

        given(getChatRoomsService.chatRooms(username, 1))
                .willReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/customer/chatrooms")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void chatRoomDetail() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        Long chatRoomId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/customer/chatrooms/%d", chatRoomId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void chatRoomDetailWithUnauthorized() throws Exception {
        Username invalidUsername = new Username("xxx");
        String token = jwtUtil.encode(invalidUsername);

        int page = 1;

        Long chatRoomId = 999L;

        given(getChatRoomService.chatRoomDetail(invalidUsername, chatRoomId, page))
                .willThrow(ChatRoomNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/customer/chatrooms/%d", chatRoomId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void chatRoomDetailWithChatRoomNotFound() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        int page = 1;

        Long chatRoomId = 999L;

        given(getChatRoomService.chatRoomDetail(username, chatRoomId, page))
                .willThrow(ChatRoomNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/customer/chatrooms/%d", chatRoomId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void chatRoomDetailWithCompanyNotFound() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        int page = 1;

        Long chatRoomId = 1L;

        given(getChatRoomService.chatRoomDetail(username, chatRoomId, page))
                .willThrow(CompanyNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/customer/chatrooms/%d", chatRoomId))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWithAlreadyExistChatRoom() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        given(createChatRoomService.getCharRoomId(any(), any()))
                .willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/customer/chatrooms")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"companyId\":\"1\"" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    void create() throws Exception {
        Username username = new Username("customer123");
        String token = jwtUtil.encode(username);

        given(createChatRoomService.getCharRoomId(any(), any()))
                .willThrow(ChatRoomNotFound.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/customer/chatrooms")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"companyId\":\"1\"" +
                                "}"))
                .andExpect(status().isCreated());
    }
}
