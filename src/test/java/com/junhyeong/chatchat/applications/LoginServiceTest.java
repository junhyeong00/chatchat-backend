package com.junhyeong.chatchat.applications;

import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.LoginFailed;
import com.junhyeong.chatchat.exceptions.UserNotFound;
import com.junhyeong.chatchat.models.user.Password;
import com.junhyeong.chatchat.models.user.User;
import com.junhyeong.chatchat.models.user.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
class LoginServiceTest {
    private LoginService loginService;
    private GetUserService getUserService;
    private IssueTokenService issueTokenService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        getUserService = mock(GetUserService.class);
        issueTokenService = mock(IssueTokenService.class);
        passwordEncoder = new Argon2PasswordEncoder();
        loginService = new LoginService(getUserService, issueTokenService, passwordEncoder);
    }

    @Test
    void loginSuccess() {
        UserName userName = new UserName("test123");
        Password password = new Password("Password1234!");

        User user = User.fake(userName);

        user.changePassword(password, passwordEncoder);

        given(issueTokenService.issue(userName))
                .willReturn(TokenDto.fake());

        given(getUserService.find(userName))
                .willReturn(user);

        TokenDto token = loginService.login(userName, password);

        assertThat(token).isNotNull();
    }

    @Test
    void loginWithWrongUserName() {
        UserName userName = new UserName("notTest123");
        Password password = new Password("Password1234!");

        given(getUserService.find(userName))
                .willThrow(UserNotFound.class);

        assertThrows(LoginFailed.class, () -> loginService.login(userName, password));
    }

    @Test
    void loginWithWrongPassword() {
        UserName userName = new UserName("test123");
        Password password = new Password("Password1234!");
        Password wrongPassword = new Password("notPassword1234!");

        User user = User.fake(userName);

        user.changePassword(password, passwordEncoder);

        given(getUserService.find(userName)).willReturn(user);

        assertThrows(LoginFailed.class,
                () -> loginService.login(userName, wrongPassword));
    }
}
