package com.junhyeong.chatchat.applications.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetChatRoomsService {
    private final ChatRoomRepository chatRoomRepository;

    public GetChatRoomsService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional
    public Page<ChatRoomDto> chatRooms(Username username, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 10);

        Page<ChatRoomDto> chatRooms = chatRoomRepository.findAllDtoByCompany(username, pageable);

        return chatRooms;
    }
}
