package com.junhyeong.chatchat.repositories.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.models.commom.Username;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryQueryDsl {
    Page<ChatRoomDto> findAllDtoByCompany(Username company, Pageable pageable);
    Page<ChatRoomDto> findAllDtoByCustomer(Username customer, Pageable pageable);
}
