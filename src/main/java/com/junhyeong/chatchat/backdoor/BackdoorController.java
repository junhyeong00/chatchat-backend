package com.junhyeong.chatchat.backdoor;

import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.CustomerNotFound;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Name;
import com.junhyeong.chatchat.models.commom.Password;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.models.message.Content;
import com.junhyeong.chatchat.models.message.Message;
import com.junhyeong.chatchat.models.message.MessageType;
import com.junhyeong.chatchat.models.message.Sender;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.message.MessageRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backdoor")
public class BackdoorController {
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private ChatRoomRepository chatRoomRepository;
    private MessageRepository messageRepository;
    private PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public BackdoorController(CustomerRepository customerRepository,
                              CompanyRepository companyRepository,
                              ChatRoomRepository chatRoomRepository,
                              MessageRepository messageRepository,
                              PasswordEncoder passwordEncoder,
                              JdbcTemplate jdbcTemplate) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/customers")
    public String customers() {
        jdbcTemplate.execute("DELETE FROM customer");

        Customer customer1 = new Customer(new Username("customer1"), new Name("고객1"));

        customer1.changePassword(new Password("Password1234!"), passwordEncoder);

        customerRepository.save(customer1);

        return "ok";
    }

    @GetMapping("/companies")
    public String companies() {
        jdbcTemplate.execute("DELETE FROM company");

        Company company1 = new Company(new Username("company1"), new Name("기업1"));

        company1.changePassword(new Password("Password1234!"), passwordEncoder);

        companyRepository.save(company1);

        return "ok";
    }

    @GetMapping("/chatrooms")
    public String chatRooms() {
        jdbcTemplate.execute("DELETE FROM chat_room");
        jdbcTemplate.execute("DELETE FROM message");

        Customer customer1 = customerRepository.findByUsername(new Username("customer1"))
                .orElseThrow(CustomerNotFound::new);

        Company company1 = companyRepository.findByUsername(new Username("company1"))
                .orElseThrow(CompanyNotFound::new);

        ChatRoom chatRoom1 = new ChatRoom(
                customer1.username(),
                company1.username()
        );
        ChatRoom savedChatRoom1 = chatRoomRepository.save(chatRoom1);

        Message message1 = new Message(
                savedChatRoom1.id(),
                new Sender(customer1.id(), customer1.username()),
                new Content("내용1"),
                MessageType.GENERAL);
        message1.read();
        messageRepository.save(message1);

        Message message2 = new Message(
                savedChatRoom1.id(),
                new Sender(customer1.id(), customer1.username()),
                new Content("내용2"),
                MessageType.GENERAL);
        message2.read();
        messageRepository.save(message2);

        return "ok";
    }
}
