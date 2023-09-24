package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.exceptions.CompanyDeleted;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCompanyService {
    private final CompanyRepository companyRepository;

    public GetCompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public Company find(Username username) {
        Company company = companyRepository.findByUsername(username)
                .orElseThrow(CompanyNotFound::new);

        if (company.isDeleted()) {
            throw new CompanyDeleted();
        }

        return company;
    }
}
