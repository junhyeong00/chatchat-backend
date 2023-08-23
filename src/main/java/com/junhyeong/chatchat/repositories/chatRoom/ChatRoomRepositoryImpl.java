package com.junhyeong.chatchat.repositories.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.models.chatRoom.QChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.QCompany;
import com.junhyeong.chatchat.models.customer.QCustomer;
import com.junhyeong.chatchat.models.message.MessageType;
import com.junhyeong.chatchat.models.message.QMessage;
import com.junhyeong.chatchat.models.message.ReadStatus;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
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
                        getUnreadMessageCount(chatRoom, company)
                        ))
                .from(chatRoom)
                .innerJoin(message)
                .on(chatRoom.id.eq(message.chatRoomId))
                .leftJoin(customer)
                .on(chatRoom.customer.eq(customer.username))
                .where(message.type.eq(MessageType.GENERAL))
                .groupBy(chatRoom.id, customer.name, customer.profileImage,
                         message.content, message.createdAt, message.readStatus)
                .having(message.createdAt.eq(
                        getLastCreatedAt(chatRoom)).and(
                        chatRoom.id.in(
                                JPAExpressions
                                        .selectDistinct(message.chatRoomId)
                                        .from(message)
                                        .where(message.type.eq(MessageType.GENERAL))
                        )
                ))
                .orderBy(
                        new CaseBuilder()
                                .when(message.readStatus.eq(ReadStatus.UNREAD))
                                .then(message.createdAt.min())
                                .otherwise(message.createdAt.max())
                                .desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = getPageCountByCompany(company, chatRoom);

        return new PageImpl<>(chatRooms, pageable, count);
    }

    @Override
    public Page<ChatRoomDto> findAllDtoByCustomer(Username customer, Pageable pageable) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QMessage message = QMessage.message;
        QCompany company = QCompany.company;

        List<ChatRoomDto> chatRooms = queryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                        chatRoom.id,
                        company.name,
                        company.profileImage,
                        message.content,
                        message.createdAt,
                        getUnreadMessageCount(chatRoom, customer)
                ))
                .from(chatRoom)
                .leftJoin(message)
                .on(chatRoom.id.eq(message.chatRoomId))
                .leftJoin(company)
                .on(chatRoom.company.eq(company.username))
                .where(chatRoom.customer.eq(customer))
                .groupBy(chatRoom.id, company.name, company.profileImage,
                        message.content, message.createdAt, message.readStatus)
                .having(message.createdAt.eq(
                        getLastCreatedAt(chatRoom)
                ))
                .orderBy(message.createdAt.max().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = getPageCountByCustomer(customer, chatRoom);

        return new PageImpl<>(chatRooms, pageable, count);
    }

    @Override
    public ChatRoomDto findDtoByCompany(Username company) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QMessage message = QMessage.message;
        QCustomer customer = QCustomer.customer;

        return queryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                        chatRoom.id,
                        customer.name,
                        customer.profileImage,
                        message.content,
                        message.createdAt,
                        getUnreadMessageCount(chatRoom, company)
                ))
                .from(chatRoom)
                .innerJoin(message)
                .on(chatRoom.id.eq(message.chatRoomId))
                .leftJoin(customer)
                .on(chatRoom.customer.eq(customer.username))
                .where(message.type.eq(MessageType.GENERAL).and(
                        chatRoom.company.eq(company)
                ))
                .groupBy(chatRoom.id, customer.name, customer.profileImage,
                        message.content, message.createdAt, message.readStatus)
                .having(message.createdAt.eq(
                        getLastCreatedAt(chatRoom)).and(
                        chatRoom.id.in(
                                JPAExpressions
                                        .selectDistinct(message.chatRoomId)
                                        .from(message)
                                        .where(message.type.eq(MessageType.GENERAL))
                        )
                ))
                .fetchOne();
    }

    @Override
    public ChatRoomDto findDtoByCustomer(Username customer) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QMessage message = QMessage.message;
        QCompany company = QCompany.company;

        return queryFactory
                .select(Projections.constructor(ChatRoomDto.class,
                        chatRoom.id,
                        company.name,
                        company.profileImage,
                        message.content,
                        message.createdAt,
                        getUnreadMessageCount(chatRoom, customer)
                ))
                .from(chatRoom)
                .leftJoin(message)
                .on(chatRoom.id.eq(message.chatRoomId))
                .leftJoin(company)
                .on(chatRoom.company.eq(company.username))
                .groupBy(chatRoom.id, company.name, company.profileImage,
                        message.content, message.createdAt, message.readStatus)
                .having(message.createdAt.eq(
                        getLastCreatedAt(chatRoom)
                ))
                .fetchOne();
    }

    private Expression<Long> getUnreadMessageCount(QChatRoom chatRoom, Username username) {
        QMessage message = QMessage.message;

        return ExpressionUtils.as(
                JPAExpressions.select(ExpressionUtils.count(message.id))
                        .from(message)
                        .where(message.chatRoomId.eq(chatRoom.id).and(
                                message.readStatus.eq(ReadStatus.UNREAD)).and(
                                        message.sender.username.ne(username)
                        )),
                "unreadMessageCount"
        );
    }

    private JPQLQuery<LocalDateTime> getLastCreatedAt(QChatRoom chatRoom) {
        QMessage message = QMessage.message;

        return JPAExpressions.select(message.createdAt.max())
                .from(message)
                .where(message.chatRoomId.eq(chatRoom.id));
    }

    private Long getPageCountByCompany(Username company, QChatRoom chatRoom) {
        return queryFactory
                .select(chatRoom.count())
                .from(chatRoom)
                .where(chatRoom.company.eq(company))
                .fetchOne();
    }

    private Long getPageCountByCustomer(Username customer, QChatRoom chatRoom) {
        return queryFactory
                .select(chatRoom.count())
                .from(chatRoom)
                .where(chatRoom.customer.eq(customer))
                .fetchOne();
    }
}
