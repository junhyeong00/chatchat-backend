package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record EditCompanyRequestDto(@NotBlank String name,
                                    String description,
                                    String imageUrl,
                                    @NotNull boolean profileVisibility
                                    ) {
}
