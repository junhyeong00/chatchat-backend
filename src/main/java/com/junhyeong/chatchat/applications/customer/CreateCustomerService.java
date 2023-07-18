package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.dtos.CreateCustomerRequest;
import com.junhyeong.chatchat.exceptions.NotMatchPassword;
import com.junhyeong.chatchat.exceptions.UsernameAlreadyInUse;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCustomerService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateCustomerService(CustomerRepository customerRepository,
                                 CompanyRepository companyRepository,
                                 PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(CreateCustomerRequest createCustomerRequest) {
        if (!createCustomerRequest.getPassword().equals(createCustomerRequest.getConfirmPassword())) {
            throw new NotMatchPassword();
        }

        if (customerRepository.existsByUsername(createCustomerRequest.getUsername()) ||
                companyRepository.existsByUsername(createCustomerRequest.getUsername())) {
            throw new UsernameAlreadyInUse();
        }

        Customer customer = new Customer(
                createCustomerRequest.getUsername(),
                createCustomerRequest.getName()
        );

        customer.changePassword(createCustomerRequest.getPassword(), passwordEncoder);

        customerRepository.save(customer);
    }
}
