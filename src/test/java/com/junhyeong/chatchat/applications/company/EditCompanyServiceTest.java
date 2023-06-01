package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.dtos.EditCompanyRequest;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.UserName;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class EditCompanyServiceTest {
    private CompanyRepository companyRepository;
    private EditCompanyService editCompanyService;

    @BeforeEach
    void setUp() {
        companyRepository =mock(CompanyRepository.class);
        editCompanyService = new EditCompanyService(companyRepository);
    }

    @Test
    void edit() {
        UserName userName = new UserName("company123");

        EditCompanyRequest editCompanyRequest = EditCompanyRequest.fake(new Name("악덕기업"));

        given(companyRepository.findByUserName(userName))
                .willReturn(Optional.of(Company.fake(userName)));

        assertDoesNotThrow(() -> editCompanyService.edit(userName, editCompanyRequest));
    }

    @Test
    void editWithCompanyNotFound() {
        UserName invalidUserName = new UserName("xxx");

        EditCompanyRequest editCompanyRequest = EditCompanyRequest.fake(new Name("악덕기업"));

        given(companyRepository.findByUserName(invalidUserName))
                .willThrow(CompanyNotFound.class);

        assertThrows(CompanyNotFound.class,
                () ->  editCompanyService.edit(invalidUserName, editCompanyRequest));
    }
}
