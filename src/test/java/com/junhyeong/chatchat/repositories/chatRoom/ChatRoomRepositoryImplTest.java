package com.junhyeong.chatchat.repositories.chatRoom;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.models.message.Content;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class ChatRoomRepositoryImplTest {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private Company company1;

    @BeforeEach
    void setUp() {
        company1 = new Company(
                new Username("company1"),
                new Name("기업1")
        );
        companyRepository.save(company1);

        Customer customer1 = new Customer(
                new Username("customer1"),
                new Name("고객1")
        );
        customerRepository.save(customer1);
        Customer customer2 = new Customer(
                new Username("customer2"),
                new Name("고객2")
        );
        customerRepository.save(customer2);

        ChatRoom chatRoom1 = new ChatRoom(
                customer1.userName(),
                company1.userName()
        );
        ChatRoom savedChatRoom1 = chatRoomRepository.save(chatRoom1);

        Message message1 = new Message(
                savedChatRoom1.id(),
                customer1.userName(),
                new Content("내용1")
        );
        message1.setRead();
        messageRepository.save(message1);

        Message message2 = new Message(
                savedChatRoom1.id(),
                company1.userName(),
                new Content("내용2")
        );
        message2.setRead();
        messageRepository.save(message2);

        ChatRoom chatRoom2 = new ChatRoom(
                customer2.userName(),
                company1.userName()
        );
        ChatRoom savedChatRoom2 = chatRoomRepository.save(chatRoom2);

        Message message3 = new Message(
                savedChatRoom2.id(),
                company1.userName(),
                new Content("내용3")
        );
        message3.setRead();
        messageRepository.save(message3);

        Message message4 = new Message(
                savedChatRoom2.id(),
                customer2.userName(),
                new Content("내용4")
        );
        messageRepository.save(message4);

        Message message5 = new Message(
                savedChatRoom2.id(),
                customer2.userName(),
                new Content("내용5")
        );
        messageRepository.save(message5);
    }

    @Test
    void findAllByCompany() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ChatRoomDto> chatRooms = chatRoomRepository.findAllDtoByCompany(company1.userName(), pageable);

        assertThat(chatRooms.get().toList().get(0).getUnreadMessageCount()).isEqualTo(2);
        assertThat(chatRooms.get().toList().get(0).getLastMessage()).isEqualTo("내용5");
        assertThat(chatRooms.get().toList().get(1).getUnreadMessageCount()).isEqualTo(0);
        assertThat(chatRooms.get().toList().get(1).getLastMessage()).isEqualTo("내용2");
    }
}
