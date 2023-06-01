package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotBlank;

public record EditCompanyRequestDto(@NotBlank String name,
                                    @NotBlank String description,
                                    String imageUrl) {
}
