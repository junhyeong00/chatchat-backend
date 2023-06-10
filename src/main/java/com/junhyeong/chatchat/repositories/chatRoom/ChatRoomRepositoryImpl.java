package com.junhyeong.chatchat.repositories.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.chatRoom.QChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.QCustomer;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.models.message.MessageType;
import com.junhyeong.chatchat.models.message.QMessage;
import com.junhyeong.chatchat.models.message.ReadStatus;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryQueryDsl {
    private final JPAQueryFactory queryFactory;

    public ChatRoomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<ChatRoomDto> findAllDtoByCompany(Username company, Pageable pageable) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QMessage message = QMessage.message;
        QMessage message2 = QMessage.message;
        QMessage message3 = QMessage.message;
        QCustomer customer = QCustomer.customer;

        List<ChatRoomDto> chatRooms = queryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                        chatRoom.id,
                        customer.name,
                        customer.profileImage,
                        message.content,
                        message.createdAt,
                        ExpressionUtils.as(
                                JPAExpressions.select(ExpressionUtils.count(message2.id))
                                        .from(message2)
                                        .where(message2.chatroomId.eq(chatRoom.id)
                                                .and(message2.readStatus.eq(ReadStatus.UNREAD))),
                                "unreadMessageCount"
                        )
                        ))
                .from(chatRoom)
                .leftJoin(message)
                .on(chatRoom.id.eq(message.chatroomId))
                .leftJoin(customer)
                .on(chatRoom.customer.eq(customer.userName))
                .where(message.type.eq(MessageType.GENERAL))
                .groupBy(chatRoom.id, customer.name, customer.profileImage,
                         message.content, message.createdAt)
                .having(message.createdAt.eq(
                        JPAExpressions.select(message3.createdAt.max())
                                .from(message3)
                                .where(message3.chatroomId.eq(chatRoom.id))
                ))
                .orderBy(
                        new CaseBuilder()
                                .when(message.readStatus.eq(ReadStatus.UNREAD))
                                .then(message.createdAt.min())
                                .otherwise(message.createdAt.max())
                                .desc()
                )
                .fetch();

        Long count = queryFactory
                .select(chatRoom.count())
                .from(chatRoom)
                .where(chatRoom.company.eq(company))
                .fetchOne();

        return new PageImpl<>(chatRooms, pageable, count);
    }
}
