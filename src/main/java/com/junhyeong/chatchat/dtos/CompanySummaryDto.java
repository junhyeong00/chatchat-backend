package com.junhyeong.chatchat.dtos;

public record CompanySummaryDto(Long id,
                                String name,
                                String description,
                                String imageUrl) {
    public static CompanySummaryDto fake() {
        return new CompanySummaryDto(1L, "악덕기업", "악덕기업이다", "이미지");
    }
}
