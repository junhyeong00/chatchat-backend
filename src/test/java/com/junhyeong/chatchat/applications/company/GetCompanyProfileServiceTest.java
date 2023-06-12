package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetCompanyProfileServiceTest {
    private CompanyRepository companyRepository;
    private GetCompanyProfileService getCompanyProfileService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        getCompanyProfileService = new GetCompanyProfileService(companyRepository);
    }

    @Test
    void find() {
        Username userName = new Username("company123");

        given(companyRepository.findByUsername(userName))
                .willReturn(Optional.of(Company.fake(userName)));

        assertDoesNotThrow(() -> getCompanyProfileService.find(userName));
    }

    @Test
    void findWithCompanyNotFound() {
        Username invalidUserName = new Username("xxx");

        given(companyRepository.findByUsername(invalidUserName))
                .willThrow(CompanyNotFound.class);

        assertThrows(CompanyNotFound.class,
                () ->  getCompanyProfileService.find(invalidUserName));
    }
}
