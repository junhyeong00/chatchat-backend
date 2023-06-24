package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotBlank;

public record LoginRequestDto(@NotBlank String username,
                              @NotBlank String password) {
}
