package com.junhyeong.chatchat.controllers.customer;

import com.junhyeong.chatchat.applications.customer.CreateCustomerService;
import com.junhyeong.chatchat.applications.customer.DeleteCustomerService;
import com.junhyeong.chatchat.applications.customer.EditCustomerPasswordService;
import com.junhyeong.chatchat.applications.customer.EditCustomerService;
import com.junhyeong.chatchat.applications.customer.GetCustomerProfileService;
import com.junhyeong.chatchat.dtos.CreateCustomerRequest;
import com.junhyeong.chatchat.dtos.CreateCustomerRequestDto;
import com.junhyeong.chatchat.dtos.CustomerProfileDto;
import com.junhyeong.chatchat.dtos.DeleteCustomerRequestDto;
import com.junhyeong.chatchat.dtos.EditCustomerPasswordRequest;
import com.junhyeong.chatchat.dtos.EditCustomerPasswordRequestDto;
import com.junhyeong.chatchat.dtos.EditCustomerRequest;
import com.junhyeong.chatchat.dtos.EditCustomerRequestDto;
import com.junhyeong.chatchat.exceptions.AuthenticationFailed;
import com.junhyeong.chatchat.exceptions.CreateCustomerFailed;
import com.junhyeong.chatchat.exceptions.EditCustomerFailed;
import com.junhyeong.chatchat.exceptions.EditCustomerPasswordFailed;
import com.junhyeong.chatchat.exceptions.SameAsPreviousPassword;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.exceptions.UsernameAlreadyInUse;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final GetCustomerProfileService getCustomerProfileService;
    private final CreateCustomerService createCustomerService;
    private final EditCustomerService editCustomerService;
    private final EditCustomerPasswordService editCustomerPasswordService;
    private final DeleteCustomerService deleteCustomerService;

    public CustomerController(GetCustomerProfileService getCustomerProfileService,
                              CreateCustomerService createCustomerService,
                              EditCustomerService editCustomerService,
                              EditCustomerPasswordService editCustomerPasswordService,
                              DeleteCustomerService deleteCustomerService) {
        this.getCustomerProfileService = getCustomerProfileService;
        this.createCustomerService = createCustomerService;
        this.editCustomerService = editCustomerService;
        this.editCustomerPasswordService = editCustomerPasswordService;
        this.deleteCustomerService = deleteCustomerService;
    }

    @GetMapping("me")
    public CustomerProfileDto customerProfile(
            @RequestAttribute Username username
    ) {
        Customer customer = getCustomerProfileService.find(username);

        return customer.toProfileDto();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(
            @Validated @RequestBody CreateCustomerRequestDto createCustomerRequestDto
    ) {
        try {
            CreateCustomerRequest createCustomerRequest = CreateCustomerRequest.of(createCustomerRequestDto);

            createCustomerService.create(createCustomerRequest);
        } catch (UsernameAlreadyInUse e) {
            throw new UsernameAlreadyInUse();
        } catch (Exception e) {
            throw new CreateCustomerFailed(e.getMessage());
        }
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

    @PatchMapping("me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editPassword(
            @RequestAttribute Username username,
            @Validated @RequestBody EditCustomerPasswordRequestDto editCustomerPasswordRequestDto
    ) {
        try {
            EditCustomerPasswordRequest editCustomerPasswordRequest
                    = EditCustomerPasswordRequest.of(editCustomerPasswordRequestDto);

            editCustomerPasswordService.edit(username, editCustomerPasswordRequest);
        } catch (Unauthorized e) {
            throw new Unauthorized();
        } catch (AuthenticationFailed e) {
            throw new AuthenticationFailed();
        } catch (SameAsPreviousPassword e) {
            throw new SameAsPreviousPassword();
        } catch (Exception e) {
            throw new EditCustomerPasswordFailed(e.getMessage());
        }
    }

    @DeleteMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void secession(
            @RequestAttribute Username username,
            @Validated @RequestBody DeleteCustomerRequestDto deleteCustomerRequestDto
    ) {
        Password password = new Password(deleteCustomerRequestDto.password());

        deleteCustomerService.delete(username, password);
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(UsernameAlreadyInUse.class)
    public String usernameAlreadyInUse(Exception e, HttpServletResponse response) {
        response.setStatus(480);

        return e.getMessage();
    }

    @ExceptionHandler(AuthenticationFailed.class)
    public String authenticationFailed(Exception e, HttpServletResponse response) {
        response.setStatus(481);

        return e.getMessage();
    }

    @ExceptionHandler(SameAsPreviousPassword.class)
    public String sameAsPreviousPassword(Exception e, HttpServletResponse response) {
        response.setStatus(482);

        return e.getMessage();
    }

    @ExceptionHandler(CreateCustomerFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String createCustomerFailed(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EditCustomerFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String editCustomerFailed(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(EditCustomerPasswordFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String editCustomerPasswordFailed(Exception e) {
        return e.getMessage();
    }
}
