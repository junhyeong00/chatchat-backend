package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetCompanyServiceTest {
    private CompanyRepository companyRepository;
    private GetCompanyService getCompanyService;

    @BeforeEach
    void setup() {
        companyRepository = mock(CompanyRepository.class);
        getCompanyService = new GetCompanyService(companyRepository);
    }

    @Test
    void whenCustomerExists() {
        Username userName = new Username("test123");

        given(companyRepository.findByUsername(userName))
                .willReturn(Optional.of(Company.fake(userName)));

        assertDoesNotThrow(() -> getCompanyService.find(userName));
    }

    @Test
    void whenCustomerNotExists() {
        Username userName = new Username("notExists@gmail.com");

        given(companyRepository.findByUsername(userName))
                .willReturn(Optional.empty());

        assertThrows(CompanyNotFound.class,
                () -> getCompanyService.find(userName));
    }
}