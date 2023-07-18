package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;

public class CreateCompanyRequest {
    private Username username;
    private Password password;
    private Password confirmPassword;
    private Name name;

    public CreateCompanyRequest(Username username, Password password, Password confirmPassword, Name name) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
    }

    public static CreateCompanyRequest of(CreateCompanyRequestDto createCompanyRequestDto) {
        return new CreateCompanyRequest(
                new Username(createCompanyRequestDto.username()),
                new Password(createCompanyRequestDto.password()),
                new Password(createCompanyRequestDto.confirmPassword()),
                new Name(createCompanyRequestDto.name()));
    }

    public static CreateCompanyRequest fake(Username username, Name name) {
        return new CreateCompanyRequest(
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
