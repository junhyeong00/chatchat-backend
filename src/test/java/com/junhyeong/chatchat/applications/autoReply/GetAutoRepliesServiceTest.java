package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.autoReply.AutoReplyRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetAutoRepliesServiceTest {
    private CompanyRepository companyRepository;
    private AutoReplyRepository autoReplyRepository;
    private GetAutoRepliesService getAutoRepliesService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        autoReplyRepository = mock(AutoReplyRepository.class);
        getAutoRepliesService = new GetAutoRepliesService(companyRepository, autoReplyRepository);
    }

    @Test
    void autoReplies() {
        Username username = new Username("company123");

        List<AutoReply> autoReplies = List.of(AutoReply.fake(username));

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(Company.fake(username)));

        given(autoReplyRepository.findAllByCompanyUsername(username))
                .willReturn(autoReplies);

        List<AutoReply> found = getAutoRepliesService.autoReplies(username);

        assertThat(found).hasSize(1);
        assertThat(found.get(0).companyUsername()).isEqualTo(username);
    }

    @Test
    void autoRepliesWithUnauthorized() {
        Username invalidUsername = new Username("company123");

        given(companyRepository.findByUsername(invalidUsername))
                .willThrow(Unauthorized.class);

        assertThrows(Unauthorized.class,
                () -> getAutoRepliesService.autoReplies(invalidUsername));
    }
}
