package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.UserName;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.CompanyRepository;
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
        UserName userName = new UserName("company123");

        given(companyRepository.findByUserName(userName))
                .willReturn(Optional.of(Company.fake(userName)));

        assertDoesNotThrow(() -> getCompanyProfileService.find(userName));
    }

    @Test
    void findWithCompanyNotFound() {
        UserName invalidUserName = new UserName("xxx");

        given(companyRepository.findByUserName(invalidUserName))
                .willThrow(CompanyNotFound.class);

        assertThrows(CompanyNotFound.class,
                () ->  getCompanyProfileService.find(invalidUserName));
    }
}
