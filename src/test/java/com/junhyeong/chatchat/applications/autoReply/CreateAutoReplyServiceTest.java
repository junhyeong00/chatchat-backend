package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.dtos.CreateAutoReplyRequest;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.autoReply.Question;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.autoReply.AutoReplyRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateAutoReplyServiceTest {
    private CompanyRepository companyRepository;
    private AutoReplyRepository autoReplyRepository;
    private CreateAutoReplyService createAutoReplyService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        autoReplyRepository = mock(AutoReplyRepository.class);
        createAutoReplyService = new CreateAutoReplyService(companyRepository, autoReplyRepository);
    }

    @Test
    void create() {
        Username username = new Username("company123");

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(Company.fake(username)));

        CreateAutoReplyRequest createAutoReplyRequest = CreateAutoReplyRequest.fake(new Question("질문"));

        AutoReply autoReply = AutoReply.fake(username);

        given(autoReplyRepository.save(any(AutoReply.class)))
                .willReturn(autoReply);

        Long saved = createAutoReplyService.create(username, createAutoReplyRequest);

        assertThat(saved).isNotNull();
    }

    @Test
    void createWithUnauthorized() {
        Username invalidUsername = new Username("xxx");

        CreateAutoReplyRequest createAutoReplyRequest = CreateAutoReplyRequest.fake(new Question("질문"));

        given(companyRepository.findByUsername(invalidUsername))
                .willThrow(Unauthorized.class);

        assertThrows(Unauthorized.class,
                () -> createAutoReplyService.create(invalidUsername, createAutoReplyRequest));
    }
}
