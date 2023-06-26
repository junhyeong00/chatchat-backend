package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetCompanyDetailServiceTest {
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private GetCompanyDetailService getCompanyDetailService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        customerRepository = mock(CustomerRepository.class);
        getCompanyDetailService = new GetCompanyDetailService(customerRepository, companyRepository);
    }

    @Test
    void find() {
        Username username = new Username("customer123");

        Long companyId = 1L;
        Username company = new Username("company123");

        given(customerRepository.existsByUsername(username))
                .willReturn(true);

        given(companyRepository.findById(companyId))
                .willReturn(Optional.of(Company.fake(company)));

        assertDoesNotThrow(() -> getCompanyDetailService.find(username, companyId));
    }

    @Test
    void findWithUnauthorized() {
        Username invalidUserName = new Username("xxx");

        Long companyId = 1L;

        given(customerRepository.existsByUsername(invalidUserName))
                .willReturn(false);

        given(companyRepository.findById(companyId))
                .willThrow(Unauthorized.class);

        assertThrows(Unauthorized.class,
                () ->  getCompanyDetailService.find(invalidUserName, companyId));
    }

    @Test
    void findWithCompanyNotFound() {
        Username username = new Username("customer123");

        Long invalidCompanyId = 999L;

        given(customerRepository.existsByUsername(username))
                .willReturn(true);

        given(companyRepository.findById(invalidCompanyId))
                .willThrow(CompanyNotFound.class);

        assertThrows(CompanyNotFound.class,
                () ->  getCompanyDetailService.find(username, invalidCompanyId));
    }
}
