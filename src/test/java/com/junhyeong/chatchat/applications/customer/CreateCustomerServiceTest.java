package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.dtos.CreateCustomerRequest;
import com.junhyeong.chatchat.dtos.EditCustomerRequest;
import com.junhyeong.chatchat.exceptions.UsernameAlreadyInUse;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CreateCustomerServiceTest {
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private PasswordEncoder passwordEncoder;
    private CreateCustomerService createCustomerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        companyRepository = mock(CompanyRepository.class);
        passwordEncoder = new Argon2PasswordEncoder();
        createCustomerService = new CreateCustomerService(customerRepository, companyRepository, passwordEncoder);
    }

    @Test
    void create() {
        Username username = new Username("customer123");

        CreateCustomerRequest createCustomerRequest = CreateCustomerRequest.fake(username, new Name("고객"));

        given(customerRepository.existsByUsername(username))
                .willReturn(false);

        given(companyRepository.existsByUsername(username))
                .willReturn(false);

        assertDoesNotThrow(() -> createCustomerService.create(createCustomerRequest));
    }

    @Test
    void createWithUsernameAlreadyInUse() {
        Username username = new Username("customer123");

        CreateCustomerRequest createCustomerRequest = CreateCustomerRequest.fake(username, new Name("고객"));

        given(customerRepository.existsByUsername(username))
                .willReturn(true);

        assertThrows(UsernameAlreadyInUse.class,
                () -> createCustomerService.create(createCustomerRequest));
    }
}
