package com.junhyeong.chatchat.repositories.message;

import com.junhyeong.chatchat.models.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryQueryDsl {
    Page<Message> findAllByChatRoomId(Long chatRoomId, Pageable pageable);
    List<Message> findAllByChatRoomId(Long chatRoomId);
}
