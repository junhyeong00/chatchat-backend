package com.junhyeong.chatchat.applications;

import com.junhyeong.chatchat.applications.token.IssueTokenService;
import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.RefreshTokenNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.token.Token;
import com.junhyeong.chatchat.repositories.token.RefreshTokenRepository;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class IssueTokenServiceTest {
    private RefreshTokenRepository refreshTokenRepository;
    private IssueTokenService issueTokenService;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil("SECRET");
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        issueTokenService = new IssueTokenService(refreshTokenRepository, jwtUtil);
    }

    @Test
    void issue() {
        Username userName = new Username("test123");

        TokenDto tokenDto = issueTokenService.issue(userName);

        assertThat(tokenDto.accessToken()).contains(".");
        assertThat(tokenDto.refreshToken()).contains(".");

        verify(refreshTokenRepository).save(any(Token.class));
    }

    @Test
    void whenReissueSuccess() {
        Username userName = new Username("test123");

        String token = jwtUtil.encode(UUID.randomUUID());

        given(refreshTokenRepository.findByNumber(token))
                .willReturn(Optional.of(Token.of(userName, token)));

        TokenDto newToken = issueTokenService.reissue(token);

        assertThat(newToken).isNotNull();
    }

    @Test
    void reissueFail() {
        String token = jwtUtil.encode(UUID.randomUUID());

        given(refreshTokenRepository.findByNumber(token))
                .willReturn(Optional.empty());

        assertThrows(RefreshTokenNotFound.class,
                () -> issueTokenService.reissue(token));
    }
}
