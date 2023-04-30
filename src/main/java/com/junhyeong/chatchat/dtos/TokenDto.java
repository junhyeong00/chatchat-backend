package com.junhyeong.chatchat.dtos;

public record TokenDto(String accessToken,
                      String refreshToken
                      ) {
    public static TokenDto fake() {
        return new TokenDto("a.a.a", "b.b.b");
    }
}
