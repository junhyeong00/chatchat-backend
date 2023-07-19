package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.exceptions.AuthenticationFailed;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DeleteCustomerServiceTest {
    private CustomerRepository customerRepository;
    private PasswordEncoder passwordEncoder;
    private DeleteCustomerService deleteCustomerService;

    @BeforeEach
    void setup() {
        customerRepository = mock(CustomerRepository.class);
        passwordEncoder = new Argon2PasswordEncoder();
        deleteCustomerService = new DeleteCustomerService(customerRepository, passwordEncoder);
    }

    @Test
    void delete() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");

        Customer customer = Customer.fake(username);
        customer.changePassword(password, passwordEncoder);

        given(customerRepository.findByUsername(username))
                .willReturn(Optional.of(customer));

        assertDoesNotThrow(() -> deleteCustomerService.delete(username, password));
    }

    @Test
    void deleteWithUnauthorized() {
        Username invalidUserName = new Username("xxx");
        Password password = new Password("Password1234!");

        given(customerRepository.findByUsername(invalidUserName))
                .willThrow(Unauthorized.class);

        assertThrows(Unauthorized.class,
                () -> deleteCustomerService.delete(invalidUserName, password));
    }

    @Test
    void deletePasswordWithWrongPassword() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");
        Password invalidPassword = new Password("Xxxxxx1234!");

        Customer customer = Customer.fake(username);
        customer.changePassword(password, passwordEncoder);

        given(customerRepository.findByUsername(username))
                .willReturn(Optional.of(customer));

        assertThrows(AuthenticationFailed.class,
                () -> deleteCustomerService.delete(username, invalidPassword));
    }
}
