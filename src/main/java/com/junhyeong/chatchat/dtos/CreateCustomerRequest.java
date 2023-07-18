package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;

public class CreateCustomerRequest {
    private Username username;
    private Password password;
    private Password confirmPassword;
    private Name name;

    public CreateCustomerRequest(Username username, Password password, Password confirmPassword, Name name) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
    }

    public static CreateCustomerRequest of(CreateCustomerRequestDto createCustomerRequestDto) {
        return new CreateCustomerRequest(
                new Username(createCustomerRequestDto.username()),
                new Password(createCustomerRequestDto.password()),
                new Password(createCustomerRequestDto.confirmPassword()),
                new Name(createCustomerRequestDto.name()));
    }

    public static CreateCustomerRequest fake(Username username, Name name) {
        return new CreateCustomerRequest(
                username,
                new Password("Password1234!"),
                new Password("Password1234!"),
                name
        );
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public Password getConfirmPassword() {
        return confirmPassword;
    }

    public Name getName() {
        return name;
    }
}
