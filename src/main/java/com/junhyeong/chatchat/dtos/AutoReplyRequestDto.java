package com.junhyeong.chatchat.dtos;

import javax.validation.constraints.NotNull;

public record AutoReplyRequestDto(@NotNull Long chatRoomId) {
}
