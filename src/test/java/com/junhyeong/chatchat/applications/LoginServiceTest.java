package com.junhyeong.chatchat.applications;

import com.junhyeong.chatchat.applications.login.LoginService;
import com.junhyeong.chatchat.applications.token.IssueTokenService;
import com.junhyeong.chatchat.applications.customer.GetUserService;
import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.LoginFailed;
import com.junhyeong.chatchat.exceptions.UserNotFound;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
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
        Username userName = new Username("test123");
        Password password = new Password("Password1234!");

        Customer customer = Customer.fake(userName);

        customer.changePassword(password, passwordEncoder);

        given(issueTokenService.issue(userName))
                .willReturn(TokenDto.fake());

        given(getUserService.find(userName))
                .willReturn(customer);

        TokenDto token = loginService.login(userName, password);

        assertThat(token).isNotNull();
    }

    @Test
    void loginWithWrongUserName() {
        Username userName = new Username("notTest123");
        Password password = new Password("Password1234!");

        given(getUserService.find(userName))
                .willThrow(UserNotFound.class);

        assertThrows(LoginFailed.class, () -> loginService.login(userName, password));
    }

    @Test
    void loginWithWrongPassword() {
        Username userName = new Username("test123");
        Password password = new Password("Password1234!");
        Password wrongPassword = new Password("notPassword1234!");

        Customer customer = Customer.fake(userName);

        customer.changePassword(password, passwordEncoder);

        given(getUserService.find(userName)).willReturn(customer);

        assertThrows(LoginFailed.class,
                () -> loginService.login(userName, wrongPassword));
    }
}
