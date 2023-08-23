package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.login.CustomerLoginService;
import com.junhyeong.chatchat.dtos.LoginRequestDto;
import com.junhyeong.chatchat.dtos.LoginResultDto;
import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.LoginFailed;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.utils.HttpUtil;
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
@RequestMapping("customer/session")
public class CustomerSessionController {
    private final CustomerLoginService loginService;
    private final HttpUtil httpUtil;

    public CustomerSessionController(CustomerLoginService loginService, HttpUtil httpUtil) {
        this.loginService = loginService;
        this.httpUtil = httpUtil;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResultDto login(
            HttpServletResponse response,
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {
        try {
            Username userName = Username.of(loginRequestDto.username());
            Password password = Password.of(loginRequestDto.password(), "encode");

            TokenDto token = loginService.login(userName, password);

            ResponseCookie cookie = httpUtil.generateHttpOnlyCookie("refreshToken", token.refreshToken());

            httpUtil.addCookie(cookie, response);

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
