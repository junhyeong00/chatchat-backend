package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotBlank;

public record LoginRequestDto(@NotBlank String userName,
                              @NotBlank String password) {
}
