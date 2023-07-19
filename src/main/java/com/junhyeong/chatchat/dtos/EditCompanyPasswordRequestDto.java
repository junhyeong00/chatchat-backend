package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotBlank;

public record EditCompanyPasswordRequestDto(@NotBlank String password,
                                            @NotBlank String newPassword,
                                            @NotBlank String confirmNewPassword
                                    ) {
}
