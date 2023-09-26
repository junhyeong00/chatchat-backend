package com.junhyeong.chatchat.applications.autoReply;

import com.junhyeong.chatchat.exceptions.AutoReplyNotFound;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.Unauthorized;
import com.junhyeong.chatchat.models.autoReply.AutoReply;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.models.message.Content;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.models.message.MessageType;
import com.junhyeong.chatchat.models.message.Sender;
import com.junhyeong.chatchat.repositories.autoReply.AutoReplyRepository;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SendAutoReplyService {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final AutoReplyRepository autoReplyRepository;
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public SendAutoReplyService(CustomerRepository customerRepository,
                                CompanyRepository companyRepository,
                                AutoReplyRepository autoReplyRepository,
                                MessageRepository messageRepository,
                                ChatRoomRepository chatRoomRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.autoReplyRepository = autoReplyRepository;
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional
    public void send(Username username, Long autoReplyId, Long chatRoomId) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(Unauthorized::new);

        AutoReply autoReply = autoReplyRepository.findById(autoReplyId)
                .orElseThrow(AutoReplyNotFound::new);

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFound::new);

        Company company = companyRepository.findByUsername(autoReply.companyUsername())
                .orElseThrow(CompanyNotFound::new);

        Message question = new Message(
                chatRoom.id(),
                new Sender(customer.id(), customer.username()),
                new Content(autoReply.question().value()),
                MessageType.AUTO
        );
        question.read();

        Message answer = new Message(
                chatRoom.id(),
                new Sender(company.id(), company.username()),
                new Content(autoReply.answer().value()),
                MessageType.AUTO
        );
        answer.read();

        messageRepository.save(question);
        messageRepository.save(answer);
    }
}
