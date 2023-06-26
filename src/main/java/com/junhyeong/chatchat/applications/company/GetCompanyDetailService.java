package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCompanyDetailService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public GetCompanyDetailService(CustomerRepository customerRepository, CompanyRepository companyRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public Company find(Username username, Long id) {
        if (!customerRepository.existsByUsername(username)) {
            throw new Unauthorized();
        }

        return companyRepository.findById(id)
                .orElseThrow(CompanyNotFound::new);
    }
}
