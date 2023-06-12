package com.junhyeong.chatchat.repositories.message;

import com.junhyeong.chatchat.models.message.Message;

import java.util.List;

public interface MessageRepositoryQueryDsl {
    List<Message> findAllGeneralMessagesByChatRoomId(Long chatRoomId);
}
