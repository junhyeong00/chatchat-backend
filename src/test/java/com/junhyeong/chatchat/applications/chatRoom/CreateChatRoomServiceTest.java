package com.junhyeong.chatchat.applications.chatRoom;

import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CreateChatRoomServiceTest {
    private ChatRoomRepository chatRoomRepository;
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private MessageRepository messageRepository;
    private CreateChatRoomService createChatRoomService;

    @BeforeEach
    void setUp() {
        chatRoomRepository = mock(ChatRoomRepository.class);
        customerRepository = mock(CustomerRepository.class);
        companyRepository = mock(CompanyRepository.class);
        messageRepository = mock(MessageRepository.class);
        createChatRoomService = new CreateChatRoomService(
                chatRoomRepository, customerRepository, companyRepository, messageRepository);
    }

    @Test
    void getChatRoomId() {
        Username customer = new Username("customer");
        Username company = new Username("company");
        Long companyId = 1L;

        given(customerRepository.existsByUsername(customer))
                .willReturn(true);

        given(companyRepository.findById(companyId))
                .willReturn(Optional.of(Company.fake(company)));

        given(chatRoomRepository.findByCustomerAndCompany(customer, company))
                .willReturn(Optional.of(ChatRoom.fake(1L)));

        Long chatRoomId = createChatRoomService.getCharRoomId(customer, companyId);

        assertThat(chatRoomId).isNotNull();
    }

    @Test
    void getChatRoomIdWithUnauthorized() {
        Username invalid = new Username("xxx");
        Long companyId = 1L;

        given(customerRepository.existsByUsername(invalid))
                .willReturn(false);

        assertThrows(Unauthorized.class,
                () -> createChatRoomService.getCharRoomId(invalid, companyId));
    }

    @Test
    void getChatRoomIdWithCompanyNotFound() {
        Username customer = new Username("customer");
        Long companyId = 1L;

        given(customerRepository.existsByUsername(customer))
                .willReturn(true);

        given(companyRepository.findById(companyId))
                .willThrow(CompanyNotFound.class);

        assertThrows(CompanyNotFound.class,
                () -> createChatRoomService.getCharRoomId(customer, companyId));
    }

    @Test
    void getChatRoomIdWithChatRoomNotFound() {
        Username customer = new Username("customer");
        Username company = new Username("company");
        Long companyId = 1L;

        given(customerRepository.existsByUsername(customer))
                .willReturn(true);

        given(companyRepository.findById(companyId))
                .willReturn(Optional.of(Company.fake(company)));

        given(chatRoomRepository.findByCustomerAndCompany(customer, company))
                .willThrow(ChatRoomNotFound.class);

        assertThrows(ChatRoomNotFound.class,
                () -> createChatRoomService.getCharRoomId(customer, companyId));
    }

    @Test
    void create() {
        Username customer = new Username("customer");
        Username company = new Username("company");
        Long companyId = 1L;

        given(companyRepository.findById(companyId))
                .willReturn(Optional.of(Company.fake(company)));

        given(chatRoomRepository.save(any()))
                .willReturn(ChatRoom.fake(1L));

        Long chatRoomId = createChatRoomService.create(customer, companyId);

        assertThat(chatRoomId).isNotNull();
    }

    @Test
    void createWithCompanyNotFound() {
        Username customer = new Username("customer");
        Long companyId = 1L;

        given(companyRepository.findById(companyId))
                .willThrow(CompanyNotFound.class);

        assertThrows(CompanyNotFound.class,
                () -> createChatRoomService.create(customer, companyId));
    }
}
