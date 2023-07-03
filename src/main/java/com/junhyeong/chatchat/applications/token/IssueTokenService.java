package com.junhyeong.chatchat.applications.token;

import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.RefreshTokenNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.token.Token;
import com.junhyeong.chatchat.repositories.token.RefreshTokenRepository;
import com.junhyeong.chatchat.utils.HttpUtil;
import com.junhyeong.chatchat.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class IssueTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public IssueTokenService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public TokenDto issue(Username username) {
        String accessToken = jwtUtil.encode(username);
        String refreshToken = jwtUtil.encode(UUID.randomUUID());

        Token tokenEntity = Token.of(username, refreshToken);

        refreshTokenRepository.save(tokenEntity);

        return new TokenDto(accessToken, refreshToken);
    }

    public TokenDto reissue(String token) {
        jwtUtil.decodeRefreshToken(token);

        Token refreshToken = refreshTokenRepository.findByNumber(token)
                .orElseThrow(RefreshTokenNotFound::new);

        String accessToken = refreshToken.getNextAccessToken(jwtUtil);

        return new TokenDto(accessToken, token);
    }
}
