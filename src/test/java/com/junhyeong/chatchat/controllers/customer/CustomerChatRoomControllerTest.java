package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.chatRoom.GetCustomerChatRoomsService;
import com.junhyeong.chatchat.dtos.ChatRoomDto;
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

@WebMvcTest(CustomerChatRoomController.class)
class CustomerChatRoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCustomerChatRoomsService getChatRoomsService;

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
}
