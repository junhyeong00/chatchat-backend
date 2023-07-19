package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.dtos.EditCompanyPasswordRequest;
import com.junhyeong.chatchat.exceptions.AuthenticationFailed;
import com.junhyeong.chatchat.exceptions.NotMatchPassword;
import com.junhyeong.chatchat.exceptions.SameAsPreviousPassword;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class EditCompanyPasswordServiceTest {
    private CompanyRepository companyRepository;
    private PasswordEncoder passwordEncoder;
    private EditCompanyPasswordService editCompanyPasswordService;

    @BeforeEach
    void setup() {
        companyRepository = mock(CompanyRepository.class);
        passwordEncoder = new Argon2PasswordEncoder();
        editCompanyPasswordService = new EditCompanyPasswordService(companyRepository, passwordEncoder);
    }

    @Test
    void edit() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");

        EditCompanyPasswordRequest editCompanyRequest = new EditCompanyPasswordRequest(
                password,
                new Password("newPassword1234!"),
                new Password("newPassword1234!")
        );

        Company company = Company.fake(username);
        company.changePassword(password, passwordEncoder);

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(company));

        assertDoesNotThrow(() -> editCompanyPasswordService.edit(username, editCompanyRequest));
    }

    @Test
    void editWithUnauthorized() {
        Username invalidUserName = new Username("xxx");
        Password password = new Password("Password1234!");

        EditCompanyPasswordRequest editCompanyRequest = new EditCompanyPasswordRequest(
                password,
                new Password("newPassword1234!"),
                new Password("newPassword1234!")
        );

        given(companyRepository.findByUsername(invalidUserName))
                .willThrow(Unauthorized.class);

        assertThrows(Unauthorized.class,
                () -> editCompanyPasswordService.edit(invalidUserName, editCompanyRequest));
    }

    @Test
    void editPasswordWithNotMatchPassword() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");

        EditCompanyPasswordRequest editCompanyRequest = new EditCompanyPasswordRequest(
                password,
                new Password("newPassword4321!"),
                new Password("newPassword1234!")
        );

        Company company = Company.fake(username);
        company.changePassword(password, passwordEncoder);

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(company));

        assertThrows(NotMatchPassword.class,
                () -> editCompanyPasswordService.edit(username, editCompanyRequest));
    }

    @Test
    void editPasswordWithSameAsPreviousPassword() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");

        EditCompanyPasswordRequest editCompanyRequest = new EditCompanyPasswordRequest(
                password,
                new Password("Password1234!"),
                new Password("Password1234!")
        );

        Company company = Company.fake(username);
        company.changePassword(password, passwordEncoder);

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(company));

        assertThrows(SameAsPreviousPassword.class,
                () -> editCompanyPasswordService.edit(username, editCompanyRequest));
    }

    @Test
    void editPasswordWithWrongPassword() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");
        Password invalidPassword = new Password("Xxxxxx1234!");

        EditCompanyPasswordRequest editCompanyRequest = new EditCompanyPasswordRequest(
                invalidPassword,
                new Password("newPassword1234!"),
                new Password("newPassword1234!")
        );

        Company company = Company.fake(username);
        company.changePassword(password, passwordEncoder);

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(company));

        assertThrows(AuthenticationFailed.class,
                () -> editCompanyPasswordService.edit(username, editCompanyRequest));
    }
}
