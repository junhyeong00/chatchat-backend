package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.dtos.CompanySummaryDto;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetCompanyProfilesServiceTest {
    private CompanyRepository companyRepository;
    private CustomerRepository customerRepository;
    private GetCompanyProfilesService getCompanyProfilesService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        customerRepository = mock(CustomerRepository.class);
        getCompanyProfilesService = new GetCompanyProfilesService(customerRepository, companyRepository);
    }

    @Test
    void companyProfiles() {
        Username username = new Username("customer123");
        int page = 1;

        String keyword = "";

        given(customerRepository.existsByUsername(username)).willReturn(true);

        given(companyRepository.findAllDtoByKeyword(any(), any()))
                .willReturn(new PageImpl<>(List.of(CompanySummaryDto.fake())));

        Page<CompanySummaryDto> found = getCompanyProfilesService.companyProfiles(username,keyword, page);

        assertThat(found).hasSize(1);
    }
}
