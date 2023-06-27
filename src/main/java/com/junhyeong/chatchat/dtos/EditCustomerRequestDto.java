package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record EditCustomerRequestDto(@NotBlank String name,
                                     String imageUrl
                                    ) {
}
