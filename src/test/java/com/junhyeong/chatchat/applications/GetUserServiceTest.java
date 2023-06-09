package com.junhyeong.chatchat.applications;

import com.junhyeong.chatchat.applications.customer.GetUserService;
import com.junhyeong.chatchat.exceptions.UserNotFound;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetUserServiceTest {
    private CustomerRepository userRepository;
    private GetUserService getUserService;

    @BeforeEach
    void setup() {
        userRepository = mock(CustomerRepository.class);
        getUserService = new GetUserService(userRepository);
    }

    @Test
    void whenUserExists() {
        Username userName = new Username("test123");

        given(userRepository.findByUserName(userName))
                .willReturn(Optional.of(Customer.fake(userName)));

        assertDoesNotThrow(() -> getUserService.find(userName));
    }

    @Test
    void whenUserNotExists() {
        Username userName = new Username("notExists@gmail.com");

        given(userRepository.findByUserName(userName))
                .willReturn(Optional.empty());

        assertThrows(UserNotFound.class,
                () -> getUserService.find(userName));
    }
}
