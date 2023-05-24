package com.junhyeong.chatchat.controllers;

import com.junhyeong.chatchat.applications.LoginService;
import com.junhyeong.chatchat.dtos.LoginRequestDto;
import com.junhyeong.chatchat.dtos.LoginResultDto;
import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.LoginFailed;
import com.junhyeong.chatchat.models.user.Password;
import com.junhyeong.chatchat.models.user.UserName;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("session")
public class SessionController {
    private final LoginService loginService;

    public SessionController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResultDto login(
            HttpServletResponse response,
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {
        try {
        UserName userName = UserName.of(loginRequestDto.userName());
        Password password = Password.of(loginRequestDto.password());

        TokenDto token = loginService.login(userName, password);

            ResponseCookie cookie = ResponseCookie.from("refreshToken", token.refreshToken())
                    .httpOnly(true)
                    .path("/")
                    .sameSite("Lax")
                    .domain("localhost")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new LoginResultDto(token.accessToken());
    } catch (Exception exception) {
        throw new LoginFailed();
    }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidRequest() {
        return "이메일 혹은 비밀번호가 잘못 되었습니다";
    }

    @ExceptionHandler(LoginFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String loginFailed(Exception e) {
        return e.getMessage();
    }
}
