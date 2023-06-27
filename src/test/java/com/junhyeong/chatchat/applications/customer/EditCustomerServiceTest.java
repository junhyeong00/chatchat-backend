package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.applications.company.EditCompanyService;
import com.junhyeong.chatchat.dtos.EditCompanyRequest;
import com.junhyeong.chatchat.dtos.EditCustomerRequest;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class EditCustomerServiceTest {
    private CustomerRepository customerRepository;
    private EditCustomerService editCustomerService;

    @BeforeEach
    void setUp() {
        customerRepository =mock(CustomerRepository.class);
        editCustomerService = new EditCustomerService(customerRepository);
    }

    @Test
    void edit() {
        Username username = new Username("customer123");

        EditCustomerRequest editCustomerRequest = EditCustomerRequest.fake(new Name("고객"));

        given(customerRepository.findByUsername(username))
                .willReturn(Optional.of(Customer.fake(username)));

        assertDoesNotThrow(() -> editCustomerService.edit(username, editCustomerRequest));
    }

    @Test
    void editWithCompanyNotFound() {
        Username invalidUserName = new Username("xxx");

        EditCustomerRequest editCustomerRequest = EditCustomerRequest.fake(new Name("고객"));

        given(customerRepository.findByUsername(invalidUserName))
                .willThrow(Unauthorized.class);

        assertThrows(Unauthorized.class,
                () ->  editCustomerService.edit(invalidUserName, editCustomerRequest));
    }
}
