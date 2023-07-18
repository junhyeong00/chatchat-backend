package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.dtos.CreateCompanyRequest;
import com.junhyeong.chatchat.exceptions.NotMatchPassword;
import com.junhyeong.chatchat.exceptions.UsernameAlreadyInUse;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCompanyService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateCompanyService(CustomerRepository customerRepository,
                                CompanyRepository companyRepository,
                                PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(CreateCompanyRequest createCompanyRequest) {
        if (!createCompanyRequest.getPassword().equals(createCompanyRequest.getConfirmPassword())) {
            throw new NotMatchPassword();
        }

        if (customerRepository.existsByUsername(createCompanyRequest.getUsername()) ||
                companyRepository.existsByUsername(createCompanyRequest.getUsername())) {
            throw new UsernameAlreadyInUse();
        }

        Company company = new Company(
                createCompanyRequest.getUsername(),
                createCompanyRequest.getName()
        );

        company.changePassword(createCompanyRequest.getPassword(), passwordEncoder);

        companyRepository.save(company);
    }
}
