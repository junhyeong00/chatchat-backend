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
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
        QCustomer customer = QCustomer.customer;

        List<ChatRoomDto> chatRooms = queryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                        chatRoom.id,
                        customer.name,
                        customer.profileImage,
                        message.content,
                        message.createdAt,
                        getUnreadMessageCount(chatRoom)
                        ))
                .from(chatRoom)
                .leftJoin(message)
                .on(chatRoom.id.eq(message.chatRoomId))
                .leftJoin(customer)
                .on(chatRoom.customer.eq(customer.username))
                .where(message.type.eq(MessageType.GENERAL))
                .groupBy(chatRoom.id, customer.name, customer.profileImage,
                         message.content, message.createdAt)
                .having(message.createdAt.eq(
                        getLastCreatedAt(chatRoom)
                ))
                .orderBy(
                        new CaseBuilder()
                                .when(message.readStatus.eq(ReadStatus.UNREAD))
                                .then(message.createdAt.min())
                                .otherwise(message.createdAt.max())
                                .desc()
                )
                .fetch();

        Long count = getPageCount(company, chatRoom);

        return new PageImpl<>(chatRooms, pageable, count);
    }

    private Expression<Long> getUnreadMessageCount(QChatRoom chatRoom) {
        QMessage message = QMessage.message;

        return ExpressionUtils.as(
                JPAExpressions.select(ExpressionUtils.count(message.id))
                        .from(message)
                        .where(message.chatRoomId.eq(chatRoom.id)
                                .and(message.readStatus.eq(ReadStatus.UNREAD))),
                "unreadMessageCount"
        );
    }

    private JPQLQuery<LocalDateTime> getLastCreatedAt(QChatRoom chatRoom) {
        QMessage message = QMessage.message;

        return JPAExpressions.select(message.createdAt.max())
                .from(message)
                .where(message.chatRoomId.eq(chatRoom.id));
    }

    private Long getPageCount(Username company, QChatRoom chatRoom) {
        return queryFactory
                .select(chatRoom.count())
                .from(chatRoom)
                .where(chatRoom.company.eq(company))
                .fetchOne();
    }
}
