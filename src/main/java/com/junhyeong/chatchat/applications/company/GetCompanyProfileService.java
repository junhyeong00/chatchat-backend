package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCompanyProfileService {
    private final CompanyRepository companyRepository;

    public GetCompanyProfileService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public Company find(Username userName) {
        return companyRepository.findByUsername(userName)
                .orElseThrow(Unauthorized::new);
    }
}
