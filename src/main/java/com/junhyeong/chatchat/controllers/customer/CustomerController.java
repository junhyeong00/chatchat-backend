package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.customer.EditCustomerService;
import com.junhyeong.chatchat.applications.customer.GetCustomerProfileService;
import com.junhyeong.chatchat.dtos.CustomerProfileDto;
import com.junhyeong.chatchat.dtos.EditCustomerRequest;
import com.junhyeong.chatchat.dtos.EditCustomerRequestDto;
import com.junhyeong.chatchat.exceptions.EditCustomerFailed;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final GetCustomerProfileService getCustomerProfileService;
    private final EditCustomerService editCustomerService;

    public CustomerController(GetCustomerProfileService getCustomerProfileService,
                              EditCustomerService editCustomerService) {
        this.getCustomerProfileService = getCustomerProfileService;
        this.editCustomerService = editCustomerService;
    }

    @GetMapping("me")
    public CustomerProfileDto customerProfile(
            @RequestAttribute Username username
    ) {
        Customer customer = getCustomerProfileService.find(username);

        return customer.toProfileDto();
    }

    @PatchMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(
            @RequestAttribute Username username,
            @Validated @RequestBody EditCustomerRequestDto editCustomerRequestDto
    ) {
        try {
            EditCustomerRequest editCustomerRequest = EditCustomerRequest.of(editCustomerRequestDto);

            editCustomerService.edit(username, editCustomerRequest);
        } catch (Unauthorized e) {
            throw new Unauthorized();
        } catch (Exception e) {
            throw new EditCustomerFailed(e.getMessage());
        }
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EditCustomerFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String editCustomerFailed(Exception e) {
        return e.getMessage();
    }
}
