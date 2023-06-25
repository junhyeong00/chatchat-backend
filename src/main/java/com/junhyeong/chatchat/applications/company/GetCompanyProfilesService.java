package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.dtos.CompanySummaryDto;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCompanyProfilesService {
    private final CompanyRepository companyRepository;

    public GetCompanyProfilesService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public Page<CompanySummaryDto> companyProfiles(Username username, String keyword, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 10);

        return companyRepository.findAllDtoByKeyword(keyword, pageable);
    }
}
