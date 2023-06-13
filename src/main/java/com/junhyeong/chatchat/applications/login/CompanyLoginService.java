package com.junhyeong.chatchat.applications.login;

import com.junhyeong.chatchat.applications.company.GetCompanyService;
import com.junhyeong.chatchat.applications.token.IssueTokenService;
import com.junhyeong.chatchat.dtos.TokenDto;
import com.junhyeong.chatchat.exceptions.LoginFailed;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyLoginService {
    private final GetCompanyService getCompanyService;
    private final IssueTokenService issueTokenService;
    private final PasswordEncoder passwordEncoder;

    public CompanyLoginService(GetCompanyService getCompanyService,
                               IssueTokenService issueTokenService,
                               PasswordEncoder passwordEncoder) {
        this.getCompanyService = getCompanyService;
        this.issueTokenService = issueTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenDto login(Username userName, Password password) {
        try {
            Company company = getCompanyService.find(userName);

            company.authenticate(password, passwordEncoder);

            TokenDto tokenDto = issueTokenService.issue(userName);

            return tokenDto;
        } catch (Exception e) {
            throw new LoginFailed();
        }
    }
}
