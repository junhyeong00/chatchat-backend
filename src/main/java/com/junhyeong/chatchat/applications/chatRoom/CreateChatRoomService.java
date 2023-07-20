package com.junhyeong.chatchat.applications.chatRoom;

import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.message.Content;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.models.message.MessageType;
import com.junhyeong.chatchat.models.message.Sender;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final MessageRepository messageRepository;

    public CreateChatRoomService(ChatRoomRepository chatRoomRepository,
                                 CustomerRepository customerRepository,
                                 CompanyRepository companyRepository,
                                 MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    public Long getCharRoomId(Username username, Long companyId) {
        if (!customerRepository.existsByUsername(username)) {
            throw new Unauthorized();
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(CompanyNotFound::new);

        ChatRoom chatRoom = chatRoomRepository.findByCustomerAndCompany(username, company.username())
                .orElseThrow(ChatRoomNotFound::new);

        return chatRoom.id();
    }

    @Transactional
    public Long create(Username username, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(CompanyNotFound::new);

        ChatRoom chatRoom = new ChatRoom(username, company.username());

        ChatRoom saved = chatRoomRepository.save(chatRoom);

        sendGuideMessage(chatRoom.id(), company);

        return saved.id();
    }

    private void sendGuideMessage(Long chatRoomId, Company company) {
        Message message = new Message(
                chatRoomId,
                new Sender(company.id(), company.username()),
                Content.guideMessageOf(company.name()),
                MessageType.AUTO
        );

        messageRepository.save(message);
    }
}
