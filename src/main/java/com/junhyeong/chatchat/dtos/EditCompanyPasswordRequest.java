package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.commom.Password;

public class EditCompanyPasswordRequest {
    private Password password;
    private Password newPassword;
    private Password confirmNewPassword;

    public EditCompanyPasswordRequest(Password password, Password newPassword, Password confirmNewPassword) {
        this.password = password;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public static EditCompanyPasswordRequest of(EditCompanyPasswordRequestDto editCompanyPasswordRequestDto) {
        return new EditCompanyPasswordRequest(
                new Password(editCompanyPasswordRequestDto.password()),
                new Password(editCompanyPasswordRequestDto.newPassword()),
                new Password(editCompanyPasswordRequestDto.confirmNewPassword()));
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
