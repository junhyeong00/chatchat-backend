package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.commom.Image;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.message.Content;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatRoomDto {
    private Long id;
    private String receiverName;
    private String receiverImageUrl;
    private String lastMessage;
    private String lastMessageDate;
    private Long unreadMessageCount;

    public ChatRoomDto(Long id, Name receiverName, Image receiverImageUrl, Content lastMessage,
                       LocalDateTime lastMessageDate, Long unreadMessageCount
    ) {
        this.id = id;
        this.receiverName = receiverName.value();
        this.receiverImageUrl = receiverImageUrl.value();
        this.lastMessage = lastMessage.value();
        this.lastMessageDate = lastMessageDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        this.unreadMessageCount = unreadMessageCount;
    }

    public static ChatRoomDto fake() {
        return new ChatRoomDto(1L, new Name("고객"), new Image("이미지"),
                new Content("내용"), LocalDateTime.of(2023, 1, 1, 1, 1), 3L);
    }

    public Long getId() {
        return id;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverImageUrl() {
        return receiverImageUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public Long getUnreadMessageCount() {
        return unreadMessageCount;
    }
}
