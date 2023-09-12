package com.junhyeong.chatchat.applications.notification;

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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

@Service
public class MessageNotificationService {
    private static final Logger log = LoggerFactory.getLogger(MessageNotificationService.class);
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
            log.error("[SSE] - Error occurred while sending SSE event to the client", exception);
            sseEmitterRepository.deleteById(id);
            sseEmitter.completeWithError(exception);
        }
    }

    private SseEmitter createEmitter(String id) {
        SseEmitter sseEmitter = sseEmitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onCompletion(() -> sseEmitterRepository.deleteById(id));

        sseEmitter.onError(throwable -> {
            log.error("[SSE] - ★★★★★★★★SseEmitters / [ onError ]");
            log.error("", throwable);
            sseEmitter.complete();
        });

        return sseEmitter;
    }

    public void sendNotification(MessageRequest messageRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(messageRequest.getChatRoomId())
                .orElseThrow(ChatRoomNotFound::new);

        Customer customer = customerRepository.findByUsername(chatRoom.customer())
                .orElseThrow(CustomerNotFound::new);
        sendChatRoomInformation(String.valueOf(customer.id()));

        Company company = companyRepository.findByUsername(chatRoom.company())
                .orElseThrow(CompanyNotFound::new);
        sendChatRoomInformation(String.valueOf(company.id()));
    }

    private void sendChatRoomInformation(String id) {
        Map<String, SseEmitter> sseEmitters = sseEmitterRepository.findAllByUserId(id);

        sseEmitters.forEach(
                (key, emitter) -> sendToClient(emitter, key, "receive message")
        );
    }
}
