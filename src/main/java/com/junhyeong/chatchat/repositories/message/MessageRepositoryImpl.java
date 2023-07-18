package com.junhyeong.chatchat.repositories.message;

import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.models.message.MessageType;
import com.junhyeong.chatchat.models.message.QMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Message> findAllGeneralMessagesByChatRoomId(Long chatRoomId, Pageable pageable) {
        QMessage message = QMessage.message;

        List<Message> messages = queryFactory
                .selectFrom(message)
                .where(message.chatRoomId.eq(chatRoomId).and(
                        message.type.eq(MessageType.GENERAL)
                ))
                .orderBy(message.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = getPageCount(message, chatRoomId);

        return new PageImpl<>(messages, pageable, count);
    }

    private Long getPageCount(QMessage message, Long chatRoomId) {
        return queryFactory
                .select(message.count())
                .from(message)
                .where(message.chatRoomId.eq(chatRoomId).and(
                                message.type.eq(MessageType.GENERAL)))
                .fetchOne();
    }
}
