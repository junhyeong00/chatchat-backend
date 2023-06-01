package com.junhyeong.chatchat.applications.login;

import com.junhyeong.chatchat.applications.user.GetUserService;
import com.junhyeong.chatchat.applications.token.IssueTokenService;
import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.LoginFailed;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.user.User;
import com.junhyeong.chatchat.models.commom.UserName;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {
    private final GetUserService getUserService;
    private final IssueTokenService issueTokenService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(GetUserService getUserService,
                        IssueTokenService issueTokenService,
                        PasswordEncoder passwordEncoder) {
        this.getUserService = getUserService;
        this.issueTokenService = issueTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenDto login(UserName userName, Password password) {
        try {
            User user = getUserService.find(userName);

            user.authenticate(password, passwordEncoder);

            TokenDto tokenDto = issueTokenService.issue(userName);

            return tokenDto;
        } catch (Exception e) {
            throw new LoginFailed();
        }
    }
}
