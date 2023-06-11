package com.junhyeong.chatchat.repositories.message;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageRepositoryQueryDsl {
    List<Message> findAllGeneralMessagesByChatRoomId(Long chatRoomId);
}
