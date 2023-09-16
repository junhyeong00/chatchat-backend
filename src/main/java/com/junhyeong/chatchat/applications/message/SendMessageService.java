package com.junhyeong.chatchat.applications.message;

import com.junhyeong.chatchat.dtos.MessageRequest;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.CustomerNotFound;
import com.junhyeong.chatchat.exceptions.UnknownRole;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.models.message.MessageType;
import com.junhyeong.chatchat.models.message.Sender;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import com.junhyeong.chatchat.repositories.session.SessionRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class SendMessageService {
    private final SimpMessagingTemplate messagingTemplate;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final MessageRepository messageRepository;
    private final SessionRepository sessionRepository;

    public SendMessageService(SimpMessagingTemplate messagingTemplate,
                              CustomerRepository customerRepository,
                              CompanyRepository companyRepository,
                              MessageRepository messageRepository,
                              SessionRepository sessionRepository) {
        this.messagingTemplate = messagingTemplate;
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.messageRepository = messageRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public void sendMessage(MessageRequest messageRequest) {
        Long senderId = messageRequest.getSenderId();

        Username username = getUsername(messageRequest, senderId);

        Message message = new Message(
                messageRequest.getChatRoomId(),
                new Sender(senderId, username),
                messageRequest.getContent(),
                MessageType.GENERAL
        );

        Message saved = messageRepository.save(message);

        messagingTemplate.convertAndSend(
                "/sub/chatrooms/" + saved.chatRoomId(),
                saved.toDto()
        );

        markConnectedUsersMessagesAsRead(username, message);
    }

    private void markConnectedUsersMessagesAsRead(Username username, Message message) {
        Set<String> sessionIds = sessionRepository.getSessionIdByChatRoomId(message.chatRoomId());

        for (String sessionId : sessionIds) {
            Username connectedUsername = sessionRepository.getUsernameBySessionId(sessionId);

            if (!username.equals(connectedUsername)) {
                message.read();
                break;
            }
        }
    }

    private Username getUsername(MessageRequest messageRequest, Long senderId) {
        return switch (messageRequest.getRole()) {
            case "company" -> companyRepository.findById(senderId)
                    .orElseThrow(CompanyNotFound::new).username();
            case "customer" -> customerRepository.findById(senderId)
                    .orElseThrow(CustomerNotFound::new).username();
            default -> throw new UnknownRole();
        };
    }
}
