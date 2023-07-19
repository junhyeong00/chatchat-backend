package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotBlank;

public record EditCustomerPasswordRequestDto(@NotBlank String password,
                                             @NotBlank String newPassword,
                                             @NotBlank String confirmNewPassword
                                    ) {
}
