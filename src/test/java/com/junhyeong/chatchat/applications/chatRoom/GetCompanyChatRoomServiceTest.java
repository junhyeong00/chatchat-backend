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
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class
GetCompanyChatRoomServiceTest {
    private CompanyRepository companyRepository;
    private CustomerRepository customerRepository;
    private ChatRoomRepository chatRoomRepository;
    private MessageRepository messageRepository;
    private GetCompanyChatRoomService getChatRoomService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        customerRepository = mock(CustomerRepository.class);
        chatRoomRepository = mock(ChatRoomRepository.class);
        messageRepository = mock(MessageRepository.class);
        getChatRoomService = new GetCompanyChatRoomService(
                companyRepository, customerRepository, chatRoomRepository, messageRepository);
    }

    @Test
    void chatRoomDetail() {
        Username username = new Username("company123");
        Long chatRoomId = 1L;
        int page = 1;

        given(companyRepository.findByUsername(username))
                .willReturn(Optional.of(Company.fake(username)));

        given(chatRoomRepository.findById(chatRoomId))
                .willReturn(Optional.of(ChatRoom.fake(chatRoomId)));

        Username customerUsername = new Username("customer");
        Customer customer = Customer.fake(customerUsername);

        given(customerRepository.findByUsername(customerUsername))
                .willReturn(Optional.of(customer));

        List<Message> messages = List.of(
                Message.fake(customerUsername, new Content("내용1"))
        );

        given(messageRepository.findAllGeneralMessagesByChatRoomId(any(), any()))
                .willReturn(new PageImpl<>(messages));

        ChatRoomDetailDto found = getChatRoomService.chatRoomDetail(username, chatRoomId, page);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(chatRoomId);
    }
}
