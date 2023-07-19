package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.dtos.EditCustomerPasswordRequest;
import com.junhyeong.chatchat.exceptions.NotMatchPassword;
import com.junhyeong.chatchat.exceptions.SameAsPreviousPassword;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditCustomerPasswordService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public EditCustomerPasswordService(CustomerRepository customerRepository,
                                       PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void edit(Username username, EditCustomerPasswordRequest editCustomerRequest) {
        if (!editCustomerRequest.getNewPassword().equals(editCustomerRequest.getConfirmNewPassword())) {
            throw new NotMatchPassword();
        }

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(Unauthorized::new);

        customer.authenticate(editCustomerRequest.getPassword(), passwordEncoder);

        if (editCustomerRequest.getPassword().equals(editCustomerRequest.getNewPassword())) {
            throw new SameAsPreviousPassword();
        }

        customer.changePassword(editCustomerRequest.getNewPassword(), passwordEncoder);
    }
}
