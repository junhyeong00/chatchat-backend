package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotBlank;

public record DeleteCustomerRequestDto(@NotBlank String password) {
}
