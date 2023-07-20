package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotNull;

public record CreateChatRoomRequestDto(@NotNull Long companyId) {
}
