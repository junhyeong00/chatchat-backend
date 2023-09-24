package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.exceptions.CustomerDeleted;
import com.junhyeong.chatchat.exceptions.CustomerNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCustomerService {
    private final CustomerRepository customerRepository;

    public GetCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Customer find(Username username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(CustomerNotFound::new);

        if (customer.isDeleted()) {
            throw new CustomerDeleted();
        }

        return customer;
    }
}
