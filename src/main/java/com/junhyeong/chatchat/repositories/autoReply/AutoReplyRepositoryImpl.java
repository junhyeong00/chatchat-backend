package com.junhyeong.chatchat.repositories.autoReply;

import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.autoReply.QAutoReply;
import com.junhyeong.chatchat.models.commom.Status;
import com.junhyeong.chatchat.models.commom.Username;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AutoReplyRepositoryImpl implements AutoReplyRepositoryQueryDsl {
    private final JPAQueryFactory queryFactory;

    public AutoReplyRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<AutoReply> findAllByCompanyUsername(Username username) {
        QAutoReply autoReply = QAutoReply.autoReply;

        return queryFactory
                .selectFrom(autoReply)
                .where(autoReply.companyUsername.eq(username).and(
                        autoReply.status.ne(Status.DELETED)
                ))
                .fetch();
    }

    @Override
    public Long countByUsername(Username username) {
        QAutoReply autoReply = QAutoReply.autoReply;

        return queryFactory
                .select(autoReply.count())
                .from(autoReply)
                .where(autoReply.companyUsername.eq(username).and(
                        autoReply.status.ne(Status.DELETED)
                ))
                .fetchOne();
    }
}
