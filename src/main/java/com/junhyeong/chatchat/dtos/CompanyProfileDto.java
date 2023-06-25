package com.junhyeong.chatchat.dtos;

public record CompanyProfileDto(Long id,
                                String name,
                                String description,
                                String imageUrl,
                                boolean profileVisibility
                                ) {
}
