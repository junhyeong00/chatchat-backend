package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.dtos.EditCustomerRequest;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditCustomerService {
    private final CustomerRepository customerRepository;

    public EditCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void edit(Username username, EditCustomerRequest editCustomerRequest) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(Unauthorized::new);

        customer.edit(
                editCustomerRequest.getName(),
                editCustomerRequest.getProfileImage()
        );
    }
}
