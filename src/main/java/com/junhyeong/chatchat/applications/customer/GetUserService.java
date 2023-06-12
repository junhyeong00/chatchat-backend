package com.junhyeong.chatchat.applications.customer;

import com.junhyeong.chatchat.exceptions.UserNotFound;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GetUserService {
    private final CustomerRepository userRepository;

    public GetUserService(CustomerRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Customer find(Username userName) {
        Customer customer = userRepository.findByUsername(userName)
                .orElseThrow(UserNotFound::new);

        return customer;
    }
}
