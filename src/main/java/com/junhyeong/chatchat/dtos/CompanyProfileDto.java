package com.junhyeong.chatchat.dtos;

public record CompanyProfileDto(Long companyId,
                                String name,
                                String description,
                                String imageUrl,
                                boolean profileVisibility
                                ) {
}
