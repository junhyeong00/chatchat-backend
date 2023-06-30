package com.junhyeong.chatchat.dtos;


import javax.validation.constraints.NotBlank;

public record EditAutoReplyRequestDto(
        @NotBlank String question,
        @NotBlank String answer
) {
}
