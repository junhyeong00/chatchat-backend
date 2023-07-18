package com.junhyeong.chatchat.applications.company;

import com.junhyeong.chatchat.dtos.CreateCompanyRequest;
import com.junhyeong.chatchat.exceptions.UsernameAlreadyInUse;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CreateCompanyServiceTest {
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private PasswordEncoder passwordEncoder;
    private CreateCompanyService createCompanyService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        companyRepository = mock(CompanyRepository.class);
        passwordEncoder = new Argon2PasswordEncoder();
        createCompanyService = new CreateCompanyService(customerRepository, companyRepository, passwordEncoder);
    }

    @Test
    void create() {
        Username username = new Username("company123");

        CreateCompanyRequest createCompanyRequest = CreateCompanyRequest.fake(username, new Name("고객"));

        given(customerRepository.existsByUsername(username))
                .willReturn(false);

        given(companyRepository.existsByUsername(username))
                .willReturn(false);

        assertDoesNotThrow(() -> createCompanyService.create(createCompanyRequest));
    }

    @Test
    void createWithUsernameAlreadyInUse() {
        Username username = new Username("company123");

        CreateCompanyRequest createCompanyRequest = CreateCompanyRequest.fake(username, new Name("고객"));

        given(customerRepository.existsByUsername(username))
                .willReturn(true);

        assertThrows(UsernameAlreadyInUse.class,
                () -> createCompanyService.create(createCompanyRequest));
    }
}
