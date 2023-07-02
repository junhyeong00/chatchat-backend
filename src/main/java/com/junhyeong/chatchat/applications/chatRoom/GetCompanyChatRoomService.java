package com.junhyeong.chatchat.applications.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDetailDto;
import com.junhyeong.chatchat.dtos.MessageDto;
import com.junhyeong.chatchat.dtos.PageDto;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetCompanyChatRoomService {
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public GetCompanyChatRoomService(CompanyRepository companyRepository,
                                     CustomerRepository customerRepository,
                                     ChatRoomRepository chatRoomRepository,
                                     MessageRepository messageRepository) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    public ChatRoomDetailDto chatRoomDetail(Username username, Long chatRoomId, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 20);

        Company company = companyRepository.findByUsername(username)
                .orElseThrow(Unauthorized::new);

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFound::new);

        List<Message> all = messageRepository.findAllGeneralMessagesByChatRoomId(chatRoom.id());

        Page<Message> found = messageRepository.findAllGeneralMessagesByChatRoomId(chatRoom.id(), pageable);

        PageDto pageDto = new PageDto(page, found.getTotalPages());

        List<Message> messages = found.stream().toList();

        updateMessagesAsReadFromOthers(company.username(), all);

        List<MessageDto> messageDtos = messages.stream()
                .map(Message::toDto).toList();

        Customer customer = customerRepository.findByUsername(chatRoom.customer())
                .orElseThrow(CustomerNotFound::new);

        ChatRoomDetailDto chatRoomDetailDto = customer.toRoomDetailDto(chatRoom.id(), messageDtos, pageDto);

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
