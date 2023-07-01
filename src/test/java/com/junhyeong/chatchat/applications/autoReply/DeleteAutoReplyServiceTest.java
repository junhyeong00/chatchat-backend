package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.dtos.EditAutoReplyRequest;
import com.junhyeong.chatchat.exceptions.AutoReplyNotFound;
import com.junhyeong.chatchat.exceptions.NotHaveDeleteAutoReplyAuthority;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DeleteAutoReplyServiceTest {
    private AutoReplyRepository autoReplyRepository;
    private DeleteAutoReplyService deleteAutoReplyService;

    @BeforeEach
    void setUp() {
        autoReplyRepository = mock(AutoReplyRepository.class);
        deleteAutoReplyService = new DeleteAutoReplyService(autoReplyRepository);
    }

    @Test
    void delete() {
        Username username = new Username("company123");

        Long autoReplyId = 1L;

        AutoReply autoReply = AutoReply.fake(username);

        given(autoReplyRepository.findById(autoReplyId))
                .willReturn(Optional.of(autoReply));

        assertDoesNotThrow(() -> deleteAutoReplyService.delete(username, autoReplyId));
    }

    @Test
    void deleteWithAutoReplyNotFound() {
        Username username = new Username("company123");

        Long autoReplyId = 1L;

        given(autoReplyRepository.findById(autoReplyId))
                .willThrow(AutoReplyNotFound.class);

        assertThrows(AutoReplyNotFound.class,
                () -> deleteAutoReplyService.delete(username, autoReplyId));
    }

    @Test
    void deleteWithNotHaveDeleteAutoReplyAuthority() {
        Username username = new Username("company123");

        Long autoReplyId = 1L;

        Username another = new Username("another");
        AutoReply autoReply = AutoReply.fake(another);

        given(autoReplyRepository.findById(autoReplyId))
                .willReturn(Optional.of(autoReply));

        assertThrows(NotHaveDeleteAutoReplyAuthority.class,
                () -> deleteAutoReplyService.delete(username, autoReplyId));
    }
}
