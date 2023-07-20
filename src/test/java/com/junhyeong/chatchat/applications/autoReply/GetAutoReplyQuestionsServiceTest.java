package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.dtos.AutoReplyQuestionDto;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.autoReply.AutoReplyRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetAutoReplyQuestionsServiceTest {
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private AutoReplyRepository autoReplyRepository;
    private GetAutoReplyQuestionsService getAutoRepliesService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        companyRepository = mock(CompanyRepository.class);
        autoReplyRepository = mock(AutoReplyRepository.class);
        getAutoRepliesService = new GetAutoReplyQuestionsService(
                customerRepository,companyRepository, autoReplyRepository);
    }

    @Test
    void autoReplies() {
        Username username = new Username("customer");
        Username company = new Username("company");
        Long companyId = 1L;

        List<AutoReply> autoReplies = List.of(AutoReply.fake(username));

        given(customerRepository.existsByUsername(username))
                .willReturn(true);

        given(companyRepository.findById(companyId))
                .willReturn(Optional.of(Company.fake(company)));

        given(autoReplyRepository.findAllByCompanyUsername(company))
                .willReturn(autoReplies);

        List<AutoReplyQuestionDto> found = getAutoRepliesService.questions(username, companyId);

        assertThat(found).hasSize(1);
    }

    @Test
    void autoRepliesWithUnauthorized() {
        Username invalidUsername = new Username("xxx");
        Long companyId = 1L;

        given(customerRepository.existsByUsername(invalidUsername))
                .willReturn(false);

        given(companyRepository.findById(companyId))
                .willReturn(Optional.of(Company.fake(new Username("company"))));

        assertThrows(Unauthorized.class,
                () -> getAutoRepliesService.questions(invalidUsername, companyId));
    }

    @Test
    void autoRepliesWithCompanyNotFound() {
        Username username = new Username("customer");
        Long companyId = 999L;

        given(customerRepository.existsByUsername(username))
                .willReturn(true);

        given(companyRepository.findById(companyId))
                .willThrow(CompanyNotFound.class);

        assertThrows(CompanyNotFound.class,
                () -> getAutoRepliesService.questions(username, companyId));
    }
}
