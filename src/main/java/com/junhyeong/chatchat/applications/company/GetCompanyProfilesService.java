package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.dtos.CompanySummaryDto;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCompanyProfilesService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public GetCompanyProfilesService(CustomerRepository customerRepository,
                                     CompanyRepository companyRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public Page<CompanySummaryDto> companyProfiles(Username username, String keyword, Integer page) {
        if (!customerRepository.existsByUsername(username)) {
            throw new Unauthorized();
        }

        Pageable pageable = PageRequest.of(page - 1, 10);

        return companyRepository.findAllDtoByKeyword(keyword, pageable);
    }
}
