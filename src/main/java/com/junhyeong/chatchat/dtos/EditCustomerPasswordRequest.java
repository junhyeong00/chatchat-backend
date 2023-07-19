package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.commom.Password;

public class EditCustomerPasswordRequest {
    private Password password;
    private Password newPassword;
    private Password confirmNewPassword;

    public EditCustomerPasswordRequest(Password password, Password newPassword, Password confirmNewPassword) {
        this.password = password;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public static EditCustomerPasswordRequest of(EditCustomerPasswordRequestDto editCustomerPasswordRequestDto) {
        return new EditCustomerPasswordRequest(
                new Password(editCustomerPasswordRequestDto.password()),
                new Password(editCustomerPasswordRequestDto.newPassword()),
                new Password(editCustomerPasswordRequestDto.confirmNewPassword()));
    }

    public Password getPassword() {
        return password;
    }

    public Password getNewPassword() {
        return newPassword;
    }

    public Password getConfirmNewPassword() {
        return confirmNewPassword;
    }
}
