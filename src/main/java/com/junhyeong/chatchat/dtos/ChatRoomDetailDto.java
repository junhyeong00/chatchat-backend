package com.junhyeong.chatchat.dtos;

import java.util.List;

public class ChatRoomDetailDto {
    private Long id;
    private Long receiverId;
    private String receiverName;
    private String receiverImageUrl;
    private List<MessageDto> messages;
    private PageDto page;

    public ChatRoomDetailDto(Long id, Long receiverId,
                             String receiverName, String receiverImageUrl,
                             List<MessageDto> messages, PageDto page) {
        this.id = id;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiverImageUrl = receiverImageUrl;
        this.messages = messages;
        this.page = page;
    }

    public Long getId() {
        return id;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverImageUrl() {
        return receiverImageUrl;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public PageDto getPage() {
        return page;
    }
}
