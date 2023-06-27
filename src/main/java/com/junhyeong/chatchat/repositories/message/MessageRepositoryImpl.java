package com.junhyeong.chatchat.repositories.message;

import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.models.message.MessageType;
import com.junhyeong.chatchat.models.message.QMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepositoryImpl implements MessageRepositoryQueryDsl {
    private final JPAQueryFactory queryFactory;

    public MessageRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Message> findAllGeneralMessagesByChatRoomId(Long chatRoomId) {
        QMessage message = QMessage.message;

        return queryFactory
                .selectFrom(message)
                .where(message.chatRoomId.eq(chatRoomId).and(
                        message.type.eq(MessageType.GENERAL)
                ))
                .fetch();
    }
}
