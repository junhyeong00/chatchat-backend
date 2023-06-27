package com.junhyeong.chatchat.applications.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDetailDto;
import com.junhyeong.chatchat.dtos.MessageDto;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.CustomerNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetCustomerChatRoomService {
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public GetCustomerChatRoomService(CompanyRepository companyRepository,
                                      CustomerRepository customerRepository,
                                      ChatRoomRepository chatRoomRepository,
                                      MessageRepository messageRepository) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public ChatRoomDetailDto chatRoomDetail(Username username, Long chatRoomId) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(Unauthorized::new);

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFound::new);

        List<Message> messages = messageRepository.findAllByChatRoomId(chatRoom.id());

        updateMessagesAsReadFromOthers(customer.username(), messages);

        List<MessageDto> messageDtos = messages.stream()
                .map(Message::toDto).toList();

        Company company = companyRepository.findByUsername(chatRoom.company())
                .orElseThrow(CompanyNotFound::new);

        ChatRoomDetailDto chatRoomDetailDto = company.toRoomDetailDto(chatRoom.id(), messageDtos);

        return chatRoomDetailDto;
    }

    private void updateMessagesAsReadFromOthers(Username username, List<Message> messages) {
        messages.stream().forEach(message -> {
                    if (!message.isSender(username) && !message.isRead()) {
                        message.read();
                    }
                }
        );
    }
}
