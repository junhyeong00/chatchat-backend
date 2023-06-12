package com.junhyeong.chatchat.dtos;

import com.junhyeong.chatchat.models.commom.Image;
import com.junhyeong.chatchat.models.commom.Name;

import java.util.List;

public class ChatRoomDetailDto {
    private Long chatRoomId;
    private Long receiverId;
    private String receiverName;
    private String receiverImageUrl;
    private List<MessageDto> messages;

    public ChatRoomDetailDto(Long chatRoomId, Long receiverId,
                             String receiverName, String receiverImageUrl,
                             List<MessageDto> messages) {
        this.chatRoomId = chatRoomId;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiverImageUrl = receiverImageUrl;
        this.messages = messages;
    }

    public Long getChatRoomId() {
        return chatRoomId;
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
}
