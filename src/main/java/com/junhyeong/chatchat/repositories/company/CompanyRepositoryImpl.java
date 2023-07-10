package com.junhyeong.chatchat.repositories.company;

import com.junhyeong.chatchat.dtos.CompanySummaryDto;
import com.junhyeong.chatchat.models.company.ProfileVisibility;
import com.junhyeong.chatchat.models.company.QCompany;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.junhyeong.chatchat.models.chatRoom.QChatRoom.chatRoom;

@Repository
public class CompanyRepositoryImpl implements CompanyRepositoryQueryDsl {
    private final JPAQueryFactory queryFactory;

    public CompanyRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<CompanySummaryDto> findAllDtoByKeyword(String keyword, Pageable pageable) {
        QCompany company = QCompany.company;

        List<CompanySummaryDto> companySummaries = queryFactory
                .select(Projections.constructor(CompanySummaryDto.class,
                        company.id,
                        company.name.value,
                        company.description.value,
                        company.profileImage.value
                        ))
                .from(company)
                .where(company.profileVisibility.eq(ProfileVisibility.VISIBLE)
                        .and(company.name.value.contains(keyword)))
                .orderBy(company.registeredAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = getPageCount(company);

        return new PageImpl<>(companySummaries, pageable, count);
    }

    private Long getPageCount(QCompany company) {
        return queryFactory
                .select(company.count())
                .from(company)
                .where(company.profileVisibility.eq(ProfileVisibility.VISIBLE))
                .fetchOne();
    }
}
