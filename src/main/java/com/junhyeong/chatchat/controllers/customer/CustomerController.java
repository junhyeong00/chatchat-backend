package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.customer.GetCustomerProfileService;
import com.junhyeong.chatchat.dtos.CompanyProfileDto;
import com.junhyeong.chatchat.dtos.CustomerProfileDto;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final GetCustomerProfileService getCustomerProfileService;

    public CustomerController(GetCustomerProfileService getCustomerProfileService) {
        this.getCustomerProfileService = getCustomerProfileService;
    }

    @GetMapping("me")
    public CustomerProfileDto customerProfile(
            @RequestAttribute Username username
    ) {
        Customer customer = getCustomerProfileService.find(username);

        return customer.toProfileDto();
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }
}
