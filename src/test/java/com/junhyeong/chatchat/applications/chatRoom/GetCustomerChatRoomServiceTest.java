package com.junhyeong.chatchat.applications.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDetailDto;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.models.message.Content;
import com.junhyeong.chatchat.models.message.Message;
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

class GetCustomerChatRoomServiceTest {
    private CompanyRepository companyRepository;
    private CustomerRepository customerRepository;
    private ChatRoomRepository chatRoomRepository;
    private MessageRepository messageRepository;
    private GetCustomerChatRoomService getChatRoomService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        customerRepository = mock(CustomerRepository.class);
        chatRoomRepository = mock(ChatRoomRepository.class);
        messageRepository = mock(MessageRepository.class);
        getChatRoomService = new GetCustomerChatRoomService(
                companyRepository, customerRepository, chatRoomRepository, messageRepository);
    }

    @Test
    void chatRoomDetail() {
        Username username = new Username("customer123");
        Long chatRoomId = 1L;

        given(customerRepository.findByUsername(username))
                .willReturn(Optional.of(Customer.fake(username)));

        given(chatRoomRepository.findById(chatRoomId))
                .willReturn(Optional.of(ChatRoom.fake(chatRoomId)));

        Username companyUsername = new Username("company");
        Company company = Company.fake(companyUsername);

        given(companyRepository.findByUsername(companyUsername))
                .willReturn(Optional.of(company));

        List<Message> messages = List.of(
                Message.fake(companyUsername, new Content("내용1"))
        );

        given(messageRepository.findAllByChatRoomId(chatRoomId))
                .willReturn(messages);

        ChatRoomDetailDto found = getChatRoomService.chatRoomDetail(username, chatRoomId);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(chatRoomId);
    }
}
