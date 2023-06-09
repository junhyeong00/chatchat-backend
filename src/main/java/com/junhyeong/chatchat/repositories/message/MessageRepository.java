package com.junhyeong.chatchat.repositories.message;

import com.junhyeong.chatchat.models.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
