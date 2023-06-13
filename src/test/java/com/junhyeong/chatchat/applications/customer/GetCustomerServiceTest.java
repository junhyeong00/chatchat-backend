package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.exceptions.CustomerNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetCustomerServiceTest {
    private CustomerRepository customerRepository;
    private GetCustomerService getCustomerService;

    @BeforeEach
    void setup() {
        customerRepository = mock(CustomerRepository.class);
        getCustomerService = new GetCustomerService(customerRepository);
    }

    @Test
    void whenCustomerExists() {
        Username userName = new Username("test123");

        given(customerRepository.findByUsername(userName))
                .willReturn(Optional.of(Customer.fake(userName)));

        assertDoesNotThrow(() -> getCustomerService.find(userName));
    }

    @Test
    void whenCustomerNotExists() {
        Username userName = new Username("notExists@gmail.com");

        given(customerRepository.findByUsername(userName))
                .willReturn(Optional.empty());

        assertThrows(CustomerNotFound.class,
                () -> getCustomerService.find(userName));
    }
}
