package com.junhyeong.chatchat.dtos;


import javax.validation.constraints.NotBlank;

public record CreateAutoReplyRequestDto(
        @NotBlank String question,
        @NotBlank String answer
) {
}
