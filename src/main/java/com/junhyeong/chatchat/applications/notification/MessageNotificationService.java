package com.junhyeong.chatchat.applications.notification;

import com.junhyeong.chatchat.dtos.ChatRoomDto;
import com.junhyeong.chatchat.dtos.MessageRequest;
import com.junhyeong.chatchat.exceptions.ChatRoomNotFound;
import com.junhyeong.chatchat.exceptions.CompanyNotFound;
import com.junhyeong.chatchat.exceptions.CustomerNotFound;
import com.junhyeong.chatchat.exceptions.UnknownRole;
import com.junhyeong.chatchat.models.chatRoom.ChatRoom;
import com.junhyeong.chatchat.models.commom.Username;
import com.junhyeong.chatchat.models.company.Company;
import com.junhyeong.chatchat.models.customer.Customer;
import com.junhyeong.chatchat.repositories.chatRoom.ChatRoomRepository;
import com.junhyeong.chatchat.repositories.company.CompanyRepository;
import com.junhyeong.chatchat.repositories.customer.CustomerRepository;
import com.junhyeong.chatchat.repositories.sseEmitter.SseEmitterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
public class MessageNotificationService {
    private final SseEmitterRepository sseEmitterRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final ChatRoomRepository chatRoomRepository;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;


    public MessageNotificationService(SseEmitterRepository sseEmitterRepository,
                                      CompanyRepository companyRepository,
                                      CustomerRepository customerRepository,
                                      ChatRoomRepository chatRoomRepository) {
        this.sseEmitterRepository = sseEmitterRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional
    public SseEmitter subscribe(Username username, String userType) {
        Long userId = getUserId(username, userType);

        String id = userId + "_" + System.currentTimeMillis();

        SseEmitter sseEmitter = createEmitter(id);

        sendToClient(sseEmitter, id, "Connected!");

        return sseEmitter;
    }

    private Long getUserId(Username username, String userType) {
        return switch (userType) {
            case "company" -> companyRepository.findByUsername(username)
                    .orElseThrow(CompanyNotFound::new).id();
            case "customer" -> customerRepository.findByUsername(username)
                    .orElseThrow(CustomerNotFound::new).id();
            default -> throw new UnknownRole();
        };
    }

    private void sendToClient(SseEmitter sseEmitter, String id, Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            sseEmitterRepository.deleteById(id);
            sseEmitter.completeWithError(exception);
        }
    }

    private SseEmitter createEmitter(String id) {
        SseEmitter sseEmitter = sseEmitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        sseEmitter.onCompletion(() -> sseEmitterRepository.deleteById(id));
        sseEmitter.onTimeout(() -> sseEmitterRepository.deleteById(id));

        return sseEmitter;
    }

    public void sendNotification(MessageRequest messageRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getChatRoomId())
                .orElseThrow(ChatRoomNotFound::new);

        Customer customer = customerRepository.findByUsername(chatRoom.customer())
                .orElseThrow(CustomerNotFound::new);
        sendChatRoomInformation(String.valueOf(
                customer.id()), customer.username(), "customer", chatRoom.id());

        Company company = companyRepository.findByUsername(chatRoom.company())
                .orElseThrow(CompanyNotFound::new);
        sendChatRoomInformation(String.valueOf(
                company.id()), company.username(), "company", chatRoom.id());
    }

    private void sendChatRoomInformation(String id, Username username, String userType, Long chatRoomId) {
        Map<String, SseEmitter> sseEmitters = sseEmitterRepository.findAllByUserId(id);

        ChatRoomDto chatRoomDto = getChatRoomDto(username, userType, chatRoomId);

        sseEmitters.forEach(
                (key, emitter) -> sendToClient(emitter, key, chatRoomDto)
        );
    }

    private ChatRoomDto getChatRoomDto(Username username, String userType, Long chatRoomId) {
        return switch (userType) {
            case "company" -> chatRoomRepository.findDtoByCompany(username, chatRoomId);
            case "customer" -> chatRoomRepository.findDtoByCustomer(username, chatRoomId);
            default -> throw new UnknownRole();
        };
    }
}
