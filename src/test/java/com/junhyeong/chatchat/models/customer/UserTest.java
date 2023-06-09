package com.junhyeong.chatchat.models.customer;

import com.junhyeong.chatchat.models.commom.Password;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void authenticate() {
        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
        Password password = new Password("Password1234!");

        Customer customer = new Customer();

        customer.changePassword(password, passwordEncoder);

        assertDoesNotThrow(() -> customer.authenticate(password, passwordEncoder));
    }
}
