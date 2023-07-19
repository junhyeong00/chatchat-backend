package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotBlank;

public record DeleteCompanyRequestDto(@NotBlank String password) {
}
