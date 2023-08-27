package com.junhyeong.chatchat.repositories.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryQueryDsl {
    Page<ChatRoomDto> findAllDtoByCompany(Username company, Pageable pageable);
    Page<ChatRoomDto> findAllDtoByCustomer(Username company, Pageable pageable);
    ChatRoomDto findDtoByCompany(Username company, Long chatRoomId);
    ChatRoomDto findDtoByCustomer(Username company, Long chatRoomId);
}
