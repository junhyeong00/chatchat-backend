package com.junhyeong.chatchat.applications.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetChatRoomsServiceTest {
    private ChatRoomRepository chatRoomRepository;
    private GetChatRoomsService getChatRoomsService;

    @BeforeEach
    void setUp() {
        chatRoomRepository = mock(ChatRoomRepository.class);
        getChatRoomsService = new GetChatRoomsService(chatRoomRepository);
    }

    @Test
    void chatRooms() {
        Username username = new Username("company123");
        int page = 1;

        given(chatRoomRepository.findAllDtoByCompany(any(), any()))
                .willReturn(new PageImpl<>(List.of(ChatRoomDto.fake())));

        Page<ChatRoomDto> found = getChatRoomsService.chatRooms(username, page);

        assertThat(found).hasSize(1);
    }
}
