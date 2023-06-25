package com.junhyeong.chatchat.dtos;

import java.util.List;

public record CompanySummariesDto(List<CompanySummaryDto> companySummaries,
                                  PageDto page) {
}
