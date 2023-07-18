package com.junhyeong.chatchat.controllers.common;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.junhyeong.chatchat.applications.token.IssueTokenService;
import com.junhyeong.chatchat.dtos.ReissuedTokenDto;
import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.ReissueTokenFailed;
import com.junhyeong.chatchat.utils.HttpUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TokenController {
    private final IssueTokenService issueTokenService;
    private final HttpUtil httpUtil;

    public TokenController(IssueTokenService issueTokenService, HttpUtil httpUtil) {
        this.issueTokenService = issueTokenService;
        this.httpUtil = httpUtil;
    }

    @PostMapping("token")
    @ResponseStatus(HttpStatus.CREATED)
    public ReissuedTokenDto reissueToken(
            HttpServletResponse response,
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        try {
            TokenDto token = issueTokenService.reissue(refreshToken);

            ResponseCookie cookie = httpUtil.generateHttpOnlyCookie("refreshToken", token.refreshToken());

            httpUtil.addCookie(cookie, response);

            return new ReissuedTokenDto(token.accessToken());
        } catch (Exception exception) {
            throw new ReissueTokenFailed();
        }
    }

    @DeleteMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
            HttpServletResponse response
    ) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    @ExceptionHandler(ReissueTokenFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidToken(Exception exception, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return exception.getMessage();
    }

    @ExceptionHandler(JWTDecodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidRefreshToken() {
        return "invalid refreshToken";
    }
}
