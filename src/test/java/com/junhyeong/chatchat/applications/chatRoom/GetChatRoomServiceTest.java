package com.junhyeong.chatchat.applications.chatRoom;

import com.junhyeong.chatchat.applications.chatroom.GetChatRoomService;
import com.junhyeong.chatchat.dtos.ChatRoomDetailDto;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.models.message.Content;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.models.message.MessageType;
import com.junhyeong.chatchat.models.message.Sender;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetChatRoomServiceTest {
    private CustomerRepository customerRepository;
    private ChatRoomRepository chatRoomRepository;
    private MessageRepository messageRepository;
    private GetChatRoomService getChatRoomService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        chatRoomRepository = mock(ChatRoomRepository.class);
        messageRepository = mock(MessageRepository.class);
        getChatRoomService = new GetChatRoomService(
                 customerRepository, chatRoomRepository, messageRepository);
    }

    @Test
    void chatRoomDetail() {
        Username username = new Username("company123");
        Long chatRoomId = 1L;

        given(chatRoomRepository.findById(chatRoomId))
                .willReturn(Optional.of(ChatRoom.fake(chatRoomId)));

        Username customerUsername = new Username("customer");
        Customer customer = Customer.fake(customerUsername);

        given(customerRepository.findByUsername(customerUsername))
                .willReturn(Optional.of(customer));

        List<Message> messages = List.of(
                Message.fake(customerUsername, new Content("내용1"))
        );

        given(messageRepository.findAllGeneralMessagesByChatRoomId(chatRoomId))
                .willReturn(messages);

        ChatRoomDetailDto found = getChatRoomService.chatRoomDetail(username, chatRoomId);

        assertThat(found).isNotNull();
        assertThat(found.getChatRoomId()).isEqualTo(chatRoomId);
    }
}
