package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.applications.company.GetCompanyProfileService;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetCustomerProfileServiceTest {
    private CustomerRepository customerRepository;
    private GetCustomerProfileService getCustomerProfileService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        getCustomerProfileService = new GetCustomerProfileService(customerRepository);
    }

    @Test
    void find() {
        Username userName = new Username("customer123");

        given(customerRepository.findByUsername(userName))
                .willReturn(Optional.of(Customer.fake(userName)));

        assertDoesNotThrow(() -> getCustomerProfileService.find(userName));
    }

    @Test
    void findWithCompanyNotFound() {
        Username invalidUserName = new Username("xxx");

        given(customerRepository.findByUsername(invalidUserName))
                .willThrow(Unauthorized.class);

        assertThrows(Unauthorized.class,
                () ->  getCustomerProfileService.find(invalidUserName));
    }
}
