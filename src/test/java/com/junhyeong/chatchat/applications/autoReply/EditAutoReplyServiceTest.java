package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.dtos.EditAutoReplyRequest;
import com.junhyeong.chatchat.exceptions.AutoReplyNotFound;
import com.junhyeong.chatchat.exceptions.NotHaveEditAutoReplyAuthority;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.autoReply.AutoReplyRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class EditAutoReplyServiceTest {
    private CompanyRepository companyRepository;
    private AutoReplyRepository autoReplyRepository;
    private EditAutoReplyService editAutoReplyService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        autoReplyRepository = mock(AutoReplyRepository.class);
        editAutoReplyService = new EditAutoReplyService(companyRepository, autoReplyRepository);
    }

    @Test
    void edit() {
        Username username = new Username("company123");

        Long autoReplyId = 1L;

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(Company.fake(username)));

        EditAutoReplyRequest editAutoReplyRequest = EditAutoReplyRequest.fake(autoReplyId);

        AutoReply autoReply = AutoReply.fake(username);

        given(autoReplyRepository.findById(autoReplyId))
                .willReturn(Optional.of(autoReply));

        assertDoesNotThrow(() -> editAutoReplyService.edit(username, editAutoReplyRequest));
    }

    @Test
    void editWithUnauthorized() {
        Username invalidUsername = new Username("xxx");

        Long autoReplyId = 1L;

        EditAutoReplyRequest editAutoReplyRequest = EditAutoReplyRequest.fake(autoReplyId);

        given(companyRepository.findByUsername(invalidUsername))
                .willThrow(Unauthorized.class);

        assertThrows(Unauthorized.class,
                () -> editAutoReplyService.edit(invalidUsername, editAutoReplyRequest));
    }

    @Test
    void editWithAutoReplyNotFound() {
        Username username = new Username("company123");

        Long autoReplyId = 1L;

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(Company.fake(username)));

        EditAutoReplyRequest editAutoReplyRequest = EditAutoReplyRequest.fake(autoReplyId);

        given(autoReplyRepository.findById(autoReplyId))
                .willThrow(AutoReplyNotFound.class);

        assertThrows(AutoReplyNotFound.class,
                () -> editAutoReplyService.edit(username, editAutoReplyRequest));
    }

    @Test
    void editWithNotHaveEditAutoReplyAuthority() {
        Username username = new Username("company123");

        Long autoReplyId = 1L;

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(Company.fake(username)));

        EditAutoReplyRequest editAutoReplyRequest = EditAutoReplyRequest.fake(autoReplyId);

        Username another = new Username("another");
        AutoReply autoReply = AutoReply.fake(another);

        given(autoReplyRepository.findById(autoReplyId))
                .willReturn(Optional.of(autoReply));

        assertThrows(NotHaveEditAutoReplyAuthority.class,
                () -> editAutoReplyService.edit(username, editAutoReplyRequest));
    }
}
