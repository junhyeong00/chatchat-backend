package com.junhyeong.chatchat.applications.message;

import com.junhyeong.chatchat.dtos.MessageRequest;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.CustomerNotFound;
import com.junhyeong.chatchat.exceptions.UnknownRole;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SendMessageService {
    private final SimpMessagingTemplate messagingTemplate;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final MessageRepository messageRepository;

    public SendMessageService(SimpMessagingTemplate messagingTemplate,
                              CustomerRepository customerRepository,
                              CompanyRepository companyRepository,
                              MessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void sendMessage(MessageRequest messageRequest) {

        Username username = switch (messageRequest.getRole()) {
            case "company" -> companyRepository.findById(messageRequest.getUserId())
                    .orElseThrow(CompanyNotFound::new).userName();
            case "customer" -> customerRepository.findById(messageRequest.getUserId())
                    .orElseThrow(CustomerNotFound::new).userName();
            default -> throw new UnknownRole();
        };

        Message message = new Message(
                messageRequest.getChatRoomId(),
                username,
                messageRequest.getContent(),
                messageRequest.getType()
        );

        Message saved = messageRepository.save(message);

        messagingTemplate.convertAndSend(
                "/sub/chatrooms/" + saved.chatroomId(),
                saved.toDto(messageRequest.getUserId())
        );
    }
}
