package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.dtos.EditCustomerPasswordRequest;
import com.junhyeong.chatchat.dtos.EditCustomerRequest;
import com.junhyeong.chatchat.exceptions.AuthenticationFailed;
import com.junhyeong.chatchat.exceptions.NotMatchPassword;
import com.junhyeong.chatchat.exceptions.SameAsPreviousPassword;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Name;
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

class EditCustomerPasswordServiceTest {
    private CustomerRepository customerRepository;
    private PasswordEncoder passwordEncoder;
    private EditCustomerPasswordService editCustomerPasswordService;

    @BeforeEach
    void setup() {
        customerRepository = mock(CustomerRepository.class);
        passwordEncoder = new Argon2PasswordEncoder();
        editCustomerPasswordService = new EditCustomerPasswordService(customerRepository, passwordEncoder);
    }

    @Test
    void edit() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");

        EditCustomerPasswordRequest editCustomerRequest = new EditCustomerPasswordRequest(
                password,
                new Password("newPassword1234!"),
                new Password("newPassword1234!")
        );

        Customer customer = Customer.fake(username);
        customer.changePassword(password, passwordEncoder);

        given(customerRepository.findByUsername(username))
                .willReturn(Optional.of(customer));

        assertDoesNotThrow(() -> editCustomerPasswordService.edit(username, editCustomerRequest));
    }

    @Test
    void editWithUnauthorized() {
        Username invalidUserName = new Username("xxx");
        Password password = new Password("Password1234!");

        EditCustomerPasswordRequest editCustomerRequest = new EditCustomerPasswordRequest(
                password,
                new Password("newPassword1234!"),
                new Password("newPassword1234!")
        );

        given(customerRepository.findByUsername(invalidUserName))
                .willThrow(Unauthorized.class);

        assertThrows(Unauthorized.class,
                () ->  editCustomerPasswordService.edit(invalidUserName, editCustomerRequest));
    }

    @Test
    void editPasswordWithNotMatchPassword() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");

        EditCustomerPasswordRequest editCustomerRequest = new EditCustomerPasswordRequest(
                password,
                new Password("newPassword4321!"),
                new Password("newPassword1234!")
        );

        Customer customer = Customer.fake(username);
        customer.changePassword(password, passwordEncoder);

        given(customerRepository.findByUsername(username))
                .willReturn(Optional.of(customer));

         assertThrows(NotMatchPassword.class,
                () ->  editCustomerPasswordService.edit(username, editCustomerRequest));
    }

    @Test
    void editPasswordWithSameAsPreviousPassword() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");

        EditCustomerPasswordRequest editCustomerRequest = new EditCustomerPasswordRequest(
                password,
                new Password("Password1234!"),
                new Password("Password1234!")
        );

        Customer customer = Customer.fake(username);
        customer.changePassword(password, passwordEncoder);

        given(customerRepository.findByUsername(username))
                .willReturn(Optional.of(customer));

         assertThrows(SameAsPreviousPassword.class,
                () ->  editCustomerPasswordService.edit(username, editCustomerRequest));
    }

    @Test
    void editPasswordWithWrongPassword() {
        Username username = new Username("test123");
        Password password = new Password("Password1234!");
        Password invalidPassword = new Password("Xxxxxx1234!");

        EditCustomerPasswordRequest editCustomerRequest = new EditCustomerPasswordRequest(
                invalidPassword,
                new Password("newPassword1234!"),
                new Password("newPassword1234!")
        );

        Customer customer = Customer.fake(username);
        customer.changePassword(password, passwordEncoder);

        given(customerRepository.findByUsername(username))
                .willReturn(Optional.of(customer));

         assertThrows(AuthenticationFailed.class,
                () ->  editCustomerPasswordService.edit(username, editCustomerRequest));
    }
}
