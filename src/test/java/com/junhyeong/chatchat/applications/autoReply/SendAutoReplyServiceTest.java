package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.exceptions.AutoReplyNotFound;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.message.Content;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.repositories.autoReply.AutoReplyRepository;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SendAutoReplyServiceTest {
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private AutoReplyRepository autoReplyRepository;
    private MessageRepository messageRepository;
    private ChatRoomRepository chatRoomRepository;
    private SendAutoReplyService sendAutoReplyService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        companyRepository = mock(CompanyRepository.class);
        autoReplyRepository = mock(AutoReplyRepository.class);
        messageRepository = mock(MessageRepository.class);
        chatRoomRepository = mock(ChatRoomRepository.class);
        sendAutoReplyService = new SendAutoReplyService(
                customerRepository,companyRepository, autoReplyRepository,
                messageRepository, chatRoomRepository);
    }

    @Test
    void send() {
        Username username = new Username("customer");
        Username company = new Username("company");
        Long autoReplyId = 1L;
        Long chatRoomId = 1L;

        given(customerRepository.existsByUsername(username))
                .willReturn(true);

        given(companyRepository.findByUsername(company))
                .willReturn(Optional.of(Company.fake(company)));

        given(autoReplyRepository.findById(autoReplyId))
                .willReturn(Optional.of(AutoReply.fake(company)));

        given(chatRoomRepository.findById(chatRoomId))
                .willReturn(Optional.of(ChatRoom.fake(chatRoomId)));

        given(messageRepository.save(any()))
                .willReturn(Message.fake(company, new Content("질문")));

        assertDoesNotThrow(() -> sendAutoReplyService.send(username, autoReplyId, chatRoomId));
    }

    @Test
    void sendWithUnauthorized() {
        Username invalidUsername = new Username("xxx");
        Long autoReplyId = 1L;
        Long chatRoomId = 1L;

        given(customerRepository.existsByUsername(invalidUsername))
                .willReturn(false);

        assertThrows(Unauthorized.class,
                () -> sendAutoReplyService.send(invalidUsername, autoReplyId, chatRoomId));
    }

    @Test
    void sendWithCompanyNotFound() {
        Username username = new Username("customer");
        Username company = new Username("xxx");
        Long autoReplyId = 1L;
        Long chatRoomId = 1L;

        given(customerRepository.existsByUsername(username))
                .willReturn(true);

        given(autoReplyRepository.findById(autoReplyId))
                .willReturn(Optional.of(AutoReply.fake(company)));

        given(chatRoomRepository.findById(chatRoomId))
                .willReturn(Optional.of(ChatRoom.fake(chatRoomId)));

        given(companyRepository.findByUsername(company))
                .willThrow(CompanyNotFound.class);

        assertThrows(CompanyNotFound.class,
                () -> sendAutoReplyService.send(username, autoReplyId, chatRoomId));
    }

    @Test
    void sendWithAutoReplyNotFound() {
        Username username = new Username("customer");
        Username company = new Username("company");
        Long autoReplyId = 999L;
        Long chatRoomId = 1L;

        given(customerRepository.existsByUsername(username))
                .willReturn(true);

        given(autoReplyRepository.findById(autoReplyId))
                .willThrow(AutoReplyNotFound.class);

        given(chatRoomRepository.findById(chatRoomId))
                .willReturn(Optional.of(ChatRoom.fake(chatRoomId)));

        assertThrows(AutoReplyNotFound.class,
                () -> sendAutoReplyService.send(username, autoReplyId, chatRoomId));
    }

    @Test
    void sendWithChatRoomNotFound() {
        Username username = new Username("customer");
        Username company = new Username("company");
        Long autoReplyId = 1L;
        Long chatRoomId = 999L;

        given(customerRepository.existsByUsername(username))
                .willReturn(true);

        given(autoReplyRepository.findById(autoReplyId))
                .willReturn(Optional.of(AutoReply.fake(company)));

        given(chatRoomRepository.findById(chatRoomId))
                .willThrow(ChatRoomNotFound.class);

        assertThrows(ChatRoomNotFound.class,
                () -> sendAutoReplyService.send(username, autoReplyId, chatRoomId));
    }
}
