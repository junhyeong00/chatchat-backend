package com.junhyeong.chatchat.repositories.company;

import com.junhyeong.chatchat.dtos.CompanySummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryQueryDsl {
    Page<CompanySummaryDto> findAllDtoByKeyword(String keyword, Pageable page);
}
