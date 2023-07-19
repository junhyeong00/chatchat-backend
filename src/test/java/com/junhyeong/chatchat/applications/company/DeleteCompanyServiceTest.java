package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.exceptions.AuthenticationFailed;
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

class DeleteCompanyServiceTest {
    private CompanyRepository companyRepository;
    private PasswordEncoder passwordEncoder;
    private DeleteCompanyService deleteCompanyService;

    @BeforeEach
    void setup() {
        companyRepository = mock(CompanyRepository.class);
        passwordEncoder = new Argon2PasswordEncoder();
        deleteCompanyService = new DeleteCompanyService(companyRepository, passwordEncoder);
    }

    @Test
    void delete() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");

        Company company = Company.fake(username);
        company.changePassword(password, passwordEncoder);

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(company));

        assertDoesNotThrow(() -> deleteCompanyService.delete(username, password));
    }

    @Test
    void deleteWithUnauthorized() {
        Username invalidUserName = new Username("xxx");
        Password password = new Password("Password1234!");

        given(companyRepository.findByUsername(invalidUserName))
                .willThrow(Unauthorized.class);

        assertThrows(Unauthorized.class,
                () -> deleteCompanyService.delete(invalidUserName, password));
    }

    @Test
    void deletePasswordWithWrongPassword() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");
        Password invalidPassword = new Password("Xxxxxx1234!");

        Company company = Company.fake(username);
        company.changePassword(password, passwordEncoder);

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(company));

        assertThrows(AuthenticationFailed.class,
                () -> deleteCompanyService.delete(username, invalidPassword));
    }
}
