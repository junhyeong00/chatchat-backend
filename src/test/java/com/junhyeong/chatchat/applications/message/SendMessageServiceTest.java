package com.junhyeong.chatchat.applications.message;

import com.junhyeong.chatchat.dtos.MessageDto;
import com.junhyeong.chatchat.dtos.MessageRequest;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.message.Content;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import com.junhyeong.chatchat.repositories.session.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class SendMessageServiceTest {
    @SpyBean
    private SimpMessagingTemplate messagingTemplate;

    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private MessageRepository messageRepository;
    private SendMessageService sendMessageService;
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        companyRepository = mock(CompanyRepository.class);
        messageRepository = mock(MessageRepository.class);
        sessionRepository = mock(SessionRepository.class);

        sendMessageService = new SendMessageService(
                messagingTemplate,
                customerRepository,
                companyRepository,
                messageRepository,
                sessionRepository);
    }

    @Test
    void sendMessage() {
        Content content = new Content("안녕하세요");

        Username username = new Username("company123");

        Message message = Message.fake(username, content);

        Company company = Company.fake(username);

        given(companyRepository.findById(company.id()))
                .willReturn(Optional.of(company));

        given(messageRepository.save(any()))
                .willReturn(message);

        String role = "company";

        MessageRequest messageRequest = MessageRequest.fake(content, role);

        sendMessageService.sendMessage(messageRequest);

        verify(messagingTemplate).convertAndSend(
                eq("/sub/chatrooms/" + messageRequest.getChatRoomId()),
                any(MessageDto.class)
        );
    }
}
