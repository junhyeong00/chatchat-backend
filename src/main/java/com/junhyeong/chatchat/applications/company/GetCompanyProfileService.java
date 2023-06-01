package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.UserName;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class GetCompanyProfileService {
    private final CompanyRepository companyRepository;

    public GetCompanyProfileService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company find(UserName userName) {
        return companyRepository.findByUserName(userName)
                .orElseThrow(CompanyNotFound::new);
    }
}
