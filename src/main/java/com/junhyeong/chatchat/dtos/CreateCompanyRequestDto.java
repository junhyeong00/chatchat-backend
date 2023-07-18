package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotBlank;

public record CreateCompanyRequestDto(@NotBlank String username,
                                      @NotBlank String password,
                                      @NotBlank String confirmPassword,
                                      @NotBlank String name
                                    ) {
}
